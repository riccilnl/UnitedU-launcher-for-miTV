package com.uniteduone.launcher

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * Known Xiaomi MiTV-MFTR0 (freeguy) routes. Keep these local to the device adapter;
 * other Android TVs continue to use TvInputManager + TvContract.
 */
internal data class FreeguySource(
    val label: String,
    val passthrough: Boolean,
    val action: String,
    val activity: String,
    val inputExtra: Int? = null,
)

internal object MiTvFreeguy {
    private const val TV_PLAYER = "com.xiaomi.mitv.tvplayer"
    private const val HDMI = "com.mediatek.tvinput/.hdmi.HDMIInputService/"
    private const val TUNER = "com.mediatek.tvinput/.tuner.TunerInputService/"
    private const val COMPOSITE = "com.mediatek.tvinput/.composite.CompositeInputService/"

    fun isDevice(): Boolean =
        Build.DEVICE.equals("freeguy", ignoreCase = true) &&
            Build.MODEL.equals("MiTV-MFTR0", ignoreCase = true)

    // Pure lookup: HW8 is a framework input, not a fourth physical HDMI socket.
    fun sourceFor(inputId: String): FreeguySource? = when (inputId) {
        HDMI + "HW5" -> external("HDMI 1", 23)
        HDMI + "HW6" -> external("HDMI 2", 24)
        HDMI + "HW7" -> external("HDMI 3", 25)
        COMPOSITE + "HW2" -> external("AV", 2)
        TUNER + "HW1" -> FreeguySource("ATV", false,
            "$TV_PLAYER.ATV_PLAY", "$TV_PLAYER.AtvActivity")
        TUNER + "HW0" -> FreeguySource("DTMB", false,
            "$TV_PLAYER.DTMB_PLAY", "$TV_PLAYER.dtmb.DTMBActivity")
        else -> null
    }

    private fun external(label: String, input: Int) = FreeguySource(
        label, true, "$TV_PLAYER.EXTSRC_PLAY", "$TV_PLAYER.ExternalSourceActivity", input)

    fun launch(ctx: Context, source: FreeguySource): Boolean {
        val intent = Intent(source.action)
            .setComponent(ComponentName(TV_PLAYER, source.activity))
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        source.inputExtra?.let { intent.putExtra("input", it) }
        return runCatching { ctx.startActivity(intent) }
            .onFailure { Log.w("UnitedU", "freeguy input " + source.label + " failed", it) }
            .isSuccess
    }

    fun settingsIntent(): Intent? = if (isDevice()) {
        Intent("com.xiaomi.mitv.settings.SETTINGS")
            .setComponent(ComponentName("com.xiaomi.mitv.settings",
                "com.xiaomi.mitv.settings.entry.MainActivity"))
    } else null

    fun wifiSettingsIntent(): Intent? = if (isDevice()) {
        Intent("com.xiaomi.mitv.settings.WIFI_SETTINGS")
            .setComponent(ComponentName("com.xiaomi.mitv.settings",
                "com.xiaomi.mitv.settings.wifi.MitvWifiActivity"))
    } else null
}
