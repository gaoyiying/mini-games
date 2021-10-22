package com.swufe.stu.mini_games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Game2048 extends AppCompatActivity {

    private TextView tvScore;
    private int score = 0;

    //提供一个单例设计模式给别的类去调用该类中的处理分数的方法
    private static Game2048 game2048 = null;

    public Game2048() {
        game2048 = this;
    }

    public static Game2048 getGame2048() {
        return game2048;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game2048);
        tvScore = (TextView) findViewById(R.id.tvScore);
        findViewById(R.id.btn_2048_go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Game2048.this, MainActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.btn_restart_2048).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameView_2048.getGameView_2048().startGame();
            }
        });
    }

    //分数清零
    public void clearScore() {
        score = 0;
        showScore();
    }

    //在控件上显示分数
    public void showScore() {
        tvScore.setText(score + "");
    }

    //使用方法添加分数，并显示出来
    public void addScore(int s) {
        score += s;
        showScore();

    }

}