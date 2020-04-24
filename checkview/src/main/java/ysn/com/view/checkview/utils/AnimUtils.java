package ysn.com.view.checkview.utils;

import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * @Author yangsanning
 * @ClassName AnimUtils
 * @Description 一句话概括作用
 * @Date 2020/4/24
 */
public class AnimUtils {

    public static void ofFloat(long duration, ValueAnimator.AnimatorUpdateListener listener, float... values) {
        ValueAnimator animator = ValueAnimator.ofFloat(values);
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(listener);
        animator.start();
    }
}
