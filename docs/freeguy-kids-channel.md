# 小米儿童频道兼容性（MiTV-MFTR0 / freeguy）

## 目标

女儿原来使用小米原厂桌面顶部「儿童」栏目，不能把「儿童模式」或独立儿童专题页当成同一个入口。

## 已确认的原厂接口

2026-10-21 在用户实际固件 OS2.0.5.0.RFTMATV / Android 11 上，以保留小米桌面的方式通过 ADB 实测：

```sh
adb shell am start -W -a com.mitv.tvhome.HOME_PAGE \
  -n com.mitv.tvhome/.MainActivity --es page '儿童'
```

实际出现小米原厂顶部「儿童」栏目，内容包括动画推荐及继续观看，底部原始应用内容和账号均保持原样。这不涉及 `KidsMultipleTabsActivity`（专题页）或 `ENTER_KIDS_MODE`（儿童模式）。

`MainActivity` 是 `MainActivityUserMode` 的导出 activity-alias；该固件中 `onNewIntent` 对 `com.mitv.tvhome.HOME_PAGE` 读取 `page` 参数并调用 `moveToPageByTitle`。

## UnitedU 兼容性实现

- 仅 MiTV-MFTR0/freeguy 显示「小米儿童频道」应用型快捷卡片。
- 直接调用原厂 `com.mitv.tvhome.HOME_PAGE`，extra `page=儿童`，绝不重新实现推荐、播放、收藏或家长控制。
- 默认将卡片放在 `movie` 影视应用行，紧挨 `com.xiaodianshi.tv.yst`（哔哩哔哩），不创建单独的儿童行。首次引导只添加已存在的应用卡片。
- 从此前测试版升级时，若存在**准确匹配旧版的独立儿童行**，将该虚拟卡片迁移至哔哩哔哩所在行，或 movie 行，不覆盖其余布局与排序。已自行整理好的卡片不会被重排。
- 保留 `com.mitv.tvhome`、儿童相关应用数据、账号、观看记录、收藏、家长控制与儿童锁。
- 不冻结、卸载原厂桌面，也不自动接管 Home。虚拟卡片在菜单中禁止卸载。
- 当前发布的 `freeguy-smoke-20261021` 旧版 APK **仍然是错误的儿童专题页入口**，只能作为历史基础桌面测试，不作为已修复版本。

## 未完成的验收

上述正确入口已通过 **ADB Shell 调用**验证，但尚未从安装的新版 UnitedU 卡片自身点击验证第三方应用权限与冷启动路径。

1. 编译并签名新版测试 APK，再安装到电视，保留官方默认 HOME。
2. 从 UnitedU 真实点击「小米儿童频道」卡片，确认进入顶部儿童栏目而非专题页或儿童模式。
3. 核对观看记录、推荐、遥控器返回/退出及重启后再次进入的行为。
4. 仅所有验收通过后，才能考虑启用 UnitedU 的 Home 接管。
