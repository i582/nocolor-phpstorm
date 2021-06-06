package com.vk.nocolor.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.elementType
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.lexer.PhpTokenTypes
import com.jetbrains.php.lang.parser.PhpElementTypes
import com.jetbrains.php.lang.psi.elements.FunctionReference
import com.jetbrains.php.lang.psi.elements.MethodReference
import com.jetbrains.php.lang.psi.elements.UnaryExpression
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

import com.vk.nocolor.palette.Palette
import com.vk.nocolor.palette.PaletteSingleton

class UndefinedColor : PhpInspection() {
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
                    return
                }

                val containsName = PaletteSingleton.instance.palette.containsColor(colorName)
                if (!containsName) {
                    holder.registerProblem(tag, "Undefined color '$colorName'")
                }
            }
        }
    }
}
