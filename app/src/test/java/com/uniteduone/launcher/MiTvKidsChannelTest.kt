package com.uniteduone.launcher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class MiTvKidsChannelTest {
    private val id = MiTvKidsChannel.SHORTCUT_ID

    @Test fun kidsCardIsAdjacentToBilibiliInSameMovieRow() {
        val base = listOf(
            LayoutRow(icon = "movie", apps = listOf("com.example.video", "com.xiaodianshi.tv.yst", "com.example.more")),
            LayoutRow(icon = "tv", apps = emptyList()),
        )
        assertSame(base, defaultLayoutWithKids(base, false))
        val available = defaultLayoutWithKids(base, true)
        assertEquals(base.size, available.size)
        assertEquals("movie", available[0].icon)
        assertEquals(
            listOf("com.example.video", "com.xiaodianshi.tv.yst", id, "com.example.more"),
            available[0].apps,
        )
        assertEquals(base[1], available[1])
        assertFalse(base.any { id in it.apps })
    }

    @Test fun kidsCardUsesMovieRowIfBilibiliNotPresent() {
        val base = listOf(LayoutRow("tv"), LayoutRow("movie", listOf("com.example.movie")))
        val available = defaultLayoutWithKids(base, true)
        assertEquals(base.size, available.size)
        assertEquals(listOf("com.example.movie", id), available[1].apps)
        assertEquals(base[0], available[0])
    }

    @Test fun firstReleaseStandaloneKidsRowMigratesWithoutResettingOtherApps() {
        val old = listOf(
            LayoutRow(icon = "kids", apps = listOf(id)),
            LayoutRow(icon = "movie", apps = listOf("com.example.video", "com.xiaodianshi.tv.yst")),
            LayoutRow(icon = "music", apps = listOf("com.example.music")),
        )
        val moved = mergeLegacyKidsRow(old)
        assertEquals(2, moved.size)
        assertEquals(listOf("com.example.video", "com.xiaodianshi.tv.yst", id), moved[0].apps)
        assertEquals(old[2], moved[1])
        assertSame(moved, mergeLegacyKidsRow(moved))
    }

    @Test fun oneRowLegacyLayoutStillAllowsAddingBilibili() {
        val rows = listOf(LayoutRow(icon = "kids", apps = listOf(id)))
        assertEquals(listOf(LayoutRow(icon = "movie", apps = listOf(id))), mergeLegacyKidsRow(rows))
    }

    @Test fun customizedKidsCardIsNotMoved() {
        val customized = listOf(
            LayoutRow(icon = "kids", apps = listOf(id, "com.example.app")),
            LayoutRow(icon = "movie", apps = listOf("com.xiaodianshi.tv.yst")),
        )
        assertSame(customized, mergeLegacyKidsRow(customized))
    }

    @Test fun shortcutOpensNormalXiaomiKidsTabRatherThanSpecialTopicOrKidsMode() {
        assertEquals("com.mitv.tvhome", MiTvKidsChannel.STOCK_PACKAGE)
        assertEquals("com.mitv.tvhome.HOME_PAGE", MiTvKidsChannel.ACTION)
        assertEquals("com.mitv.tvhome.MainActivity", MiTvKidsChannel.ACTIVITY)
        assertEquals("儿童", MiTvKidsChannel.TARGET_TAB)
        assertTrue(id.startsWith("local.unitedu."))
    }
}
