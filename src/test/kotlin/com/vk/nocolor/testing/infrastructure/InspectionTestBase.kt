package com.vk.nocolor.testing.infrastructure

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.php.lang.inspections.PhpInspection
import com.vk.nocolor.palette.PaletteSingleton
import java.io.File

abstract class InspectionTestBase(
        private val inspectionToEnable: PhpInspection
) : BasePlatformTestCase() {

    override fun getTestDataPath() = "src/test/fixtures"

    override fun setUp() {
        super.setUp()

        myFixture.enableInspections(inspectionToEnable)
    }

    /**
     * Run inspection on file.fixture.php and check that all <warning> and <error> match
     * If file.qf.php exists, apply quickfixes and compare result to file.qf.php
     */
    protected fun runFixture(fixtureFile: String) {
        PaletteSingleton.instance.palette.initForTest()

        myFixture.configureByFile(fixtureFile)
        myFixture.testHighlighting(true, false, true)
    }
}
