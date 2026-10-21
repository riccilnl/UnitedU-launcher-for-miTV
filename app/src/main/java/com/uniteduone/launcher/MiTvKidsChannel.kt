package com.uniteduone.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log

/**
 * Shortcut to the normal Xiaomi HOME tab labeled 儿童, not KidsMultipleTabsActivity (a special-topic page).
 *
 * This deliberately does not enter Kids Mode (ENTER_KIDS_MODE), replace content,
 * touch parental-control flags, or read any Xiaomi viewing data.
 */
internal object MiTvKidsChannel {
    const val SHORTCUT_ID = "local.unitedu.mitv.kids.channel"
    const val STOCK_PACKAGE = "com.mitv.tvhome"
    const val ACTION = "com.mitv.tvhome.HOME_PAGE"
    const val TARGET_TAB = "儿童"
    const val ACTIVITY = "com.mitv.tvhome.MainActivity"

    fun available(ctx: Context): Boolean {
        if (!MiTvFreeguy.isDevice()) return false
        val pm = ctx.packageManager
        return runCatching {
            val app = pm.getApplicationInfo(STOCK_PACKAGE, 0)
            val activity = pm.getActivityInfo(ComponentName(STOCK_PACKAGE, ACTIVITY), 0)
            app.enabled && activity.enabled && activity.exported && activity.permission == null
        }.getOrDefault(false)
    }

    fun launch(ctx: Context): Boolean {
        if (!available(ctx)) return false
        val intent = Intent(ACTION)
            .addCategory(Intent.CATEGORY_DEFAULT)
            .setComponent(ComponentName(STOCK_PACKAGE, ACTIVITY))
            .putExtra("page", TARGET_TAB)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching { ctx.startActivity(intent) }
            .onFailure { Log.w("UnitedU", "Xiaomi home kids tab navigation failed", it) }
            .isSuccess
    }
}

/** Xiaomi's normal 儿童 tab lives alongside video apps, not on a dedicated row. */
internal fun defaultLayoutWithKids(base: List<LayoutRow>, include: Boolean): List<LayoutRow> {
    if (!include || base.isEmpty()) return base
    val dest = kidsCardRowIndex(base)
    if (dest < 0) return base
    return base.mapIndexed { index, row ->
        if (index != dest) row else row.copy(apps = placeKidsBesideBilibili(row.apps))
    }
}

/** Prefer the actual Bilibili row, then the movie row, then the first application row. */
private fun kidsCardRowIndex(rows: List<LayoutRow>): Int =
    rows.indexOfFirst { !it.isChannel && BILIBILI_PACKAGE in it.apps }.takeIf { it >= 0 }
        ?: rows.indexOfFirst { !it.isChannel && it.icon == "movie" }.takeIf { it >= 0 }
        ?: rows.indexOfFirst { !it.isChannel }

private const val BILIBILI_PACKAGE = "com.xiaodianshi.tv.yst"

private fun placeKidsBesideBilibili(apps: List<String>): List<String> {
    val id = MiTvKidsChannel.SHORTCUT_ID
    val rest = apps.filterNot { it == id }
    val biliIndex = rest.indexOf(BILIBILI_PACKAGE)
    return if (biliIndex < 0) rest + id
        else rest.take(biliIndex + 1) + id + rest.drop(biliIndex + 1)
}

/** One-time migration of the 2026-10-21 test build's standalone kids row.
 * Change nothing if the user has already placed the card in a normal application row.
 */
internal fun mergeLegacyKidsRow(rows: List<LayoutRow>): List<LayoutRow> {
    val old = rows.indexOfFirst {
        !it.isChannel && it.icon == "kids" && it.apps == listOf(MiTvKidsChannel.SHORTCUT_ID)
    }
    if (old < 0) return rows
    val other = rows.filterIndexed { i, _ -> i != old }
    if (other.isEmpty() || other.all { it.isChannel }) {
        return rows.mapIndexed { i, row -> if (i == old) row.copy(icon = "movie") else row }
    }
    return defaultLayoutWithKids(other, include = true)
}
