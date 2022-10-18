package com.example.aapkadhunadmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play,previous,next;
    ImageView album_art;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    MediaMetadataRetriever mdr;
    byte[] art;
    String textContent;
    int position;
    Thread updateSeek;
    SeekBar seekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song2);
        textView  = findViewById(R.id.textView);
        previous  = findViewById(R.id.previous);
        play      = findViewById(R.id.play);
        next      = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);
        album_art = findViewById(R.id.imageView);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        songs = (ArrayList) bundle.getParcelableArrayList("songList");
        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(songs.get(position).toString());
        mdr= new MediaMetadataRetriever();
        mdr.setDataSource(songs.get(position).getPath());

        try {
            art = mdr.getEmbeddedPicture();
            Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
            album_art.setImageBitmap(songImage);

        } catch (Exception e) {

            album_art.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.headp));

        }

        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        updateSeek = new Thread(){
            @Override
            public void run(){
                int currentPosition = 0;
                try {
                    while (currentPosition<mediaPlayer.getDuration()){
                        currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        sleep(250);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        updateSeek.start();
        play.setOnClickListener(v -> {
            if(mediaPlayer.isPlaying()){
                play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                mediaPlayer.pause();
            }else {
                play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
                mediaPlayer.start();
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0) {
                    position = position-1;
                }
                else {
                    position = songs.size()-1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                play.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
                mdr= new MediaMetadataRetriever();
                mdr.setDataSource(songs.get(position).getPath());

                try {
                    art = mdr.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    album_art.setImageBitmap(songImage);

                } catch (Exception e) {
                    album_art.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(),R.drawable.headp));

                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                Uri uri1 = Uri.parse(songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri1);
                mediaPlayer.start();
                play.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

                seekBar.setMax(mediaPlayer.getDuration());
                seekBar.setProgress(0);
                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);
                mdr = new MediaMetadataRetriever();
                mdr.setDataSource(songs.get(position).getPath());

                try {
                    art = mdr.getEmbeddedPicture();
                    Bitmap songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                    album_art.setImageBitmap(songImage);

                } catch (Exception e) {
                    album_art.setImageDrawable(AppCompatResources.getDrawable(getApplicationContext(),R.drawable.headp));

                }
            }
        });


}}