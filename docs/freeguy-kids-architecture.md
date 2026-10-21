# 小米儿童频道：独立快捷方式，而不是 UnitedU 内置卡片

为不绑定任何桌面，`freeguy` 分支保留小米信号源和设置适配，但**撤回** UnitedU 内的 `MiTvKidsChannel` 虚拟卡片、默认布局注入和虚拟卸载/保活代码。

独立 Android TV 快捷方式源码位于 `extras/mitv-kids-shortcut/`。APK 包名 `com.riccilnl.mitv.kids`，会作为普通应用出现在 UnitedU、Projectivy 和小米桌面的应用选择器中；按用户要求与哔哩哔哩同一行时，在**个人布局**中放置即可，核心桌面不包含定制规则。

原厂导航方式（在实际电视上通过 ADB Shell 验证）：`com.mitv.tvhome.HOME_PAGE`，component `com.mitv.tvhome/.MainActivity`，string extra `page=儿童`。仍待首次以第三方 APK 发起 Intent 的实机验证。

2026-10-21 的旧测试 APK `com.uniteduone.launcher` 含有独立的虚拟儿童行。升级到移除虚拟卡片的新版本时，旧布局可能出现空儿童行，须通过 UnitedU 布局编辑器移除空行，并把新快捷方式作为**普通应用**添加到哔哩哔哩所在行。禁止清除原厂 `com.mitv.tvhome` 数据。
