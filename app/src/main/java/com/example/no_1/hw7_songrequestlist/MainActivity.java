package com.example.no_1.hw7_songrequestlist;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    /*Declaration*/
    ListView lvSongList;
    List<String> songs = new ArrayList<String>();
    private static final String MEDIA_PATH = Environment.getExternalStorageDirectory().toString();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*get the permission to access sd card*/
        requestStoragePermission();
        /*find view*/
        findView();
        /*create song list*/
        updateSongList();

        /*List view click listener*/
        lvSongList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("songs",(ArrayList)songs);
                bundle.putInt("SongNumber",position);
                intent.putExtras(bundle);
                intent.setClass(MainActivity.this,MediaActivity.class);
                startActivity(intent);
            }
        });
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

    public void findView()
    {
        lvSongList = (ListView)findViewById(R.id.ListViewSongList);
    }

    public void updateSongList() {
        File home = new File(Environment.getExternalStorageDirectory().getPath()+"/");
        if (home.listFiles( new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles( new FileExtensionFilter())) {
                songs.add(file.getName());
            }

            ArrayAdapter<String> songList = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,songs);
            lvSongList.setAdapter(songList);

        }
    }

    public class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
