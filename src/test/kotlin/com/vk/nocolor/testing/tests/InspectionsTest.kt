package com.vk.nocolor.testing.tests

import com.vk.nocolor.inspections.DuplicatedColor
import com.vk.nocolor.inspections.UndefinedColor
import com.vk.nocolor.testing.infrastructure.InspectionTestBase

class InspectionsTest : InspectionTestBase(UndefinedColor()) {
    fun testUndefinedColors() {
        runFixture("inspections/undefined_colors.fixture.php")
    }
}
