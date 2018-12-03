package com.example.ewigkeit.searching_flat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;


public class BigPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo);
        Intent intent = getIntent();
        ImageView image = (ImageView) findViewById(R.id.big_image) ;
        String url = intent.getStringExtra("photo");
        Glide.with(BigPhotoActivity.this)
                .load(url)
                .into(image);
    }
}
