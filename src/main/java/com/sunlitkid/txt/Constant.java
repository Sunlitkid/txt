package com.sunlitkid.txt;

import java.awt.*;

/**
 * Created by sunke on 2017/11/24.
 */
public class Constant {
    public static boolean isExit = false;//是否退出了 退出以后要关闭循环
    public static double screenHeight ;
    public static double screenWidth ;
    static {
        Dimension screenSize   =   Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.getWidth();
        screenHeight = screenSize.getHeight();
    }
}
