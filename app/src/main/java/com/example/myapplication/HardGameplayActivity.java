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

public class HardGameplayActivity extends AppCompatActivity {

    TextView tv_timer, tv_score, tv_round;

    ImageView iv_11, iv_12, iv_13, iv_14,
            iv_21, iv_22, iv_23, iv_24,
            iv_31, iv_32, iv_33, iv_34,
            iv_41, iv_42, iv_43, iv_44,
            iv_51, iv_52, iv_53, iv_54,
            iv_61, iv_62, iv_63, iv_64,
            btnPause, btnHome;

    Integer[] cardArray = {101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112,
                            201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212};

    CountDownTimer countDownTimer;

    Dialog pauseDialog;

    Button btnContinue, btnRestart;

    MediaPlayer gameMusic;

    private SoundPool soundPool;

    int image101, image102, image103, image104, image105, image106, image107, image108, image109, image110, image111, image112,
            image201, image202, image203, image204, image205, image206,image207, image208, image209, image210, image211, image212;

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
        setContentView(R.layout.activity_hard_gameplay);
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

        cardPressedSoundId = soundPool.load(HardGameplayActivity.this, R.raw.card_pressed, 1);
        cardMatchSoundId = soundPool.load(HardGameplayActivity.this, R.raw.match_found, 1);
        cardNotMatchSoundId = soundPool.load(HardGameplayActivity.this, R.raw.match_notfound, 1);

        playerName = getIntent().getStringExtra("playerName");

        gameMusic = MediaPlayer.create(HardGameplayActivity.this,R.raw.gameplay_audio);
        gameMusic.setVolume(2.0f,2.0f);
        gameMusic.setLooping(true);
        gameMusic.start();

        tv_timer = (TextView) findViewById(R.id.tv_timer);
        tv_score = (TextView) findViewById(R.id.tv_score);
        tv_round = (TextView) findViewById(R.id.tv_round);

        btnHome = (ImageView) findViewById(R.id.homebutton);
        btnPause = (ImageView) findViewById(R.id.pause);

        pauseDialog = new Dialog(HardGameplayActivity.this);
        pauseDialog.setContentView(R.layout.pause_dialog);
        pauseDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        pauseDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_bg));
        pauseDialog.setCancelable(false);

        btnContinue = pauseDialog.findViewById(R.id.btn_continue);
        btnRestart = pauseDialog.findViewById(R.id.btn_restart);

        iv_11 = (ImageView) findViewById(R.id.iv_11);
        iv_12 = (ImageView) findViewById(R.id.iv_12);
        iv_13 = (ImageView) findViewById(R.id.iv_13);
        iv_14 = (ImageView) findViewById(R.id.iv_14);

        iv_21 = (ImageView) findViewById(R.id.iv_21);
        iv_22 = (ImageView) findViewById(R.id.iv_22);
        iv_23 = (ImageView) findViewById(R.id.iv_23);
        iv_24 = (ImageView) findViewById(R.id.iv_24);

        iv_31 = (ImageView) findViewById(R.id.iv_31);
        iv_32 = (ImageView) findViewById(R.id.iv_32);
        iv_33 = (ImageView) findViewById(R.id.iv_33);
        iv_34 = (ImageView) findViewById(R.id.iv_34);

        iv_41 = (ImageView) findViewById(R.id.iv_41);
        iv_42 = (ImageView) findViewById(R.id.iv_42);
        iv_43 = (ImageView) findViewById(R.id.iv_43);
        iv_44 = (ImageView) findViewById(R.id.iv_44);

        iv_51 = (ImageView) findViewById(R.id.iv_51);
        iv_52 = (ImageView) findViewById(R.id.iv_52);
        iv_53 = (ImageView) findViewById(R.id.iv_53);
        iv_54 = (ImageView) findViewById(R.id.iv_54);

        iv_61 = (ImageView) findViewById(R.id.iv_61);
        iv_62 = (ImageView) findViewById(R.id.iv_62);
        iv_63 = (ImageView) findViewById(R.id.iv_63);
        iv_64 = (ImageView) findViewById(R.id.iv_64);

        iv_11.setTag(0);
        iv_12.setTag(1);
        iv_13.setTag(2);
        iv_14.setTag(3);
        iv_21.setTag(4);
        iv_22.setTag(5);
        iv_23.setTag(6);
        iv_24.setTag(7);
        iv_31.setTag(8);
        iv_32.setTag(9);
        iv_33.setTag(10);
        iv_34.setTag(11);
        iv_41.setTag(12);
        iv_42.setTag(13);
        iv_43.setTag(14);
        iv_44.setTag(15);
        iv_51.setTag(16);
        iv_52.setTag(17);
        iv_53.setTag(18);
        iv_54.setTag(19);
        iv_61.setTag(20);
        iv_62.setTag(21);
        iv_63.setTag(22);
        iv_64.setTag(23);

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

        iv_13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_13, theCard);
            }
        });

        iv_14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_14, theCard);
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

        iv_23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_23, theCard);
            }
        });

        iv_24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_24, theCard);
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

        iv_33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_33, theCard);
            }
        });

        iv_34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_34, theCard);
            }
        });

        iv_41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_41, theCard);
            }
        });

        iv_42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_42, theCard);
            }
        });

        iv_43.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_43, theCard);
            }
        });

        iv_44.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_44, theCard);
            }
        });

        iv_51.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_51, theCard);
            }
        });

        iv_52.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_52, theCard);
            }
        });

        iv_53.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_53, theCard);
            }
        });

        iv_54.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_54, theCard);
            }
        });

        iv_61.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_61, theCard);
            }
        });

        iv_62.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_62, theCard);
            }
        });

        iv_63.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_63, theCard);
            }
        });

        iv_64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int theCard = (int) v.getTag();
                doStuff(iv_64, theCard);
            }
        });

        startTimer(61000);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(HardGameplayActivity.this, MainActivity.class);
                startActivity(intent);
                countDownTimer.cancel();
                HardGameplayActivity.this.finish();
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
        Intent intent = new Intent(HardGameplayActivity.this, MainActivity.class);
        startActivity(intent);
        countDownTimer.cancel();
        HardGameplayActivity.this.finish();
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
        } else if (cardArray[card] == 107) {
            iv.setImageResource(image107);
        } else if (cardArray[card] == 108) {
            iv.setImageResource(image108);
        } else if (cardArray[card] == 109) {
            iv.setImageResource(image109);
        } else if (cardArray[card] == 110) {
            iv.setImageResource(image110);
        } else if (cardArray[card] == 111) {
            iv.setImageResource(image111);
        } else if (cardArray[card] == 112) {
            iv.setImageResource(image112);
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
        } else if (cardArray[card] == 207) {
            iv.setImageResource(image207);
        } else if (cardArray[card] == 208) {
            iv.setImageResource(image208);
        } else if (cardArray[card] == 209) {
            iv.setImageResource(image209);
        } else if (cardArray[card] == 210) {
            iv.setImageResource(image210);
        } else if (cardArray[card] == 211) {
            iv.setImageResource(image211);
        } else if (cardArray[card] == 212) {
            iv.setImageResource(image212);
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
            iv_13.setEnabled(false);
            iv_14.setEnabled(false);
            iv_21.setEnabled(false);
            iv_22.setEnabled(false);
            iv_23.setEnabled(false);
            iv_24.setEnabled(false);
            iv_31.setEnabled(false);
            iv_32.setEnabled(false);
            iv_33.setEnabled(false);
            iv_34.setEnabled(false);
            iv_41.setEnabled(false);
            iv_42.setEnabled(false);
            iv_43.setEnabled(false);
            iv_44.setEnabled(false);
            iv_51.setEnabled(false);
            iv_52.setEnabled(false);
            iv_53.setEnabled(false);
            iv_54.setEnabled(false);
            iv_61.setEnabled(false);
            iv_62.setEnabled(false);
            iv_63.setEnabled(false);
            iv_64.setEnabled(false);

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
                iv_13.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 3){
                iv_14.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 4){
                iv_21.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 5){
                iv_22.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 6){
                iv_23.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 7){
                iv_24.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 8){
                iv_31.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 9){
                iv_32.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 10){
                iv_33.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 11){
                iv_34.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 12){
                iv_41.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 13){
                iv_42.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 14){
                iv_43.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 15){
                iv_44.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 16){
                iv_51.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 17){
                iv_52.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 18){
                iv_53.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 19){
                iv_54.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 20){
                iv_61.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 21){
                iv_62.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 22){
                iv_63.setVisibility(View.INVISIBLE);
            } else if(clickedFirst == 23){
                iv_64.setVisibility(View.INVISIBLE);
            }

            if(clickedSecond == 0){
                iv_11.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 1){
                iv_12.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 2){
                iv_13.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 3){
                iv_14.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 4){
                iv_21.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 5){
                iv_22.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 6){
                iv_23.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 7){
                iv_24.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 8){
                iv_31.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 9){
                iv_32.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 10){
                iv_33.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 11){
                iv_34.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 12){
                iv_41.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 13){
                iv_42.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 14){
                iv_43.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 15){
                iv_44.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 16){
                iv_51.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 17){
                iv_52.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 18){
                iv_53.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 19){
                iv_54.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 20){
                iv_61.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 21){
                iv_62.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 22){
                iv_63.setVisibility(View.INVISIBLE);
            } else if(clickedSecond == 23){
                iv_64.setVisibility(View.INVISIBLE);
            }

            soundPool.play(cardMatchSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
            playerPoints += 600;
            tv_score.setText("Score: "+playerPoints);

        } else {
            iv_11.setImageResource(R.drawable.card_cover);
            iv_12.setImageResource(R.drawable.card_cover);
            iv_13.setImageResource(R.drawable.card_cover);
            iv_14.setImageResource(R.drawable.card_cover);
            iv_21.setImageResource(R.drawable.card_cover);
            iv_22.setImageResource(R.drawable.card_cover);
            iv_23.setImageResource(R.drawable.card_cover);
            iv_24.setImageResource(R.drawable.card_cover);
            iv_31.setImageResource(R.drawable.card_cover);
            iv_32.setImageResource(R.drawable.card_cover);
            iv_33.setImageResource(R.drawable.card_cover);
            iv_34.setImageResource(R.drawable.card_cover);
            iv_41.setImageResource(R.drawable.card_cover);
            iv_42.setImageResource(R.drawable.card_cover);
            iv_43.setImageResource(R.drawable.card_cover);
            iv_44.setImageResource(R.drawable.card_cover);
            iv_51.setImageResource(R.drawable.card_cover);
            iv_52.setImageResource(R.drawable.card_cover);
            iv_53.setImageResource(R.drawable.card_cover);
            iv_54.setImageResource(R.drawable.card_cover);
            iv_61.setImageResource(R.drawable.card_cover);
            iv_62.setImageResource(R.drawable.card_cover);
            iv_63.setImageResource(R.drawable.card_cover);
            iv_64.setImageResource(R.drawable.card_cover);

            soundPool.play(cardNotMatchSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }

        iv_11.setEnabled(true);
        iv_12.setEnabled(true);
        iv_13.setEnabled(true);
        iv_14.setEnabled(true);
        iv_21.setEnabled(true);
        iv_22.setEnabled(true);
        iv_23.setEnabled(true);
        iv_24.setEnabled(true);
        iv_31.setEnabled(true);
        iv_32.setEnabled(true);
        iv_33.setEnabled(true);
        iv_34.setEnabled(true);
        iv_41.setEnabled(true);
        iv_42.setEnabled(true);
        iv_43.setEnabled(true);
        iv_44.setEnabled(true);
        iv_51.setEnabled(true);
        iv_52.setEnabled(true);
        iv_53.setEnabled(true);
        iv_54.setEnabled(true);
        iv_61.setEnabled(true);
        iv_62.setEnabled(true);
        iv_63.setEnabled(true);
        iv_64.setEnabled(true);

        //check if game is over
        checkEnd();

    }

    private void checkEnd(){
        if (iv_11.getVisibility() == View.INVISIBLE &&
                iv_12.getVisibility() == View.INVISIBLE &&
                iv_13.getVisibility() == View.INVISIBLE &&
                iv_14.getVisibility() == View.INVISIBLE &&
                iv_21.getVisibility() == View.INVISIBLE &&
                iv_22.getVisibility() == View.INVISIBLE &&
                iv_23.getVisibility() == View.INVISIBLE &&
                iv_24.getVisibility() == View.INVISIBLE &&
                iv_31.getVisibility() == View.INVISIBLE &&
                iv_32.getVisibility() == View.INVISIBLE &&
                iv_33.getVisibility() == View.INVISIBLE &&
                iv_34.getVisibility() == View.INVISIBLE &&
                iv_41.getVisibility() == View.INVISIBLE &&
                iv_42.getVisibility() == View.INVISIBLE &&
                iv_43.getVisibility() == View.INVISIBLE &&
                iv_44.getVisibility() == View.INVISIBLE &&
                iv_51.getVisibility() == View.INVISIBLE &&
                iv_52.getVisibility() == View.INVISIBLE &&
                iv_53.getVisibility() == View.INVISIBLE &&
                iv_54.getVisibility() == View.INVISIBLE &&
                iv_61.getVisibility() == View.INVISIBLE &&
                iv_62.getVisibility() == View.INVISIBLE &&
                iv_63.getVisibility() == View.INVISIBLE &&
                iv_64.getVisibility() == View.INVISIBLE){

            gameScore = playerPoints;

            roundNum++;

            tv_round.setText("Round "+roundNum);

            resetGame();

        }
    }

    public void resetGame(){
        iv_11.setImageResource(R.drawable.card_cover);
        iv_12.setImageResource(R.drawable.card_cover);
        iv_13.setImageResource(R.drawable.card_cover);
        iv_14.setImageResource(R.drawable.card_cover);
        iv_21.setImageResource(R.drawable.card_cover);
        iv_22.setImageResource(R.drawable.card_cover);
        iv_23.setImageResource(R.drawable.card_cover);
        iv_24.setImageResource(R.drawable.card_cover);
        iv_31.setImageResource(R.drawable.card_cover);
        iv_32.setImageResource(R.drawable.card_cover);
        iv_33.setImageResource(R.drawable.card_cover);
        iv_34.setImageResource(R.drawable.card_cover);
        iv_41.setImageResource(R.drawable.card_cover);
        iv_42.setImageResource(R.drawable.card_cover);
        iv_43.setImageResource(R.drawable.card_cover);
        iv_44.setImageResource(R.drawable.card_cover);
        iv_51.setImageResource(R.drawable.card_cover);
        iv_52.setImageResource(R.drawable.card_cover);
        iv_53.setImageResource(R.drawable.card_cover);
        iv_54.setImageResource(R.drawable.card_cover);
        iv_61.setImageResource(R.drawable.card_cover);
        iv_62.setImageResource(R.drawable.card_cover);
        iv_63.setImageResource(R.drawable.card_cover);
        iv_64.setImageResource(R.drawable.card_cover);

        iv_11.setVisibility(View.VISIBLE);
        iv_12.setVisibility(View.VISIBLE);
        iv_13.setVisibility(View.VISIBLE);
        iv_14.setVisibility(View.VISIBLE);
        iv_21.setVisibility(View.VISIBLE);
        iv_22.setVisibility(View.VISIBLE);
        iv_23.setVisibility(View.VISIBLE);
        iv_24.setVisibility(View.VISIBLE);
        iv_31.setVisibility(View.VISIBLE);
        iv_32.setVisibility(View.VISIBLE);
        iv_33.setVisibility(View.VISIBLE);
        iv_34.setVisibility(View.VISIBLE);
        iv_41.setVisibility(View.VISIBLE);
        iv_42.setVisibility(View.VISIBLE);
        iv_43.setVisibility(View.VISIBLE);
        iv_44.setVisibility(View.VISIBLE);
        iv_51.setVisibility(View.VISIBLE);
        iv_52.setVisibility(View.VISIBLE);
        iv_53.setVisibility(View.VISIBLE);
        iv_54.setVisibility(View.VISIBLE);
        iv_61.setVisibility(View.VISIBLE);
        iv_62.setVisibility(View.VISIBLE);
        iv_63.setVisibility(View.VISIBLE);
        iv_64.setVisibility(View.VISIBLE);


        Collections.shuffle(Arrays.asList(cardArray));
    }

    private void restartGame() {
        resetGame();
        playerPoints = 0;
        tv_score.setText("Score: "+playerPoints);
        timeRemainingMillis = 61000;
        startTimer(timeRemainingMillis);
        pauseDialog.cancel();
    }

    public void gameOver(){
        Intent intent = new Intent(HardGameplayActivity.this,GameOverScreen.class);
        intent.putExtra("gameScore", gameScore);
        intent.putExtra("playerName", playerName);
        startActivity(intent);
        HardGameplayActivity.this.finish();
    }

    private void frontOfCardsResources(){
        image101 = R.drawable.card_front101;
        image102 = R.drawable.card_front102;
        image103 = R.drawable.card_front103;
        image104 = R.drawable.card_front104;
        image105 = R.drawable.card_front105;
        image106 = R.drawable.card_front106;
        image107 = R.drawable.card_front107;
        image108 = R.drawable.card_front108;
        image109 = R.drawable.card_front109;
        image110 = R.drawable.card_front110;
        image111 = R.drawable.card_front111;
        image112 = R.drawable.card_front112;
        image201 = R.drawable.card_front201;
        image202 = R.drawable.card_front202;
        image203 = R.drawable.card_front203;
        image204 = R.drawable.card_front204;
        image205 = R.drawable.card_front205;
        image206 = R.drawable.card_front206;
        image207 = R.drawable.card_front207;
        image208 = R.drawable.card_front208;
        image209 = R.drawable.card_front209;
        image210 = R.drawable.card_front210;
        image211 = R.drawable.card_front211;
        image212 = R.drawable.card_front212;
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
        if(gameMusic != null){
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