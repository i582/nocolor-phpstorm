package com.vk.nocolor.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YamlPsiElementVisitor

class NotAllowedColorsInRules : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : YamlPsiElementVisitor() {
            override fun visitKeyValue(keyValue: YAMLKeyValue) {
                if (!keyValue.containingFile.name.endsWith("palette.yaml")) {
                    return
                }

                super.visitKeyValue(keyValue)

                val valueNode = keyValue.value ?: return
                if (valueNode is YAMLSequence) {
                    return
                }

                val keyNode = keyValue.key ?: return
                val keyTextWithQuotes = keyNode.text ?: return
                if (keyTextWithQuotes.isEmpty()) {
                    return
                }

                val keyText = if (keyTextWithQuotes[0] == '"' || keyTextWithQuotes[0] == '\'') {
                    keyTextWithQuotes.slice(1 until keyTextWithQuotes.length - 1)
                } else {
                    keyTextWithQuotes
                }

                val rawColors = keyText.split(' ')
                for (color in rawColors) {
                    if (color == "transparent") {
                        holder.registerProblem(keyNode, "Use of 'transparent' color is " +
                                "prohibited in the rules")
                    }
                    if (color == "*") {
                        holder.registerProblem(keyNode, "Use of 'wildcard' color is " +
                                "prohibited in the rules")
                    }
                }
            }
        }
    }
}
