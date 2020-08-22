package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mprev, mplay, mnext;
    MediaPlayer mediaPlayer;
    private SeekBar seekbar;
    private TextView lefttime, righttime;
    private ImageView artistimage;
    private  Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUPUI();
seekbar.setMax(mediaPlayer.getDuration());
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
if(fromuser)
{

    mediaPlayer.seekTo(progress);
}
                SimpleDateFormat dateformate=new SimpleDateFormat("mm:ss");
int Currentpos=mediaPlayer.getCurrentPosition();
int Duration=mediaPlayer.getDuration();
lefttime.setText(dateformate.format(new Date(Currentpos)));
                righttime.setText(dateformate.format(new Date(Duration-Currentpos)));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void setUPUI() {


        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.jaanam);

        mprev = findViewById(R.id.btnprev);
        mplay = findViewById(R.id.btnplay);
        mnext = findViewById(R.id.btnnext);
        lefttime = findViewById(R.id.LeftTime);
        righttime = findViewById(R.id.RightTime);
        seekbar = findViewById(R.id.seekBar);
        artistimage = findViewById(R.id.imageView2);

        mprev.setOnClickListener(this);
        mnext.setOnClickListener(this);
        mplay.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnprev:
                backmusic();
                break;

            case R.id.btnnext:
                Forwardmusic();
                break;
            case R.id.btnplay:
                if (mediaPlayer.isPlaying()) {
                    musicpause();
                } else {
                    musicplay();
                }

                break;

        }

    }

    public void musicpause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mplay.setBackgroundResource(android.R.drawable.ic_media_play);

        }


    }
    public void backmusic(){
if(mediaPlayer.isPlaying())
{
    mediaPlayer.seekTo(0);
}

    }
    public void Forwardmusic(){
        if(mediaPlayer.isPlaying())
        {
            mediaPlayer.seekTo(mediaPlayer.getDuration()- 10);
        }

    }

    public void musicplay() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
            updateThread();
            mplay.setBackgroundResource(android.R.drawable.ic_media_pause);

        }
    }
    ////code for ka seebar song ka sath chala or necha jo time wo be update hota raha
    public  void updateThread() {

        thread = new Thread() {
            @Override
            public void run() {
                while (mediaPlayer !=null && mediaPlayer.isPlaying()) {
                    try {
                        thread.sleep(50);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                int newposition=mediaPlayer.getCurrentPosition();
                                int newmax=mediaPlayer.getDuration();
                                seekbar.setMax(newmax);
                                seekbar.setProgress(newposition);
                                ////update text
                                lefttime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getCurrentPosition()))));
                                righttime.setText(String.valueOf(new java.text.SimpleDateFormat("mm:ss").format(new Date(mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition()))));
                            }

                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        };
        thread.start();
        /////////////////////////////////////////////////


    }
    protected void onDestroy() {
        if(mediaPlayer != null && mediaPlayer.isPlaying())
        {

            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        thread.interrupt();
        thread=null;

        super.onDestroy();
    }
}
