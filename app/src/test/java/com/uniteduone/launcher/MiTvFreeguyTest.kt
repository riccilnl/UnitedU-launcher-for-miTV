package com.uniteduone.launcher

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Test

class MiTvFreeguyTest {
    private val hdmi = "com.mediatek.tvinput/.hdmi.HDMIInputService/"
    private val tuner = "com.mediatek.tvinput/.tuner.TunerInputService/"
    private val av = "com.mediatek.tvinput/.composite.CompositeInputService/"

    @Test fun physicalPortsHaveDistinctXiaomiSourceCodes() {
        assertEquals(23, MiTvFreeguy.sourceFor(hdmi + "HW5")?.inputExtra)
        assertEquals(24, MiTvFreeguy.sourceFor(hdmi + "HW6")?.inputExtra)
        assertEquals(25, MiTvFreeguy.sourceFor(hdmi + "HW7")?.inputExtra)
        assertNull(MiTvFreeguy.sourceFor(hdmi + "HW8"))
        assertEquals("HDMI 2", MiTvFreeguy.sourceFor(hdmi + "HW6")?.label)
        assertEquals(2, MiTvFreeguy.sourceFor(av + "HW2")?.inputExtra)
    }

    @Test fun analogueAndDigitalTvKeepSeparateLaunchTargets() {
        val analog = MiTvFreeguy.sourceFor(tuner + "HW1")!!
        val digital = MiTvFreeguy.sourceFor(tuner + "HW0")!!
        assertEquals("ATV", analog.label)
        assertEquals("DTMB", digital.label)
        assertFalse(analog.passthrough)
        assertFalse(digital.passthrough)
        assertNull(analog.inputExtra)
        assertNull(digital.inputExtra)
        assertEquals("com.xiaomi.mitv.tvplayer.AtvActivity", analog.activity)
        assertEquals("com.xiaomi.mitv.tvplayer.dtmb.DTMBActivity", digital.activity)
        assertNull(MiTvFreeguy.sourceFor("other/HW5"))
    }
}
