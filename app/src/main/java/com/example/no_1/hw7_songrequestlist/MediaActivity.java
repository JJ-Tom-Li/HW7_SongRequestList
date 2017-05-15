package com.example.no_1.hw7_songrequestlist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaActivity extends AppCompatActivity {

    /*Declaration*/
    ImageView ivShow;
    Button btnHome,btnCover,btnLyric;
    ImageButton btnPrev,btnNext,btnPlay;
    int songnumber=0;
    List<String> songs = new ArrayList<String>();
    MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
    MediaPlayer mp= new MediaPlayer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media);

        /*find view*/
        findView();
        /*set button listener*/
        btnPrev.setOnClickListener(imageDoClick);
        btnNext.setOnClickListener(imageDoClick);
        btnPlay.setOnClickListener(imageDoClick);
        btnHome.setOnClickListener(doClick);
        btnCover.setOnClickListener(doClick);
        btnLyric.setOnClickListener(doClick);


        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        songs=bundle.getStringArrayList("songs");
        songnumber = bundle.getInt("SongNumber",1);//get song number
        Toast.makeText(MediaActivity.this,"播放:"+songs.get(songnumber),Toast.LENGTH_LONG)
                .show();
        requestStoragePermission();
        try {
            setMetadataRetriver(songnumber);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void findView()
    {
        ivShow = (ImageView)findViewById(R.id.imageViewShow);
        btnPrev = (ImageButton)findViewById(R.id.imageButtonPrev);
        btnNext = (ImageButton)findViewById(R.id.imageButtonNext);
        btnPlay = (ImageButton)findViewById(R.id.imageButtonPlay);
        btnHome = (Button)findViewById(R.id.buttonHome);
        btnCover = (Button)findViewById(R.id.buttonCover);
        btnLyric = (Button)findViewById(R.id.buttonLyric);

    }

    private ImageButton.OnClickListener imageDoClick = new ImageButton.OnClickListener() {
        /*ImageButton listener*/
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageButtonPrev:
                    /*switch to previous song*/
                    mp.stop(); //first stop the music
                    if(songnumber==0)
                        songnumber=songs.size(); //avoid overflow
                    songnumber--;
                    try {
                        setMetadataRetriver(songnumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.imageButtonNext:
                    /*switch to next song*/
                    mp.stop(); //first stop the music
                    if(songnumber==songs.size()-1)
                        songnumber=-1;
                    songnumber++;
                    try {
                        setMetadataRetriver(songnumber);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.imageButtonPlay:
                    /*play the song*/
                    mp.start();
                    break;
            }
        }
    };
    private Button.OnClickListener doClick = new Button.OnClickListener(){
        /*Button listener*/
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonHome:
                    /*go back to MainActivity and finish this activity*/
                    /*Intent intent = new Intent();
                    intent.setClass(MediaActivity.this,MainActivity.class);
                    startActivity(intent);*/
                    mp.stop();
                    finish();
                    break;
                case R.id.buttonCover:
                    /*show the cover*/
                    showCover();
                    break;
                case R.id.buttonLyric:
                    /*show the lyric*/
                    showLyric(songnumber);
                    break;
            }
        }
    };

    public void setMetadataRetriver(int index) throws IOException {
        /*set MetaDataRetriver to get the album picture and the song name.
        * And set the MediaPlayer to play music.*/
       // AssetFileDescriptor afd = this.getAsse
        metaRetriever.setDataSource(Environment.getExternalStorageDirectory().getPath().toString()+"/"+songs.get(index));
        ivShow.setImageDrawable(Drawable.createFromPath
                (metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)));

       /* Toast.makeText(MediaActivity.this,Environment.getExternalStorageDirectory().getPath().toString()+"/"+songs.get(index),Toast.LENGTH_LONG)
                .show();*/
        Toast.makeText(MediaActivity.this,"播放:"+songs.get(songnumber),Toast.LENGTH_LONG)
                .show();
        mp=new MediaPlayer();
        mp.setDataSource(Environment.getExternalStorageDirectory().getPath().toString()+"/"+songs.get(index));
        mp.prepare();
        showCover();
    }

    public void showCover()
    {
        byte [] data = metaRetriever.getEmbeddedPicture();

        // convert the byte array to a bitmap
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            ivShow.setImageBitmap(bitmap); //associated cover art in bitmap
        }
        else
        {
            ivShow.setImageResource(R.drawable.nocover);
        }
    }

    public void showLyric(int index)
    {
        ivShow.setImageResource(R.drawable.nolyric);
    }

    private void requestStoragePermission()
    {
        if(Build.VERSION.SDK_INT>=23)
        {
            /*android 6.0 以上*/
            int hasPermission = checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if(hasPermission!= PackageManager.PERMISSION_GRANTED)
            {
                /*no permission*/
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                return;
            }
        }
    }
}
