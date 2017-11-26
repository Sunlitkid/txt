package com.sunlitkid.txt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by sunke on 2017/11/24.
 */
public class MemoDialog extends JDialog {
    //窗口属性
    private  boolean isMouseIn = false;//鼠标在里面吗
    private  boolean isHidden = false;//是否隐藏了
    private  boolean canDrag = false;//是否可以拖拽
    private Point offset;
    private JTextArea textArea;
    private int ll = 5; //侧边宽度
    public MemoDialog(int width,int height){
        this.setSize(width, height);
        this.setLocationRelativeTo(null);// 把窗体设置在屏幕中间
        ImageIcon icon = new ImageIcon("sun.png");
        this.setIconImage(icon.getImage());
        systemTray(); // 设置系统托盘
        // 添加关闭按钮事件，关闭时候实质是把窗体隐藏
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                MemoDialog.this.setVisible(false);
            }
        });
        initTextArea();
        setUndecorated(true);
        this.setVisible(true);
    }

    private void initTextArea(){
        //new 一个文本框
        this.textArea = new JTextArea();
        textArea.setTabSize(4);
        textArea.setFont(new Font("宋体", Font.PLAIN, 14));
        textArea.setLineWrap(true);// 激活自动换行功能
        textArea.setWrapStyleWord(true);// 激活断行不断字功能
        textArea.setBackground(new Color(0xffffe4));
        textArea.setDisabledTextColor(Color.BLACK);
        textArea.addMouseListener(new MyMouseListener());
        textArea.addMouseMotionListener(new MyMouseMotionListener());
        JScrollPane jScrollPane = new JScrollPane(textArea);
        textArea.setLineWrap(true);        //激活自动换行功能
        textArea.setWrapStyleWord(true);            // 激活断行不断字功能
        textArea.setMargin(new Insets(ll,ll,ll,ll));
        Container container = this.getContentPane();//获取容器
        container.setLayout(new BorderLayout());
        container.add(jScrollPane, BorderLayout.CENTER);
    }

    private void systemTray() {
        if (SystemTray.isSupported()) { // 判断系统是否支持托盘功能.
            // 创建托盘右击弹出菜单
            PopupMenu popupMenu = new PopupMenu();
            //创建弹出菜单中的退出项
            MenuItem itemExit = new MenuItem("exit");
            itemExit.addActionListener(e -> System.exit(0));
            popupMenu.add(itemExit);
            //创建托盘图标
            ImageIcon icon = new ImageIcon("sun_32px.png"); // 创建图片对象
            TrayIcon trayIcon = new TrayIcon(icon.getImage(), "memo",
                    popupMenu);
            trayIcon.addActionListener(e -> {
                if(MemoDialog.this.isVisible()){
                    MemoDialog.this.setVisible(false);
                }else{
                    MemoDialog.this.setVisible(true);
                }
            });
            //把托盘图标添加到系统托盘
            //这个可以点击关闭之后再放到托盘里面，在此是打开程序直接显示托盘图标了
            try {
                SystemTray.getSystemTray().add(trayIcon);
            } catch (AWTException e1) {
                e1.printStackTrace();
            }
        }
    }

    public  void hideDialog(){
        if(this.isMouseIn ||this.isHidden){//如果鼠标在里面或者已经隐藏  就跳过
            return;
        }
        int top = this.getY();
        int bottom = (int)(Constant.screenHeight - this.getHeight() - this.getY());
        int left = this.getX();
        int right = (int)(Constant.screenWidth - this.getWidth() - this.getX());

        int[] arr = {top,bottom,left,right};

        int min =arr[0];
        for(int i = 1 ; i< arr.length;i++){
            if(arr[i]<min){
                min = arr[i];
            }
        }
        //int newLength = 11;//变小后的长度和宽度
        if(min<=50){
            if(min == left){
                this.setLocation(ll-this.getWidth(),this.getY());
            }else if(min == top){
                this.setLocation(this.getX(),ll-this.getHeight());
            }else if(min == right){
                this.setLocation((int)(Constant.screenWidth-ll),this.getY());
            }else{//(min == bottom)
                this.setLocation(this.getX(),(int)(Constant.screenHeight-ll));
            }
            this.textArea.setEnabled(false);
            this.isHidden =true;
        }else{
            //DO NOTHING
        }
    }

    class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() >=2){
                if(MemoDialog.this.canDrag){
                    MemoDialog.this.canDrag = false;
                    textArea.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                    textArea.setEnabled(true);
                }else{
                    MemoDialog.this.canDrag = true;
                    textArea.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.DEFAULT_CURSOR));
                    textArea.setEnabled(false);
                }
            }
        }
        @Override
        public void mousePressed(MouseEvent e) {
            offset = e.getPoint();
        }
        @Override
        public void mouseReleased(MouseEvent e) {

        }
        @Override
        public void mouseEntered(MouseEvent e) {
            MemoDialog.this.isMouseIn =true;
            if(MemoDialog.this.isHidden){//如果窗口是小窗口
                //恢复
                int top = MemoDialog.this.getY();
                int bottom = (int)(Constant.screenHeight - MemoDialog.this.getHeight() - MemoDialog.this.getY());
                int left = MemoDialog.this.getX();
                int right = (int)(Constant.screenWidth - MemoDialog.this.getWidth() - MemoDialog.this.getX());

                int x = MemoDialog.this.getX() ;
                int y = MemoDialog.this.getY() ;
                if(top<0){
                    y=0;
                }
                if(left<0){
                    x= 0 ;
                }

                if(bottom<0 ){
                    y = (int)Constant.screenHeight- MemoDialog.this.getHeight();
                }

                if(right<0){
                    x  = (int)Constant.screenWidth - MemoDialog.this.getWidth();
                }
                MemoDialog.this.setLocation(x,y);
                MemoDialog.this.isHidden =false;

                //设置成不可拖拽的编辑模式
                MemoDialog.this.canDrag = false;
                textArea.setEnabled(true);
                textArea.setCursor(java.awt.Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
            }
        }
        @Override
        public void mouseExited(MouseEvent e) {
            MemoDialog.this.isMouseIn =false;
        }
    }

    class MyMouseMotionListener implements  MouseMotionListener{
        public void mouseDragged(MouseEvent e) {
            if(MemoDialog.this.canDrag){
                final int x = MemoDialog.this.getX();
                final int y = MemoDialog.this.getY();
                final Point lastAt = e.getPoint();
                MemoDialog.this.setLocation(x + lastAt.x - offset.x, y + lastAt.y - offset.y);
            }
        }
        public void mouseMoved(MouseEvent e) {

        }
    }


}
