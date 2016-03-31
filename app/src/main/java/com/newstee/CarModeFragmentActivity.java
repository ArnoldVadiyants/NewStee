package com.newstee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CarModeFragmentActivity extends AppCompatActivity {


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.car_mode_layout);

		View header = findViewById(R.id.header);
		ImageView headerTitle =(ImageView) header.findViewById(R.id.car_mode_header_title);
		LinearLayout headerPlayer = (LinearLayout)header.findViewById(R.id.car_mode_header_player);
		TextView headerTitleCanal = (TextView)header.findViewById(R.id.car_mode_header_title_canal);
		TextView headerTitleNews = (TextView)header.findViewById(R.id.car_mode_header_title_news);
		ImageButton headerPlayButton = (ImageButton)header.findViewById(R.id.car_mode_header_play);
		FrameLayout streamTab = (FrameLayout)findViewById(R.id.car_mode_tab_stream);
		FrameLayout playlistTab = (FrameLayout)findViewById(R.id.car_mode_tab_playlist);
		FrameLayout recentTab = (FrameLayout)findViewById(R.id.car_mode_tab_recent);
		FrameLayout recommendTab = (FrameLayout) findViewById(R.id.car_mode_tab_recommend);
		Button exitTab = ( Button) findViewById(R.id.car_mode_tab_exit);
		headerPlayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		streamTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CarModeFragmentActivity.this, CarModeListActivity.class));
			}
		});
		playlistTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CarModeFragmentActivity.this, CarModeListActivity.class));
			}
		});
		recentTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CarModeFragmentActivity.this, CarModeListActivity.class));
			}
		});
		recommendTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CarModeFragmentActivity.this, CarModeListActivity.class));
			}
		});
		exitTab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(CarModeFragmentActivity.this, MainActivity.class));
				finish();
			}
		});
	}
}