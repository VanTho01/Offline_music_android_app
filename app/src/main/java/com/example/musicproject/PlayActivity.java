package com.example.musicproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlayActivity extends AppCompatActivity {
    TextView _btnplay,_btnytb, _btnzing, _btnpre, _btnnext, _pre_scereen, _btnagain, _exit;
    TextView _status;
    TextView _songname, _start, _stop, _ff10, _bw10;
    SeekBar seekmusic;
    Handler handler = new Handler();
    Runnable update;
    String name;
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<File> mySongs;
    LinearLayout _playscreen;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        Drawable background = ContextCompat.getDrawable(this, R.drawable.background_play);
        Drawable youtube = ContextCompat.getDrawable(this, R.drawable.img);
        Drawable zingmp3 = ContextCompat.getDrawable(this, R.drawable.img_2);
        _playscreen = findViewById(R.id.playscreen);
        _playscreen.setBackground(background);
        _btnytb = findViewById(R.id.linktoyoutube);
        _btnzing = findViewById(R.id.linktozingmp3);
        _btnplay = findViewById(R.id.btnplay);
        _btnpre = findViewById(R.id.btnprev);
        _btnnext = findViewById(R.id.btnnext);
        _pre_scereen = findViewById(R.id.prescreen);
        _btnagain = findViewById(R.id.btnagain);
        _exit = findViewById(R.id.exit);
        _ff10 = findViewById(R.id.btnff10);
        _bw10 = findViewById(R.id.btnbw10);

        _status = findViewById(R.id.status);

        _btnytb.setBackground(youtube);
        _btnzing.setBackground(zingmp3);

        _songname = findViewById(R.id.songname);
        _start = findViewById(R.id.txtstart);
        _stop = findViewById(R.id.txtstop);
        seekmusic = findViewById(R.id.seekbar);


        if (mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        try {
            Intent i1 = getIntent();
            mySongs = (ArrayList) i1.getExtras().getParcelableArrayList("songs");
            position = i1.getIntExtra("pos", 0);
            _songname.setSelected(true);
            Uri uri = Uri.parse(mySongs.get(position).toString());
            name = mySongs.get(position).getName();

            _songname.setText(name);

            mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
            mediaPlayer.start();

            //dat gia tri max cho seek bar la do dai cua file audio
            seekmusic.setMax(mediaPlayer.getDuration());
            _stop.setText(formatTime(mediaPlayer.getDuration()));
            updateSeekbar();
            updateThread();
            _btnnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                        mediaPlayer.stop();
                        position++;
                        Uri uri = Uri.parse(mySongs.get(position).toString());
                        name = mySongs.get(position).getName();
                        _songname.setText(name);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        updateSeekbar();
                        updateThread();
                    } catch (RuntimeException e){
                        _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                        Toast.makeText(PlayActivity.this, "Bai hat khong ton tai!", Toast.LENGTH_SHORT).show();
                        mediaPlayer.release();
                        position--;
                        Uri uri = Uri.parse(mySongs.get(position).toString());
                        name = mySongs.get(position).getName();
                        _songname.setText(name);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        updateSeekbar();
                        updateThread();
                    }
                }
            });
            _btnpre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                        mediaPlayer.stop();
                        position--;
                        Uri uri = Uri.parse(mySongs.get(position).toString());
                        name = mySongs.get(position).getName();
                        _songname.setText(name);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        updateSeekbar();
                        updateThread();
                    } catch (RuntimeException e) {
                        _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                        Toast.makeText(PlayActivity.this, "Bai hat khong ton tai!", Toast.LENGTH_SHORT).show();
                        mediaPlayer.release();
                        position++;
                        Uri uri = Uri.parse(mySongs.get(position).toString());
                        name = mySongs.get(position).getName();
                        _songname.setText(name);
                        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                        mediaPlayer.start();
                        updateSeekbar();
                        updateThread();
                    }
                }
            });

            _btnplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()) {
                        _btnplay.setBackgroundResource(R.drawable.baseline_play_circle_filled_24);
                        _songname.setSelected(false);
                        mediaPlayer.pause();
                    } else {
                        _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                        mediaPlayer.start();
                        _songname.setSelected(true);
                        updateSeekbar();
                    }
                }
            });
            _ff10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.seekTo(Math.min(mediaPlayer.getCurrentPosition()+10000, mediaPlayer.getDuration()));
                    }
                }
            });
            _bw10.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mediaPlayer.isPlaying()){
                        mediaPlayer.seekTo(Math.max(mediaPlayer.getCurrentPosition()-10000, 0));
                    }
                }
            });
            _pre_scereen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i3 = new Intent(PlayActivity.this, MainActivity.class);
                    startActivity(i3);
                }
            });
            _btnytb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _btnplay.setBackgroundResource(R.drawable.baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                    String[] keyword = name.split(".mp3");
                    Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=" + keyword[0]));
                    startActivity(i2);
                }
            });
            _btnzing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    _btnplay.setBackgroundResource(R.drawable.baseline_play_circle_filled_24);
                    mediaPlayer.pause();
                    String[] keyword = name.split(".mp3");
                    Intent i4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://zingmp3.vn/tim-kiem/tat-ca?q=" + keyword[0]));
                    startActivity(i4);
                }
            });
            seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser){
                        mediaPlayer.seekTo(progress);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
            _btnagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(i == 0){
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_241);
                        mediaPlayer.setLooping(true);
                        i = 1;
                    } else {
                        _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                        mediaPlayer.setLooping(false);
                        i = 0;
                    }
                }
            });
            _exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mediaPlayer.stop();
                    finishAffinity();
                }
            });
        } catch (RuntimeException e) {
            Toast.makeText(this, "Runtime Error", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateSeekbar(){
        _stop.setText(formatTime(mediaPlayer.getDuration()));
        seekmusic.setMax(mediaPlayer.getDuration());
        seekmusic.setProgress(Math.min(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
        _start.setText(formatTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()) {
            update = this::updateSeekbar;
            handler.postDelayed(update, 1000);
        }
    }
    private String formatTime(int miliseconds){
        int minute = (int) (miliseconds/1000)/60;
        int second = (int) (miliseconds/1000)%60;
        return String.format("%02d:%02d", minute, second);
    }
    private void updateThread(){
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                    mediaPlayer.release();
                    _btnplay.setBackgroundResource(R.drawable.baseline_play_circle_filled_24);
                    position++;
                    Uri uri = Uri.parse(mySongs.get(position).toString());
                    name = mySongs.get(position).getName();
                    _songname.setText(name);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                    mediaPlayer.start();
                    updateSeekbar();
                    updateThread();
                }catch (RuntimeException e) {
                    _btnagain.setBackgroundResource(R.drawable.baseline_cached_24);
                    mediaPlayer.release();
                    _btnplay.setBackgroundResource(R.drawable.baseline_play_circle_filled_24);
                    position = 0;
                    Uri uri = Uri.parse(mySongs.get(position).toString());
                    name = mySongs.get(position).getName();
                    _songname.setText(name);
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                    _btnplay.setBackgroundResource(R.drawable.baseline_pause_circle_24);
                    mediaPlayer.start();
                    updateSeekbar();
                    updateThread();
                }
            }
        });
    }
}