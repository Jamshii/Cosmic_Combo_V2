package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Collections;

public class EasyGameplayActivity extends AppCompatActivity {

    TextView tv_timer, tv_score, tv_round;

    ImageView iv_11, iv_12, iv_21, iv_22, iv_31, iv_32, btnPause, btnHome;

    Integer[] cardArray = {101, 102, 103, 201, 202, 203};

    CountDownTimer countDownTimer;

    Dialog pauseDialog;

    Button btnContinue, btnRestart;

    MediaPlayer gameMusic;

    private SoundPool soundPool;

    int image101, image102, image103, image104, image105, image106,
            image201, image202, image203, image204, image205, image206;

    int firstCard, secondCard;

    int clickedFirst, clickedSecond;

    int cardNumber = 1;

    int playerPoints = 0, gameScore;

    int cardPressedSoundId, cardMatchSoundId, cardNotMatchSoundId;

    byte roundNum = 1;

    long timeRemainingMillis;

    String playerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_easy_gameplay);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // For Android Lollipop (API 21) and higher
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(10) // Set the maximum number of simultaneous streams
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            // For devices running versions lower than Android Lollipop
            soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        }

        cardPressedSoundId = soundPool.load(EasyGameplayActivity.this, R.raw.card_pressed, 1);
        cardMatchSoundId = soundPool.load(EasyGameplayActivity.this, R.raw.match_found, 1);
        cardNotMatchSoundId = soundPool.load(EasyGameplayActivity.this, R.raw.match_notfound, 1);

        playerName = getIntent().getStringExtra("playerName");

        gameMusic = MediaPlayer.create(EasyGameplayActivity.this,R.raw.gameplay_audio);
        gameMusic.setVolume(2.0f,2.0f);
        gameMusic.setLooping(true);
        gameMusic.start();

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_round = (TextView) findViewById(R.id.tv_round);

        btnHome = (ImageView) findViewById(R.id.homebutton);
        btnPause = (ImageView) findViewById(R.id.pause);

        pauseDialog = new Dialog(EasyGameplayActivity.this);
        pauseDialog.setContentView(R.layout.pause_dialog);
        pauseDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pauseDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        pauseDialog.setCancelable(false);

        btnContinue = pauseDialog.findViewById(R.id.btn_continue);
        btnRestart = pauseDialog.findViewById(R.id.btn_restart);


        iv_11 = (ImageView) findViewById(R.id.iv_11);
        iv_12 = (ImageView) findViewById(R.id.iv_12);

        iv_21 = (ImageView) findViewById(R.id.iv_21);
        iv_22 = (ImageView) findViewById(R.id.iv_22);

        iv_31 = (ImageView) findViewById(R.id.iv_31);
        iv_32 = (ImageView) findViewById(R.id.iv_32);

        iv_11.setTag(0);
        iv_12.setTag(1);
        iv_21.setTag(2);
        iv_22.setTag(3);
        iv_31.setTag(4);
        iv_32.setTag(5);


        //load the card images
        frontOfCardsResources();

        //shuffle the images
        Collections.shuffle(Arrays.asList(cardArray));

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseGame();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitGame();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueGame();
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        iv_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_11, theCard);
            }
        });

        iv_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_12, theCard);
            }
        });


        iv_21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_21, theCard);
            }
        });

        iv_22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_22, theCard);
            }
        });


        iv_31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_31, theCard);
            }
        });

        iv_32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_32, theCard);
            }
        });


        startTimer(61000);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(EasyGameplayActivity.this, MainActivity.class);
                startActivity(intent);
                countDownTimer.cancel();
                EasyGameplayActivity.this.finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void pauseGame() {
        pauseDialog.show();
        countDownTimer.cancel(); // Pause the timer
    }

    private void continueGame(){
        pauseDialog.cancel();
        startTimer(timeRemainingMillis);
    }

    private void exitGame(){
        Intent intent = new Intent(EasyGameplayActivity.this, MainActivity.class);
        startActivity(intent);
        countDownTimer.cancel();
        EasyGameplayActivity.this.finish();
    }

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Store the remaining time in the class-level variable
                timeRemainingMillis = millisUntilFinished;
                updateTimerText(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                // Handle timer finish event, if needed
                gameScore = playerPoints;
                gameOver();
            }
        };

        // Start the countdown timer
        countDownTimer.start();
    }

    private void updateTimerText(long millisUntilFinished) {
        // Update the TextView with the remaining time
        tv_timer.setText("Timer: " + String.valueOf(millisUntilFinished / 1000));
    }

    private void doStuff(ImageView iv, int card) {
        soundPool.play(cardPressedSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        //set to the correct image view
        if (cardArray[card] == 101) {
            iv.setImageResource(image101);
        } else if (cardArray[card] == 102) {
            iv.setImageResource(image102);
        } else if (cardArray[card] == 103) {
            iv.setImageResource(image103);
        } else if (cardArray[card] == 104) {
            iv.setImageResource(image104);
        } else if (cardArray[card] == 105) {
            iv.setImageResource(image105);
        } else if (cardArray[card] == 106) {
            iv.setImageResource(image106);
        } else if (cardArray[card] == 201) {
            iv.setImageResource(image201);
        } else if (cardArray[card] == 202) {
            iv.setImageResource(image202);
        } else if (cardArray[card] == 203) {
            iv.setImageResource(image203);
        } else if (cardArray[card] == 204) {
            iv.setImageResource(image204);
        } else if (cardArray[card] == 205) {
            iv.setImageResource(image205);
        } else if (cardArray[card] == 206) {
            iv.setImageResource(image206);
        }

        //check which image is selected and save it to temp variable
        if (cardNumber == 1) {
            firstCard = cardArray[card];
            if (firstCard > 200) {
                firstCard -= 100;
            }
            cardNumber = 2;
            clickedFirst = card;

            iv.setEnabled(false);
        } else if (cardNumber == 2) {
            secondCard = cardArray[card];
            if (secondCard > 200) {
                secondCard -= 100;
            }
            cardNumber = 1;
            clickedSecond = card;

            iv.setEnabled(false);

            iv_11.setEnabled(false);
            iv_12.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //check if the selected images are equal
                    calculate();
                }
            }, 500);
        }

    }

    private void calculate(){
        if (firstCard == secondCard){
            if(clickedFirst == 0){
                iv_11.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 1){
                iv_12.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 2){
                iv_21.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 3){
                iv_22.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 4){
                iv_31.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 5){
                iv_32.setVisibility(View.INVISIBLE);
            }

            if(clickedSecond == 0){
                iv_11.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 1){
                iv_12.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 2){
                iv_21.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 3){
                iv_22.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 4){
                iv_31.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 5){
                iv_32.setVisibility(View.INVISIBLE);
            }

            soundPool.play(cardMatchSoundId, 1.0f, 1.0f, 1, 0, 1.0f);

            playerPoints += 200;
            tv_score.setText("Score: "+playerPoints);

        } else {
            iv_11.setImageResource(R.drawable.card_cover);
            iv_12.setImageResource(R.drawable.card_cover);
            iv_21.setImageResource(R.drawable.card_cover);
            iv_22.setImageResource(R.drawable.card_cover);
            iv_31.setImageResource(R.drawable.card_cover);
            iv_32.setImageResource(R.drawable.card_cover);

            soundPool.play(cardNotMatchSoundId, 1.0f, 1.0f, 1, 0, 1.0f);

        }

        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);


        //check if game is over
        checkEnd();

    }

    private void checkEnd(){
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE ){

            gameScore = playerPoints;

            roundNum++;

            tv_round.setText("Round "+roundNum);

            resetGame();

        }
    }

    public void resetGame(){
        iv_11.setImageResource(R.drawable.card_cover);
        iv_12.setImageResource(R.drawable.card_cover);
        iv_21.setImageResource(R.drawable.card_cover);
        iv_22.setImageResource(R.drawable.card_cover);
        iv_31.setImageResource(R.drawable.card_cover);
        iv_32.setImageResource(R.drawable.card_cover);

        iv_11.setVisibility(View.VISIBLE);
        iv_12.setVisibility(View.VISIBLE);
        iv_21.setVisibility(View.VISIBLE);
        iv_22.setVisibility(View.VISIBLE);
        iv_31.setVisibility(View.VISIBLE);
        iv_32.setVisibility(View.VISIBLE);

        Collections.shuffle(Arrays.asList(cardArray));
    }

    public void restartGame(){
        resetGame();
        playerPoints = 0;
        tv_score.setText("Score: "+playerPoints);
        timeRemainingMillis = 61000;
        startTimer(timeRemainingMillis);
        pauseDialog.cancel();
    }

    public void gameOver(){
        Intent intent = new Intent(EasyGameplayActivity.this,GameOverScreen.class);
        intent.putExtra("gameScore", gameScore);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        EasyGameplayActivity.this.finish();
    }

    private void frontOfCardsResources(){
        image101 = R.drawable.card_front101;
        image102 = R.drawable.card_front102;
        image103 = R.drawable.card_front103;
        image104 = R.drawable.card_front104;
        image105 = R.drawable.card_front105;
        image106 = R.drawable.card_front106;
        image201 = R.drawable.card_front201;
        image202 = R.drawable.card_front202;
        image203 = R.drawable.card_front203;
        image204 = R.drawable.card_front204;
        image205 = R.drawable.card_front205;
        image206 = R.drawable.card_front206;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause or stop background music playback when the activity goes into the background
        if (gameMusic != null && gameMusic.isPlaying()) {
            gameMusic.pause(); // Pause the music
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume background music playback when the activity comes back into the foreground
        if (gameMusic != null && !gameMusic.isPlaying()) {
            gameMusic.start(); // Start or resume the music
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(gameMusic != null) {
            gameMusic.release();
            gameMusic = null;
        }

        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        countDownTimer.cancel();
    }


}