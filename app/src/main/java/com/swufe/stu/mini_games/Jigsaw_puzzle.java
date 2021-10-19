package com.swufe.stu.mini_games;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Jigsaw_puzzle extends AppCompatActivity {

    ImageButton ib00,ib01,ib02,ib10,ib11,ib12,ib20,ib21,ib22;
    Button restartBtn;
    TextView timeTv;

    //定义计数时间的变量
    int time = 0;
    //存放碎片的数组，便于进行统一的管理
    private int[] image = {R.mipmap.pic_00x00,R.mipmap.pic_00x01,R.mipmap.pic_00x02,
            R.mipmap.pic_01x00,R.mipmap.pic_01x01,R.mipmap.pic_01x02,
            R.mipmap.pic_02x00,R.mipmap.pic_02x01,R.mipmap.pic_02x02};

    //声明一个图片数组的下标数组，随机排列这个数组
    private int[] imageIndex = new int[image.length];

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what==1){
                time++;
                timeTv.setText("时间："+time+"秒");
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw_puzzle);
        initView();
        //打乱碎片的函数
        disruptRandom();
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    //随机打乱数组当中元素，以不规则的形式进行图片显示
    private void disruptRandom() {
        for(int i = 0;i < imageIndex.length;i++){
            imageIndex[i] = i;
        }
        //规定20次，随机选择两个角标对应的值交换
        int rand1,rand2;
        for(int j = 0;j < 20;j++){
            //随机生成角标  生成0-8之间的随机数
            rand1 = (int)(Math.random()*(imageIndex.length-1));
            //第二次随机生成的角标不能和第一次随机生成的角标相同，如果相同就不方便交换了
            do{
                rand2 = (int)(Math.random()*(imageIndex.length-1));
                if(rand1 != rand2){
                    break;
                }
            }while (true);
            //交换两个角标上对应的值
            swap(rand1,rand2);
        }
        //随机排列到指定的控件上
        ib00.setImageResource(image[imageIndex[0]]);
        ib01.setImageResource(image[imageIndex[1]]);
        ib02.setImageResource(image[imageIndex[2]]);
        ib10.setImageResource(image[imageIndex[3]]);
        ib11.setImageResource(image[imageIndex[4]]);
        ib12.setImageResource(image[imageIndex[5]]);
        ib20.setImageResource(image[imageIndex[6]]);
        ib21.setImageResource(image[imageIndex[7]]);
        ib22.setImageResource(image[imageIndex[8]]);
    }

    //交换数组指定角标上的数据
    private void swap(int rand1, int rand2) {
        int temp = imageIndex[rand1];
        imageIndex[rand1] = imageIndex[rand2];
        imageIndex[rand2] = temp;
    }

    //初始化控件
    private void initView(){
        ib00 = findViewById(R.id.pt_ib_00x00);
        ib01 = findViewById(R.id.pt_ib_00x01);
        ib02 = findViewById(R.id.pt_ib_00x02);
        ib10 = findViewById(R.id.pt_ib_01x00);
        ib11 = findViewById(R.id.pt_ib_01x01);
        ib12 = findViewById(R.id.pt_ib_01x02);
        ib20 = findViewById(R.id.pt_ib_02x00);
        ib21 = findViewById(R.id.pt_ib_02x01);
        ib22 = findViewById(R.id.pt_ib_02x02);
        timeTv = findViewById(R.id.pt_tv_time);
        restartBtn = findViewById(R.id.pt_btn_restart);
    }

    public void onClick(View view) {

    }

    //重新开始按钮的点击事件
    public void restart(View view) {
        //将拼图重新打乱

        handler.removeMessages(1);
        //将时间重新归零，并且重新开始计时
        time = 0;
        timeTv.setText("时间："+time+"秒");
        handler.sendEmptyMessageDelayed(1,1000);
    }
}