package com.vk.nocolor.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.PhpDocComment
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.PhpClass
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

import com.vk.nocolor.palette.Palette

class DuplicatedColor : PhpInspection() {
    val usedTags = mutableSetOf<String>()

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is PhpDocComment) {
                    usedTags.clear()
                } else if (element is PhpClass) {
                    handleClass(element, holder)
                }
            }

            override fun visitPhpDocTag(tag: PhpDocTag?) {
                if (tag == null) {
                    return
                }

                if (!Palette.isColorTag(tag.name)) {
                    return
                }

                val colorName = Palette.getColorFromString(tag.tagValue)
                if (colorName.isEmpty()) {
                    return
                }

                val containsName = usedTags.contains(colorName)
                if (containsName) {
                    holder.registerProblem(tag, "Duplicate color '$colorName'")
                }

                usedTags.add(colorName)
            }

            private fun handleClass(
                element: PhpClass,
                holder: ProblemsHolder
            ) {
                if (element.docComment == null) {
                    return
                }

                val classDocColors = getColorsFromDoc(element)

                for (method in element.methods) {
                    if (method.docComment == null) {
                        continue
                    }

                    val docColors = getColorsFromDoc(method)

                    for (docColor in docColors) {
                        if (classDocColors.containsKey(docColor.key)) {
                            holder.registerProblem(
                                docColor.value, "The color '${docColor.key}' has already been declared for the class"
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getColorsFromDoc(element: PsiElement): MutableMap<String, PsiElement> {
        val docComment = when (element) {
            is PhpClass -> {
                element.docComment
            }
            is Method -> {
                element.docComment
            }
            else -> return mutableMapOf()
        } ?: return mutableMapOf()

        val colors = mutableMapOf<String, PsiElement>()
        for (colorTag in Palette.colorTags) {
            val classDocColorElements = docComment.getTagElementsByName(colorTag)
            for (classDocColorElement in classDocColorElements) {
                val color = Palette.getColorFromString(classDocColorElement.tagValue)
                if (color.isEmpty()) {
                    continue
                }

                colors[color] = classDocColorElement
            }
        }

        return colors
    }
}
