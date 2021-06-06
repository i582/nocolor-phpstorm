package com.vk.nocolor.completion

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.util.TextRange
import com.intellij.psi.*

import com.vk.nocolor.palette.PaletteSingleton

class NoColorPhpSimpleReference(element: PsiElement, textRange: TextRange) :
    PsiReferenceBase<PsiElement?>(element, textRange), PsiPolyVariantReference {
    private val tagColor: String = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val targetsRules = PaletteSingleton.instance.palette.rulesPsiWithColor(tagColor)

        val results: MutableList<ResolveResult> = ArrayList()
        for (targetsRule in targetsRules) {
            results.add(PsiElementResolveResult(targetsRule))
        }
        return results.toTypedArray()
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(false)
        return if (resolveResults.size == 1) resolveResults[0].element else null
    }

    override fun getVariants(): Array<Any> {
        val colors = PaletteSingleton.instance.palette.getColors()
        val variants: MutableList<LookupElement> = mutableListOf()
        for (color in colors) {
            variants.add(
                LookupElementBuilder
                    .create(color).withIcon(AllIcons.Json.Object)
                    .withTypeText(color)
            )
        }
        return variants.toTypedArray()
    }
}
