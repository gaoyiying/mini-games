package com.swufe.stu.mini_games;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    Button btn_pt,btn_bd,btn_2048;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView welcome_gif = (ImageView) findViewById(R.id.first);
        Glide.with(this).load(R.drawable.first).into(welcome_gif);

        btn_pt=findViewById(R.id.btn_pt);
        btn_pt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Jigsaw_puzzle.class);
                startActivity(intent);
            }
        });
        btn_bd=findViewById(R.id.btn_bd);
        btn_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BirdGame.class);
                startActivity(intent);
            }
        });
        btn_2048=findViewById(R.id.btn_2048);
        btn_2048.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Game2048.class);
                startActivity(intent);
            }
        });

    }
}