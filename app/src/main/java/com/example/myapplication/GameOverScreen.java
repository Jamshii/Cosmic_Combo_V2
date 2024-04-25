package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class GameOverScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    View diffView;

    int gameScore, imgResult;

    String playerName, strScore;

    MediaPlayer goodJobMusic, excellentMusic, gameOverMusic;

    TextView tv_GameScore, tv_Player;

    ImageView results, btnReplay, btnMainMenu, btnEasy, btnMedium, btnHard, btnXDiff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game_over_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);

        excellentMusic = MediaPlayer.create(GameOverScreen.this,R.raw.excellent_audio);
        goodJobMusic = MediaPlayer.create(GameOverScreen.this,R.raw.goodjob_audio);
        gameOverMusic = MediaPlayer.create(GameOverScreen.this,R.raw.gameover_audio);

        diffView = (View) findViewById(R.id.difficulty);

        tv_GameScore = (TextView) findViewById(R.id.tv_GameScore);
        tv_Player = (TextView) findViewById(R.id.tv_Player);

        results = (ImageView) findViewById(R.id.results);

        gameScore = getIntent().getIntExtra("gameScore",0);
        playerName = getIntent().getStringExtra("playerName");

        tv_GameScore.setText("Game Score: "+gameScore);

        if(gameScore>=7200){
            imgResult = R.drawable.excellent_sign;
            excellentMusic.setVolume(1.0f,1.0f);
            excellentMusic.start();
            tv_Player.setText("Congratulations "+playerName);
        }else if(gameScore > 600){
            imgResult = R.drawable.good_job_sign;
            goodJobMusic.setVolume(1.0f,1.0f);
            goodJobMusic.start();
            tv_Player.setText("Great work! "+playerName);
        }else{
            imgResult = R.drawable.game_over_sign;
            gameOverMusic.setVolume(1.0f,1.0f);
            gameOverMusic.start();
            tv_Player.setText("Tough Luck! "+playerName);
        }

        results.setImageResource(imgResult);

        btnReplay = (ImageView) findViewById(R.id.btnReplay);
        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDifficulty();
            }
        });

        btnMainMenu = (ImageView) findViewById(R.id.btnMenu);
        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GameOverScreen.this,MainActivity.class);
                startActivity(intent);
                GameOverScreen.this.finish();
            }
        });

        btnEasy = (ImageView) findViewById(R.id.btnEasy);
        btnEasy.setClickable(false);
        btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newEasyGame();
            }
        });

        btnMedium = (ImageView) findViewById(R.id.btnMedium);
        btnMedium.setClickable(false);
        btnMedium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMediumGame();
            }
        });

        btnHard = (ImageView) findViewById(R.id.btnHard);
        btnHard.setClickable(false);
        btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newHardGame();
            }
        });

        btnXDiff = (ImageView) findViewById(R.id.btn_x_difficulty);
        btnXDiff.setClickable(false);
        btnXDiff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDiff();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(GameOverScreen.this, MainActivity.class);
                startActivity(intent);
                GameOverScreen.this.finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        strScore = String.valueOf(gameScore);

        saveEntry(playerName, strScore);
    }

    private void saveEntry(String name, String number) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, number);
        editor.apply();
    }

    private void openDifficulty() {
        diffView.setVisibility(View.VISIBLE);
        btnReplay.setClickable(false);
        btnMainMenu.setClickable(false);
        btnEasy.setClickable(true);
        btnMedium.setClickable(true);
        btnHard.setClickable(true);
        btnXDiff.setClickable(true);
    }

    private void newEasyGame(){
        Intent intent = new Intent(GameOverScreen.this, EasyGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        GameOverScreen.this.finish();
    }

    private void newMediumGame(){
        Intent intent = new Intent(GameOverScreen.this, MediumGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        GameOverScreen.this.finish();
    }

    private void newHardGame(){
        Intent intent = new Intent(GameOverScreen.this, HardGameplayActivity.class);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        GameOverScreen.this.finish();
    }

    private void exitDiff() {
        diffView.setVisibility(View.INVISIBLE);
        btnReplay.setClickable(true);
        btnMainMenu.setClickable(true);
        btnEasy.setClickable(false);
        btnMedium.setClickable(false);
        btnHard.setClickable(false);
        btnXDiff.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(excellentMusic != null){
            excellentMusic.release();
            excellentMusic = null;
        } else if(goodJobMusic != null){
            goodJobMusic.release();
            goodJobMusic = null;
        } else if(gameOverMusic != null) {
            gameOverMusic.release();
            gameOverMusic = null;
        }
    }
}