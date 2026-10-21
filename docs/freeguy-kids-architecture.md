# 小米儿童频道：独立快捷方式，而不是 UnitedU 内置卡片

为不绑定任何桌面，`freeguy` 分支保留小米信号源和设置适配，但**撤回** UnitedU 内的 `MiTvKidsChannel` 虚拟卡片、默认布局注入和虚拟卸载/保活代码。

独立 Android TV 快捷方式源码位于 `extras/mitv-kids-shortcut/`。APK 包名 `com.riccilnl.mitv.kids`，会作为普通应用出现在 UnitedU、Projectivy 和小米桌面的应用选择器中；按用户要求与哔哩哔哩同一行时，在**个人布局**中放置即可，核心桌面不包含定制规则。

原厂导航方式（在实际电视上通过 ADB Shell 验证）：`com.mitv.tvhome.HOME_PAGE`，component `com.mitv.tvhome/.MainActivity`，string extra `page=儿童`。仍待首次以第三方 APK 发起 Intent 的实机验证。

2026-10-21 的旧测试 APK `com.uniteduone.launcher` 含有独立的虚拟儿童行。升级到移除虚拟卡片的新版本时，旧布局可能出现空儿童行，须通过 UnitedU 布局编辑器移除空行，并把新快捷方式作为**普通应用**添加到哔哩哔哩所在行。禁止清除原厂 `com.mitv.tvhome` 数据。


## 实机验收更新（2026-10-21）

- 独立 APK `com.riccilnl.mitv.kids` 版本 `0.1.0` 已在 MiTV-MFTR0 安装成功。
- Android `LEANBACK_LAUNCHER` 入口可正常解析；点击其 Activity 后回到 `com.mitv.tvhome/.MainActivityUserMode`。
- 原厂儿童栏目成功显示动画推荐和 `观看至第4集` / `查看更多历史` 等继续观看内容。
- 小米 HOME role 仍为 `com.mitv.tvhome`；无障碍服务、ADB 和儿童锁均未因本应用修改。
- 独立 APK 已通过 SDK 34 的 `aapt`/`javac`/`d8`/`apksigner` 本地签名安装测试；Gradle Android 35 GitHub 工作流也已构建通过。
- 旧 UnitedU 测试 APK 已安装的虚拟儿童卡片不会因 Git 更新自动消失；待 UnitedU 升级后再从正常应用选择器添加独立快捷方式到哔哩哔哩所在行。
