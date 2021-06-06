package com.vk.nocolor.configuration

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

import com.vk.nocolor.palette.Palette
import com.vk.nocolor.palette.PaletteSingleton

class NoColorStartupActivity : StartupActivity {
    override fun runActivity(project: Project) {
        PaletteSingleton.instance.palette.init(Palette.defaultPalettePath, project)
        println(PaletteSingleton.instance.palette.getColors())
    }
}
