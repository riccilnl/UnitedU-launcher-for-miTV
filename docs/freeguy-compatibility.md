# Xiaomi MiTV-MFTR0 (`freeguy`) adaptation

This branch preserves the upstream UnitedU UI and includes a device-specific
input/settings routing layer. It does not disable the Xiaomi launcher, grant
system permissions, or modify the TV HOME role.

## Implemented

- On `Build.DEVICE=freeguy`, `Build.MODEL=MiTV-MFTR0`:
  - HDMI 1/2/3 use Xiaomi's explicit source activity and integer input 23/24/25.
  - AV uses the same activity with input 2.
  - ATV and DTMB open their separate Xiaomi TVPlayer activities.
  - A synthetic fourth HDMI (`HW8`) is excluded.
  - Distinct ATV and DTMB items are preserved rather than merged into one "TV".
  - The main system-settings action opens Xiaomi settings, not AOSP settings.
- All other models retain UnitedU's existing standard Android TV routes.
- UnitedU's built-in Home Button Takeover is unchanged.

## Evidence / limits

The activity mappings come from a real-device `MiTV-MFTR0` compatibility
report for firmware `OS3.0.101.0.UFTMATV` (Android 14). The user's unit is
`OS2.0.5.0.RFTMATV` (Android 11): validate each route on this build before
claiming verified compatibility. Hardware signal playback and D-pad behavior
need real remote/hardware tests. Unsupported IDs are not guessed.

The built-in HDMI/input enumeration still uses `TvInputManager`; no hardware
inputs appear if Xiaomi stops exposing them.

## Test sequence

1. Run `:app:testDebugUnitTest` and `:app:assembleRelease`.
2. Install the APK as a regular app, leaving `com.mitv.tvhome` as HOME.
3. Verify applications, Xiaomi settings, HDMI 1/2/3, AV, ATV and DTMB.
4. Test UnitedU's accessibility takeover only after basic navigation passes.
5. Reboot and confirm official HOME and ADB access remain recoverable.

**Never disable** `com.xiaomi.mitv.settings`, `com.mitv.tvhome`,
`com.xiaomi.mitv.tvplayer`, or MediaTek TV input services.
