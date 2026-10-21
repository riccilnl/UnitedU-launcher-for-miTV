# 小米儿童频道快捷方式

独立 Android TV APK，安装后像普通应用一样出现在应用列表中。无需 UnitedU，也无需 Google 服务。

点击图标调用**小米原厂桌面顶部的「儿童」栏目**：

- action: `com.mitv.tvhome.HOME_PAGE`
- component: `com.mitv.tvhome/.MainActivity`
- extra: `page=儿童`

该行为已在 `MiTV-MFTR0 / freeguy`、小米 HyperOS `OS2.0.5.0.RFTMATV` 的原厂桌面通过 ADB 验证；从本 APK 调用的权限及返回行为仍需实机验收。

应用不包含动画内容，不读取或改变观看记录、推荐、儿童锁、家长控制或账户数据。**必须保留 `com.mitv.tvhome` 及其相关原厂服务。**

## 安装与使用

下载构建得到的 APK，正常安装。UnitedU / Projectivy / 小米原厂桌面应能将其识别为可启动应用；在各自的应用管理中自行加入常用应用行。不会自动接管 HOME，不会更改其他桌面布局。

这个 APK 目前是测试版。GitHub Actions 自动生成的 Debug 包可能因签名不同无法直接覆盖以后重新生成的 Debug 包。正式长期发布之前需要固定并安全保存签名密钥。

## 编译

在本目录执行：

```sh
gradle :app:assembleDebug
```

需要 JDK 17、Android SDK 35、Gradle 8.10.2。该目录是一个**独立的 Gradle 项目**，不依赖 UnitedU 源码。
