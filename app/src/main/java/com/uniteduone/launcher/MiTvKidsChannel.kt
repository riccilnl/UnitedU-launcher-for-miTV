package com.uniteduone.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log

/**
 * Shortcut to Xiaomi's EXISTING kids channel inside PatchWall.
 *
 * This deliberately does not enter Kids Mode (ENTER_KIDS_MODE), replace content,
 * touch parental-control flags, or read any Xiaomi viewing data.
 */
internal object MiTvKidsChannel {
    const val SHORTCUT_ID = "local.unitedu.mitv.kids.channel"
    const val STOCK_PACKAGE = "com.mitv.tvhome"
    const val ACTION = "android.intent.action.ENTER_KIDS_MULTIPLE_TABS"
    const val ACTIVITY = "com.mitv.tvhome.business.kiddesk.KidsMultipleTabsActivity"

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
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return runCatching { ctx.startActivity(intent) }
            .onFailure { Log.w("UnitedU", "Xiaomi kids channel activity failed", it) }
            .isSuccess
    }
}

/** Insert only for first-run defaults, never edit existing user layout. */
internal fun defaultLayoutWithKids(base: List<LayoutRow>, include: Boolean): List<LayoutRow> =
    if (include) listOf(LayoutRow(icon = "kids", apps = listOf(MiTvKidsChannel.SHORTCUT_ID))) + base
    else base
