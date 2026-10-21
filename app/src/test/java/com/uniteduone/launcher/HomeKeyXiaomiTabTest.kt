package com.uniteduone.launcher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeKeyXiaomiTabTest {
    private fun header(
        id: String, selected: Boolean, text: String? = null,
        description: String? = null,
    ) = XiaomiHeaderNode(id, text, description, selected)

    @Test fun childrenHeaderTakesPrecedenceOverUnselectedHomeLabel() {
        val headers = listOf(
            header("com.mitv.tvhome:id/row_header", false, text = "首页"),
            header("com.mitv.tvhome:id/tag_main_header_img", true, description = "儿童"),
        )
        assertEquals(XiaomiHomeTab.KIDS, xiaomiHomeTab(headers))
        assertFalse(shouldRedirectXiaomiStockHome(true, xiaomiHomeTab(headers)))
    }

    @Test fun homeSelectedAfterHomeKeyMustReturnUnitedU() {
        val headers = listOf(
            header("com.mitv.tvhome:id/row_header", true, text = "首页"),
            header("com.mitv.tvhome:id/tag_main_header_img", false, description = "儿童"),
        )
        assertEquals(XiaomiHomeTab.HOME, xiaomiHomeTab(headers))
        assertTrue(shouldRedirectXiaomiStockHome(true, xiaomiHomeTab(headers)))
        assertFalse(shouldRedirectXiaomiStockHome(false, xiaomiHomeTab(headers)))
    }

    @Test fun unknownAndUnselectedTabsNeverForceExitKids() {
        assertEquals(XiaomiHomeTab.UNKNOWN, xiaomiHomeTab(emptyList()))
        assertEquals(XiaomiHomeTab.UNKNOWN, xiaomiHomeTab(listOf(
            header("com.mitv.tvhome:id/row_header", false, text = "首页"),
            header("com.mitv.tvhome:id/row_header", true, text = "免费"),
        )))
        assertFalse(shouldRedirectXiaomiStockHome(true, XiaomiHomeTab.UNKNOWN))
    }
}
