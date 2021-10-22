package com.swufe.stu.mini_games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class BirdGame extends AppCompatActivity {

    GameView gameView;
    int Score = 0;
    Button btn_bird_go;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bird_game);
        btn_bird_go=findViewById(R.id.bird_go);
        btn_bird_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BirdGame.this, MainActivity.class);
                startActivity(intent);
            }
        });
        gameView = findViewById(R.id.bird_view);

        /*
        gameView.setGameListener(new GameView.GameListener() {
            @Override
            public void addScore(final int score) {
                runOnUiThread(new Runnable() {//切换至UI线程
                    @Override
                    public void run() {
                        Score = score;
                        ((TextView)findViewById(R.id.score)).setText(""+score);
                    }
                });
            }

            @Override
            public void gameOver() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.scoreTxt)).setText("分数："+Score);
                        findViewById(R.id.relative).setVisibility(View.VISIBLE);
                    }
                });
            }

            @Override
            public void gameReady() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.relative).setVisibility(View.GONE);
                    }
                });
            }
        });

         */
        findViewById(R.id.btn_bird).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新开始
                //findViewById(R.id.relative).setVisibility(View.GONE);
                gameView.reSet();
                /*
                Score = 0;
                ((TextView)findViewById(R.id.score)).setText(""+0);
               
                 */
            }
        });
    }
}