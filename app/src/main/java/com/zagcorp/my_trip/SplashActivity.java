package com.zagcorp.my_trip;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logo = findViewById(R.id.logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation flip = AnimationUtils.loadAnimation(this, R.anim.flip);
        Animation moveUp = AnimationUtils.loadAnimation(this, R.anim.move_up);

        logo.setVisibility(View.VISIBLE);
        logo.startAnimation(fadeIn);

        // Após a animação fadeIn, comece a animação flip
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(flip);
            }
        }, fadeIn.getDuration());

        // Após a animação flip, comece a animação moveUp
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                logo.startAnimation(moveUp);
            }
        }, fadeIn.getDuration() + flip.getDuration());

        // Após todas as animações, inicie a MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
            }
        }, fadeIn.getDuration() + flip.getDuration() + moveUp.getDuration());
    }
}
