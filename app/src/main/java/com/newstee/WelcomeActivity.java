package com.newstee;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * Created by Arnold on 03.03.2016.
 */
public class WelcomeActivity extends Activity {
    private ImageButton btnFacebookReg;
    private ImageButton btnTwitterReg;
    private ImageButton btnGoogleReg;
    private ImageButton btnVkReg;
    private Button btnRegistration;
    private Button btnResume;
    private LinearLayout layoutSignIn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        btnFacebookReg = (ImageButton) findViewById(R.id.facebook_imageButton);
        btnTwitterReg = (ImageButton) findViewById(R.id.twitter_imageButton);
        btnVkReg = (ImageButton) findViewById(R.id.vk_imageButton);
        btnGoogleReg = (ImageButton) findViewById(R.id.google_imageButton);
        btnRegistration = (Button)findViewById(R.id.registration_via_email_button);
        layoutSignIn = (LinearLayout)findViewById(R.id.sign_in_layout);
        btnResume = (Button)findViewById(R.id.resume_button);
        btnFacebookReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnGoogleReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnVkReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnTwitterReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        RegisterActivity.class);
                startActivity(intent);
          //      finish();

            }
        });
        layoutSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        LoginActivity.class);
                startActivity(intent);
           //     finish();
            }
        });
        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WelcomeActivity.this,
                MainActivity.class);
        startActivity(intent);
        finish();
    }
}
