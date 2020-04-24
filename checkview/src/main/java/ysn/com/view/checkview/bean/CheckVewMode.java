package ysn.com.view.checkview.bean;

/**
 * @Author yangsanning
 * @ClassName CheckVewMode
 * @Description CheckVew模式
 * @Date 2020/4/24
 */
public enum CheckVewMode {

    /**
     * 圆形
     */
    CIRCLE(0),

    /**
     * 正方行
     */
    SQUARE(1),
    ;

    public int mode;

    CheckVewMode(int mode) {
        this.mode = mode;
    }

    public boolean isCircle(int mode) {
        return this.mode == mode;
    }
}
