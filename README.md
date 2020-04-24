# CheckView
[![](https://jitpack.io/v/yangsanning/CheckView.svg)](https://jitpack.io/#yangsanning/CheckView)
[![API](https://img.shields.io/badge/API-19%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=19)

## 效果预览

| [CheckView]                     |
| ------------------------------- | 
| <img src="images/checkview.gif" height="512" /> |

## 主要文件
| 名字             | 摘要           |
| ---------------- | -------------- |
| [CheckView] | 打勾View  |


### 1. 基本用法

#### 1.1 布局中添加
```android
<ysn.com.view.checkview.CheckView
    android:layout_width="32dp"
    android:layout_height="32dp"
    android:layout_marginStart="20dp"
    ysn:cv_mode="square" />
```

#### 1.1  切换状态
``` android
checkView.toggle(true);
```


### 2. 配置属性([Attributes])

|name|format|description|
|:---:|:---:|:---:|
| cv_enable_bg | boolean | 是否启用背景色 |
| cv_bg_normal_color | color | 未选中背景色 |
| cv_bg_checked_color | color | 选中背景色 |
| cv_enable_border | boolean | 是否启用边框 |
| cv_border_normal_color | color | 未选中边框颜色 |
| cv_border_checked_color | color | 选中边框颜色 |
| cv_tick_color | color | 选中勾的颜色 |
| cv_stroke_width | dimension | 描边的宽度、打勾的宽度 |
| cv_duration | integer | 动画时长 |
| cv_corner | dimension | 矩形圆角度数 |
| cv_mode | enum | circle(圆)、square(正方形) |


### 3.添加方法

#### 1. 添加仓库

在项目的 `build.gradle` 文件中配置仓库地址。

```android
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

#### 2. 添加项目依赖

在需要添加依赖的 Module 下添加以下信息，使用方式和普通的远程仓库一样。

```android
implementation 'com.github.yangsanning:CheckView:1.0.0'
```

[CheckView]:https://github.com/yangsanning/CheckView/blob/master/checkview/src/main/java/ysn/com/view/checkview/CheckView.java
[Attributes]:https://github.com/yangsanning/CheckView/blob/master/checkview/src/main/res/values/attrs.xml
