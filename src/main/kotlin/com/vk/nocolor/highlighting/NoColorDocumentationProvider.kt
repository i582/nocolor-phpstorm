package com.vk.nocolor.highlighting

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.documentation.phpdoc.lexer.PhpDocTokenTypes

class NoColorDocumentationProvider : DocumentationProvider {
    interface HoverDocOverride {
        fun getHoverDesc(): String
        fun getNavigateDesc(): String
    }

    private fun createHoverDocOverride(element: PsiElement, hoverElement: PsiElement): HoverDocOverride? {
        if (hoverElement.node.elementType == PhpDocTokenTypes.DOC_IDENTIFIER) {
            return PhpDocColorHoverDoc(element)
        }
        return null
    }

    // when hovering with cmd — very short, one line info
    override fun getQuickNavigateInfo(element: PsiElement, originalElement: PsiElement): String? {
        val generator = createHoverDocOverride(element, originalElement)
            ?: return null
        return generator.getNavigateDesc()
    }

    // when just hovering — a bit more detailed (but still only most important) info
    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        val generator = createHoverDocOverride(element, originalElement ?: element)
            ?: return null
        return "<pre>" + generator.getHoverDesc() + "</pre>"
    }

    private class PhpDocColorHoverDoc(private val element: PsiElement) : HoverDocOverride {
        override fun getHoverDesc(): String {
            val color = element.text
            if (color == "remover") {
                return """
                    A special <b>embedded</b> color that removes the function from 
                    validation.

                    So, for example, if there is the following chain of calls:
                    
                       f1<b>@highload</b> -> f2<b>@remover</b> -> f3<b>@no-highload</b>
                    
                    Then, during the check, only the functions <b>f1</b>and <b>f3</b> will 
                    remain, the connection between them through <b>f2</b> will be lost.
                    
                    Used to separate strongly connected components in the call
                    graph to improve validation performance.
                """.trimIndent()
            }

           return "Palette color '${element.text}'"
        }

        override fun getNavigateDesc() = getHoverDesc()
    }
}
