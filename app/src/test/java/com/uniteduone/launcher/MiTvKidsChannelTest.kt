package com.uniteduone.launcher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MiTvKidsChannelTest {
    @Test fun defaultLayoutAddsKidsBeforeOtherRowsOnlyWhenAvailable() {
        val base = listOf(LayoutRow(icon = "movie", apps = listOf("com.example.video")),
            LayoutRow(icon = "tv", apps = emptyList()))
        val notAvailable = defaultLayoutWithKids(base, false)
        assertSame(base, notAvailable)
        val available = defaultLayoutWithKids(base, true)
        assertEquals(3, available.size)
        assertEquals("kids", available[0].icon)
        assertEquals(listOf(MiTvKidsChannel.SHORTCUT_ID), available[0].apps)
        assertEquals(base, available.drop(1))
        assertFalse(base.any { MiTvKidsChannel.SHORTCUT_ID in it.apps })
    }

    @Test fun shortcutTargetsXiaomiKidsTabNotProtectedKidsMode() {
        assertEquals("com.mitv.tvhome", MiTvKidsChannel.STOCK_PACKAGE)
        assertEquals("android.intent.action.ENTER_KIDS_MULTIPLE_TABS", MiTvKidsChannel.ACTION)
        assertEquals("com.mitv.tvhome.business.kiddesk.KidsMultipleTabsActivity",
            MiTvKidsChannel.ACTIVITY)
        assertTrue(MiTvKidsChannel.SHORTCUT_ID.startsWith("local.unitedu."))
    }
}
