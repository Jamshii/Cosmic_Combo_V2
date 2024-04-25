package com.example.myapplication;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    MediaPlayer menuMusic;

    ImageView menu, leaderboard, developers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        menuMusic = MediaPlayer.create(MainActivity.this,R.raw.menu_audio);
        menuMusic.setLooping(true);
        menuMusic.start();

        menu = findViewById(R.id.nav_menu);
        leaderboard = findViewById(R.id.nav_leaderboard);
        developers = findViewById(R.id.nav_developers);

        replaceFragment(new MainMenuFragment());
        menu.setImageResource(R.drawable.main_menu_current);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MainMenuFragment());
                menu.setImageResource(R.drawable.main_menu_current);
                leaderboard.setImageResource(R.drawable.leaderboard_btn);
                developers.setImageResource(R.drawable.developers_btn);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LeaderboardFragment());
                leaderboard.setImageResource(R.drawable.leaderboard_current);
                menu.setImageResource(R.drawable.main_menu_button);
                developers.setImageResource(R.drawable.developers_btn);
            }
        });

        developers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new DevelopersFragment());
                developers.setImageResource(R.drawable.developers_pressed);
                menu.setImageResource(R.drawable.main_menu_button);
                leaderboard.setImageResource(R.drawable.leaderboard_btn);
            }
        });


    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Pause or stop background music playback when the activity goes into the background
        if (menuMusic != null && menuMusic.isPlaying()) {
            menuMusic.pause(); // Pause the music
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume background music playback when the activity comes back into the foreground
        if (menuMusic != null && !menuMusic.isPlaying()) {
            menuMusic.start(); // Start or resume the music
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(menuMusic != null){
            menuMusic.release();
            menuMusic = null;
        }
    }
}