package com.vk.nocolor.inspections

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.jetbrains.php.lang.inspections.PhpInspection
import com.jetbrains.php.lang.psi.elements.Function
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

import com.vk.nocolor.palette.Palette

class OtherColorsWithRemoverColor : PhpInspection() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : PhpElementVisitor() {

            override fun visitPhpFunction(function: Function?) {
                if (function == null) {
                    return
                }
                checkRemoverColorForElement(function, holder)
            }

            override fun visitPhpMethod(method: Method?) {
                if (method == null) {
                    return
                }
                checkRemoverColorForElement(method, holder)
            }
        }
    }

    private fun checkRemoverColorForElement(
        function: PsiElement,
        holder: ProblemsHolder
    ) {
        val docColors = Palette.getColorsFromDoc(function)
        if (docColors.isEmpty()) {
            return
        }

        if (docColors.containsKey(Palette.specialColorRemover) && docColors.size > 1) {
            for (docColor in docColors) {
                if (docColor.key == Palette.specialColorRemover) {
                    continue
                }

                holder.registerProblem(
                    docColor.value,
                    "The function contains the color remover, any other colors for this function are meaningless"
                )
            }
        }
    }
}
