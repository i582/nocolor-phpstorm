package com.vk.nocolor.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

import com.vk.nocolor.palette.Palette
import com.vk.nocolor.palette.PaletteSingleton

class PossibleSeveralColors : PhpInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {

            override fun visitPhpDocTag(tag: PhpDocTag?) {
                if (tag == null || !Palette.isColorTag(tag.name)) {
                    return
                }

                val parts = tag.tagValue.split(" ")
                if (parts.size < 2) {
                    return
                }

                val secondPart = parts[1]
                val containsName = PaletteSingleton.instance.palette.containsColor(secondPart)
                if (containsName) {
                    holder.registerProblem(tag, "In @color tag is prohibited more than one color. " +
                            "Please use multiple tags")
                }
            }
        }
    }
}
