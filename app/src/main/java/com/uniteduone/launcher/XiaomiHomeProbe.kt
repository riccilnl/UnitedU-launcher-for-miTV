package com.uniteduone.launcher

import android.accessibilityservice.AccessibilityService
import android.os.Handler

/**
 * Freeguy: the normal Xiaomi HOME page and its children tab have the same
 * foreground Activity. Inspect only the selected top-navigation header.
 *
 * Fail open on missing accessibility nodes: never interrupt kids playback
 * just because the OEM UI is loading or changes layout.
 */
internal class XiaomiHomeProbe(
    private val service: AccessibilityService,
    private val handler: Handler,
    private val stockHomeActive: () -> Boolean,
    private val onHomeSelected: () -> Unit,
) {
    private var pending = false
    private var sawHome = false
    private val check = object : Runnable {
        override fun run() {
            pending = false
            if (!stockHomeActive()) {
                sawHome = false
                return
            }
            when (readSelectedTab()) {
                XiaomiHomeTab.KIDS, XiaomiHomeTab.UNKNOWN -> sawHome = false
                XiaomiHomeTab.HOME -> {
                    if (sawHome) {
                        sawHome = false
                        onHomeSelected()
                    } else {
                        sawHome = true
                        schedule(350L)
                    }
                }
            }
        }
    }

    fun schedule(delayMs: Long = 650L) {
        if (pending) return
        pending = true
        handler.postDelayed(check, delayMs)
    }

    fun cancel() {
        handler.removeCallbacks(check)
        pending = false
        sawHome = false
    }

    private fun readSelectedTab(): XiaomiHomeTab {
        val root = service.rootInActiveWindow ?: return XiaomiHomeTab.UNKNOWN
        if (root.packageName?.toString() != STOCK_PACKAGE) return XiaomiHomeTab.UNKNOWN
        val nodes = mutableListOf<XiaomiHeaderNode>()
        for (viewId in listOf(KIDS_HEADER, TEXT_HEADER)) {
            for (node in root.findAccessibilityNodeInfosByViewId(viewId).orEmpty()) {
                nodes.add(XiaomiHeaderNode(
                    node.viewIdResourceName,
                    node.text?.toString(),
                    node.contentDescription?.toString(),
                    node.isSelected,
                ))
            }
        }
        return xiaomiHomeTab(nodes)
    }

    private companion object {
        const val STOCK_PACKAGE = "com.mitv.tvhome"
        const val KIDS_HEADER = "com.mitv.tvhome:id/tag_main_header_img"
        const val TEXT_HEADER = "com.mitv.tvhome:id/row_header"
    }
}
