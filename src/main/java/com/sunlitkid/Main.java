package com.sunlitkid;


import com.sunlitkid.txt.Constant;
import com.sunlitkid.txt.MemoDialog;

import java.util.Arrays;

/**
 * Created by sunke on 2017/11/24.
 */
public class Main {
    public static void main(String[] args) {
        MemoDialog dialog = new MemoDialog(500,400);
        dialog.setAlwaysOnTop(true);
        new Thread(() -> {
            while (true){
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    break;
                }
                dialog. hideDialog();
                //如果窗口已经退出  就关闭程序
                if(Constant.isExit){
                    break;
                }
            }
        }).start();
    }
}
