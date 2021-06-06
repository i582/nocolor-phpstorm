package com.vk.nocolor.completion

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes

import com.vk.nocolor.palette.Palette
import com.vk.nocolor.palette.PaletteSingleton

class NoColorCompletionContributor : CompletionContributor() {
    init {
        extend(
            CompletionType.BASIC, PlatformPatterns.psiElement(PhpDocTokenTypes.DOC_IDENTIFIER),
            object : CompletionProvider<CompletionParameters?>() {
                public override fun addCompletions(
                    parameters: CompletionParameters,
                    context: ProcessingContext,
                    resultSet: CompletionResultSet
                ) {
                    val tagNamePsi = parameters.position.parent.parent.firstChild
                    val tagName = tagNamePsi.text
                    if (Palette.isColorTag(tagName)) {
                        return
                    }

                    val colors = PaletteSingleton.instance.palette.getColors()
                    for (color in colors) {
                        resultSet.addElement(LookupElementBuilder.create(color))
                    }
                }
            }
        )
    }
}
