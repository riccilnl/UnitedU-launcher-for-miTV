package com.uniteduone.launcher

/**
 * The Xiaomi HOME activity hosts the normal home tab and the kids tab in the
 * SAME Activity. Window-class matching alone cannot distinguish them.
 *
 * Only the selected top navigation tab counts. Nonselected labels may remain
 * in the accessibility tree at the same time.
 */
internal enum class XiaomiHomeTab { KIDS, HOME, UNKNOWN }

internal data class XiaomiHeaderNode(
    val id: String?,
    val text: String?,
    val description: String?,
    val selected: Boolean,
)

internal fun xiaomiHomeTab(nodes: List<XiaomiHeaderNode>): XiaomiHomeTab {
    if (nodes.any { it.selected && it.id?.endsWith("/tag_main_header_img") == true &&
                it.description == "儿童" }) return XiaomiHomeTab.KIDS
    if (nodes.any { it.selected && it.id?.endsWith("/row_header") == true &&
                it.text == "首页" }) return XiaomiHomeTab.HOME
    return XiaomiHomeTab.UNKNOWN
}

internal fun shouldRedirectXiaomiStockHome(active: Boolean, tab: XiaomiHomeTab): Boolean =
    active && tab == XiaomiHomeTab.HOME
