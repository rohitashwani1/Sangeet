package com.example.sangeet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;

public class playSong extends AppCompatActivity {
    private TextView t;
    private SeekBar seek;
    private FloatingActionButton b;
    private FloatingActionButton b2;
    private FloatingActionButton b3;
    private MediaPlayer m;
    int pos;
    Thread updseek;
    @Override
    protected void onDestroy(){
        super.onDestroy();
        m.stop();
        m.release();
        updseek.interrupt();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song2);
        t = findViewById(R.id.textView);
        seek = findViewById(R.id.seekBar2);
        b = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);
        b3 = findViewById(R.id.b3);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ArrayList<File> songs = (ArrayList)(bundle.getParcelableArrayList("songlist"));
        t.setText(bundle.getString("name"));
        pos = bundle.getInt("position");
        Uri uri = Uri.parse(songs.get(pos).toString());
        m = MediaPlayer.create(this,uri);
        m.start();
        seek.setMax(m.getDuration());
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                m.seekTo(seekBar.getProgress());
            }
        });
        updseek = new Thread(){
            @Override
            public void run() {
                int currentPosition = 0;
                try{
                    while(currentPosition<m.getDuration()){
                        currentPosition = m.getCurrentPosition();
                        seek.setProgress(currentPosition);
                        sleep(80);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updseek.start();

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(m.isPlaying()){
                    m.pause();
                    b.setImageDrawable(ContextCompat.getDrawable(playSong.this , android.R.drawable.ic_media_play));
                }
                else{
                    m.start();
                    b.setImageDrawable(ContextCompat.getDrawable(playSong.this , android.R.drawable.ic_media_pause));
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.stop();
                m.release();
                if(pos == 0){
                    pos = songs.size() - 1;
                }
                else{
                    pos--;
                }
                t.setText(songs.get(pos).getName().toString());
                Uri uri = Uri.parse(songs.get(pos).toString());
                m = MediaPlayer.create(playSong.this,uri);
                m.start();
                updseek = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition = 0;
                        try{
                            while(currentPosition<m.getDuration()){
                                currentPosition = m.getCurrentPosition();
                                seek.setProgress(currentPosition);
                                sleep(80);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                updseek.start();
                seek.setMax(m.getDuration());
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.stop();
                m.release();
                if(pos == songs.size() - 1){
                    pos = 0;
                }
                else{
                    pos++;
                }
                t.setText(songs.get(pos).getName().toString());
                Uri uri = Uri.parse(songs.get(pos).toString());
                m = MediaPlayer.create(playSong.this,uri);
                m.start();
                updseek = new Thread(){
                    @Override
                    public void run() {
                        int currentPosition = 0;
                        try{
                            while(currentPosition<m.getDuration()){
                                currentPosition = m.getCurrentPosition();
                                seek.setProgress(currentPosition);
                                sleep(80);
                            }
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                updseek.start();
                seek.setMax(m.getDuration());
            }
        });
    }
}