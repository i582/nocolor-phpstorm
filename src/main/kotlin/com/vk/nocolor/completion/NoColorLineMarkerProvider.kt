package com.vk.nocolor.completion

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.icons.AllIcons
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.jetbrains.php.lang.documentation.phpdoc.psi.impl.tags.PhpDocTagImpl

import com.vk.nocolor.palette.Palette
import com.vk.nocolor.palette.PaletteSingleton

class NoColorLineMarkerProvider : RelatedItemLineMarkerProvider() {
    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>?>
    ) {
        if (element !is PhpDocTagImpl || !Palette.isColorTag(element.name)) {
            return
        }

        val tagColor = Palette.getColorFromString(element.tagValue)
        if (tagColor.isEmpty()) {
            return
        }

        val targetsRules = PaletteSingleton.instance.palette.rulesPsiWithColor(tagColor)

        val builder = NavigationGutterIconBuilder.create(AllIcons.Json.Object)
            .setTargets(targetsRules)
            .setTooltipText("Navigate to color rules")
        result.add(builder.createLineMarkerInfo(element.firstChild))
    }
}
