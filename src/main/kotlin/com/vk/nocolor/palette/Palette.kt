package com.vk.nocolor.palette

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.elements.impl.ArrayCreationExpressionImpl
import com.jetbrains.php.lang.psi.elements.impl.ArrayHashElementImpl
import com.jetbrains.php.lang.psi.visitors.PhpRecursiveElementVisitor
import org.jetbrains.annotations.NotNull
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YamlRecursivePsiElementVisitor

class PaletteSingleton {
    var palette: Palette = Palette()

    companion object {
        private var INSTANCE: PaletteSingleton? = null

        val instance: PaletteSingleton
            get() {
                if (INSTANCE == null) {
                    INSTANCE = PaletteSingleton()
                }
                return INSTANCE!!
            }
    }
}

class Palette {
    private lateinit var fileName: String
    private var colors: MutableSet<String> = mutableSetOf()
    private var file: PsiFile? = null
    private var fromYaml: Boolean = true

    fun init(fileNameYaml: String, fileNamePhp: String, project: Project) {
        val paletteYamlPsiFiles = FilenameIndex.getFilesByName(project, fileNameYaml, GlobalSearchScope.allScope(project))
        val palettePhpPsiFiles = FilenameIndex.getFilesByName(project, fileNamePhp, GlobalSearchScope.allScope(project))

        if (paletteYamlPsiFiles.isNotEmpty()) {
            this.fileName = fileNameYaml
            this.file = paletteYamlPsiFiles[0]
        } else if (palettePhpPsiFiles.isNotEmpty()) {
            this.fileName = fileNamePhp
            this.file = palettePhpPsiFiles[0]
            this.fromYaml = false
        }

        this.getAllColorsFromFile()
        this.addAsyncListener()
    }

    fun getColors(): Array<String> {
        return colors.map { it }.toTypedArray()
    }

    fun containsColor(color: String): Boolean {
        return colors.contains(color)
    }

    companion object {
        const val defaultPaletteYamlName = "palette.yaml"
        const val defaultPaletteKphpConfigurationName = "KphpConfiguration.php"
        const val defaultConstKeyInKphpConfiguration = "FUNCTION_PALETTE"

        fun isColorTag(tag: String): Boolean {
            return tag in arrayOf("@color", "@kphp-color")
        }

        fun getColorFromString(str: String): String {
            if (str.isEmpty()) {
                return ""
            }

            val parts = str.split(" ")
            if (parts.isEmpty()) {
                return ""
            }

            return parts[0]
        }
    }

    fun rulesPsiWithColor(tagColor: String): MutableList<PsiElement> {
        val targetsRules: MutableList<PsiElement> = ArrayList()

        visitColorsInFile { color, ruleElement ->
            Boolean
            if (color == tagColor) {
                targetsRules.add(ruleElement)
                return@visitColorsInFile false
            }
            true
        }
        return targetsRules
    }

    private fun visitColorsInFile(cb: (String, PsiElement) -> Boolean) {
        if (file == null) {
            return
        }

        if (fromYaml) {
            file!!.accept(object : YamlRecursivePsiElementVisitor() {
                override fun visitKeyValue(@NotNull keyValue: YAMLKeyValue) {
                    super.visitKeyValue(keyValue)

                    val keyNode = keyValue.key
                    val keyTextWithQuotes = keyNode?.text ?: return
                    handleRawKeyText(keyTextWithQuotes, keyNode, cb)
                }
            })
            return
        }

        var arrayWithColors: PsiElement? = null

        file!!.accept(object : PhpRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)

                if (element is PhpClass) {
                    for (field in element.fields) {
                        if (!field.isConstant) {
                            continue
                        }

                        if (field.name != defaultConstKeyInKphpConfiguration) {
                            continue
                        }

                        arrayWithColors = field.defaultValue
                    }
                }
            }
        })

        if (arrayWithColors == null) {
            return
        }

        arrayWithColors!!.accept(object : PhpRecursiveElementVisitor() {
            override fun visitElement(element: PsiElement) {
                super.visitElement(element)

                if (element is ArrayCreationExpressionImpl) {
                    val groupValues = element.parameters
                    for (groupValue in groupValues) {
                        if (groupValue is ArrayHashElementImpl) {
                            val key = groupValue.key ?: continue
                            handleRawKeyText(key.text, key, cb)
                        }
                    }
                }
            }
        })
    }

    private fun handleRawKeyText(
        keyTextWithQuotes: String,
        keyNode: PsiElement,
        cb: (String, PsiElement) -> Boolean,
    ) {
        // if empty "" or ''
        if (keyTextWithQuotes.length == 2) {
            return
        }

        val keyText = keyTextWithQuotes.slice(1 until keyTextWithQuotes.length - 1)
        val rawColors = keyText.split(' ')
        for (color in rawColors) {
            if (!cb(color, keyNode)) {
                break
            }
        }
    }

    private fun addAsyncListener() {
        val paletteFile = this.file ?: return

        VirtualFileManager.getInstance().addAsyncFileListener({
            for (event in it) {
                if (event.file == null) {
                    continue
                }

                if (event.file!!.name == paletteFile.name) {
                    this.getAllColorsFromFile()
                }
            }
            null
        }, {})
    }

    private fun getAllColorsFromFile() {
        colors.clear()
        visitColorsInFile { color: String, _: PsiElement -> Boolean
            colors.add(color)
            true
        }
        colors.add("remover")
    }
}
