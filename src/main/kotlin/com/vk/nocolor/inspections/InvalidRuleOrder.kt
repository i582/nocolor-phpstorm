package com.vk.nocolor.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import org.jetbrains.yaml.psi.YAMLKeyValue
import org.jetbrains.yaml.psi.YAMLQuotedText
import org.jetbrains.yaml.psi.YAMLSequence
import org.jetbrains.yaml.psi.YamlPsiElementVisitor

class InvalidRuleOrder : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : YamlPsiElementVisitor() {
            override fun visitSequence(sequence: YAMLSequence) {
                if (!sequence.containingFile.name.endsWith("palette.yaml")) {
                    return
                }

                var errorRuleFound = false

                for (item in sequence.items) {
                    val keys = item.keysValues
                    if (keys.size > 1) {
                        keys.forEachIndexed { index: Int, key: YAMLKeyValue ->
                            if (index != 0) {
                                holder.registerProblem(key, "Each element in group must have only one" +
                                        " key-value pair. Place this pair in a separate element of the array " +
                                        "(add '- ' before the pair)")
                            }
                        }
                    }
                    if (keys.isEmpty() || keys.toTypedArray()[0].value == null) {
                        continue
                    }

                    val mainKey = keys.toTypedArray()[0]

                    if (mainKey.value!! is YAMLQuotedText && mainKey.value!!.text.length == 2) {
                        if (!errorRuleFound) {
                            holder.registerProblem(mainKey, "In the group, first there should be " +
                                    "rules describing the error, and then the exclusion rules")
                        }
                    } else {
                        errorRuleFound = true
                    }
                }
            }
        }
    }
}
