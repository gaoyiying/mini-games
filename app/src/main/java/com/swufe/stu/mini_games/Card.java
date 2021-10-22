package com.swufe.stu.mini_games;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class Card extends FrameLayout {
    //首先该卡片类是一个FrameLayout控件类，即在该类里可以嵌入其他控件类例如文本控件什么的，所以当该类一实现了以后就会初始化文本控件，而通过构造函数里的初始化而同时初始化了文本控件的属性
    //构造函数：初始化
    public Card(Context context) {
        super(context);
        //初始化文本
        label = new TextView(getContext());
        //设置文本的大小
        label.setTextSize(32);

        //设置控件card的背景颜色
        label.setBackgroundColor(0x33ffffff);
        //把放在控件里的文本居中处理
        label.setGravity(Gravity.CENTER);

        //布局参数用来控制
        LayoutParams lp = new LayoutParams(-1, -1);//该类用来初始化layout控件textView里的高和宽属性
        //给每个textView的左和上设置margin，右和下就不需要了
        lp.setMargins(10, 10, 0, 0);
        addView(label, lp);//该方法是用来给label控件加上已经初始化了的高和宽属性
        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        //呈现

        //由于我们需要在这个设置数字的方法导入随机数字，而为了排除出现0，我们需要在原方法里加上判断语句
        if (num <= 0) {
            label.setText("");
        } else {
            if (num <= 4) {
                label.setTextColor(0xff000000);
            } else {
                label.setTextColor(0xffffffff);
            }
            label.setText(num + "");
        }
//      label.setText(num+"");
    }

    private int num = 0;

    //需要呈现文字
    private TextView label;

    //判断两张卡片数字是否一样？
    public boolean equals(Card o) {
        return getNum() == o.getNum();
    }

}