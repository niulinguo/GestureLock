# GestureLock

手势密码

## 导入

``` gradle
allprojects {
    repositories {
        ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

``` gradle
dependencies {
        implementation 'com.github.niulinguo:GestureLock:1.0'
}
```

## 布局

``` xml
<com.niles.gesture_lock.GestureLockView
    android:id="@+id/gesture_lock_view"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="32dp"
    />
```

## 可复写的颜色属性

``` xml
<resources>

    <!--边框色-->
    <color name="nlg_gesture_lock_border">#555</color>
    <!--空白色-->
    <color name="nlg_gesture_lock_empty">#fff</color>
    <!--主色调-->
    <color name="nlg_gesture_lock_primary">#008577</color>
    <!--错误色调-->
    <color name="nlg_gesture_lock_error">#d44</color>
    <color name="nlg_gesture_lock_error_trans">#3d44</color>

    <!--外圆边框颜色（正常、选择、出错）-->
    <color name="nlg_gesture_lock_circle_border">@color/nlg_gesture_lock_border</color>
    <color name="nlg_gesture_lock_circle_border_selected">@color/nlg_gesture_lock_primary</color>
    <color name="nlg_gesture_lock_circle_border_error">@color/nlg_gesture_lock_error</color>

    <!--外圆填充颜色（正常、选择、出错）-->
    <color name="nlg_gesture_lock_circle">@color/nlg_gesture_lock_empty</color>
    <color name="nlg_gesture_lock_circle_selected">@color/nlg_gesture_lock_empty</color>
    <color name="nlg_gesture_lock_circle_error">@color/nlg_gesture_lock_error_trans</color>

    <!--内圆颜色（正常、选择、出错）-->
    <color name="nlg_gesture_lock_circle_center">@color/nlg_gesture_lock_empty</color>
    <color name="nlg_gesture_lock_circle_center_selected">@color/nlg_gesture_lock_primary</color>
    <color name="nlg_gesture_lock_circle_center_error">@color/nlg_gesture_lock_error</color>

    <!--线颜色（正常、出错）-->
    <color name="nlg_gesture_lock_line">@color/nlg_gesture_lock_primary</color>
    <color name="nlg_gesture_lock_line_error">@color/nlg_gesture_lock_error</color>
    
</resources>
```

### 可复写的尺寸属性

``` xml
<resources>

    <!--外圆半径-->
    <dimen name="nlg_gesture_lock_circle_radius">30dp</dimen>
    <!--外圆边框宽度-->
    <dimen name="nlg_gesture_lock_circle_border_size">1dp</dimen>
    <!--线宽度-->
    <dimen name="nlg_gesture_lock_line_size">2dp</dimen>

</resources>
```

### 自适应尺寸

如果layout_width设置为match_parent或者固定宽度，则内部的布局为自适应。
如果layout_width设置为wrap_content，则内部的布局根据dimens.xml中定义的布局大小绘制。

