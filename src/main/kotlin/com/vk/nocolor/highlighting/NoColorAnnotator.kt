package com.vk.nocolor.highlighting

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl
import com.jetbrains.php.lang.highlighter.PhpHighlightingData

import com.vk.nocolor.palette.Palette

class NoColorAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is PhpDocTagImpl -> onPhpdocTag(element, holder)
        }
    }

    private fun onPhpdocTag(element: PhpDocTagImpl, holder: AnnotationHolder) {
        val tagNameE = element.firstChild ?: return

        if (Palette.isColorTag(element.name)) {
            holder.textAttributes(
                tagNameE,
                TextAttributesKey.createTextAttributesKey("PHPDOC_TAG_REGULAR", PhpHighlightingData.DOC_TAG)
            )
        } else {
            holder.textAttributes(
                tagNameE,
                TextAttributesKey.createTextAttributesKey("PHPDOC_TAG_REGULAR", PhpHighlightingData.DOC_TAG)
            )
        }
    }

    private fun AnnotationHolder.textAttributes(element: PsiElement, textAttributes: TextAttributesKey) {
        newSilentAnnotation(HighlightSeverity.INFORMATION).range(element).textAttributes(textAttributes).create()
    }
}
