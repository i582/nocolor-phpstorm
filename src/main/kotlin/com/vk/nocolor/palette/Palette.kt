package com.vk.nocolor.palette

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
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

    fun init(fileName: String, project: Project) {
        val palettePsiFiles = FilenameIndex.getFilesByName(project, fileName, GlobalSearchScope.allScope(project))
        this.fileName = fileName
        this.file = if (palettePsiFiles.isEmpty()) null else palettePsiFiles[0]

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
        const val defaultPalettePath = "palette.yaml"

        fun isColorTag(tag: String): Boolean {
            return tag in arrayOf("@color")
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

    fun visitColorsInFile(cb: (String, PsiElement) -> Boolean) {
        if (file == null) {
            return
        }

        file!!.accept(object : YamlRecursivePsiElementVisitor() {
            override fun visitKeyValue(@NotNull keyValue: YAMLKeyValue) {
                super.visitKeyValue(keyValue)

                val keyNode = keyValue.key
                val keyTextWithQuotes = keyNode?.text ?: return
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
        })
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
    }
}
