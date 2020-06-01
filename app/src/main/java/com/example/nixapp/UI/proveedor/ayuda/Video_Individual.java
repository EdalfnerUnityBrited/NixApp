package com.example.nixapp.UI.proveedor.ayuda;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nixapp.R;

public class Video_Individual extends AppCompatActivity {
    String liga,titulo;
    VideoView videoView;
    TextView titulos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__individual);

        liga=(String) getIntent().getSerializableExtra("VideoLiga");
        titulo=(String) getIntent().getSerializableExtra("Titulo");
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Tutorial:");
        mToolbar.setNavigationIcon(R.drawable.ic_backarrow);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Uri uri= Uri.parse(liga);
        videoView = findViewById(R.id.videoView);
        titulos = findViewById(R.id.titulo);
        titulos.setText(titulo);
        videoView.setVideoURI(Uri.parse(liga));
        videoView.start();
        MediaController mc=  new MediaController(this);
        mc.setAnchorView(videoView);
        videoView.setMediaController(mc);
        videoView.stopPlayback();
        videoView.requestFocus();
    }
}
