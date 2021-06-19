package com.vk.nocolor.completion

import com.intellij.openapi.util.TextRange
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceRegistrar
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.PsiReferenceContributor
import com.intellij.util.ProcessingContext
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes
import com.jetbrains.php.lang.documentation.phpdoc.psi.tags.PhpDocTag

import com.vk.nocolor.palette.Palette

class NoColorPhpReferenceContributor : PsiReferenceContributor() {
    override fun registerReferenceProviders(registrar: PsiReferenceRegistrar) {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(PhpDocTokenTypes.DOC_IDENTIFIER),
            object : PsiReferenceProvider() {
                override fun getReferencesByElement(
                    element: PsiElement,
                    context: ProcessingContext
                ): Array<PsiReference> {
                    val tagNode = element.parent.parent
                    if (tagNode !is PhpDocTag) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val tagName = tagNode.name
                    if (!Palette.isColorTag(tagName)) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val tagColor = Palette.getColorFromString(tagNode.tagValue)
                    if (tagColor.isEmpty()) {
                        return PsiReference.EMPTY_ARRAY
                    }
                    val parts = tagNode.tagValue.split(" ")
                    if (element.text in parts && parts[0] != element.text) {
                        return PsiReference.EMPTY_ARRAY
                    }

                    val property = TextRange(0, tagColor.length)
                    return arrayOf(NoColorPhpSimpleReference(element, property))
                }
            }
        )
    }
}
