package com.vk.nocolor.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

import com.vk.nocolor.palette.Palette

class EmptyColorTag : PhpInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {

            override fun visitPhpDocTag(tag: PhpDocTag?) {
                if (tag == null) {
                    return
                }

                if (!Palette.isColorTag(tag.name)) {
                    return
                }

                val colorName = Palette.getColorFromString(tag.tagValue)
                if (colorName.isEmpty()) {
                    holder.registerProblem(tag, "Specify a color from the palette (with an optional comment after)")
                }
            }
        }
    }
}
