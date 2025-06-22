package com.example.musicproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView _listsong;
    RelativeLayout mainview;
    TextView _exit, _help;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Drawable main_background = ContextCompat.getDrawable(this, R.drawable.background_main);
        _listsong = findViewById(R.id.listsong);
        mainview = findViewById(R.id.mainview);
        _exit = findViewById(R.id.next);
        _help = findViewById(R.id.help);
        mainview.setBackground(main_background);
        runtimePermission();
        _exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
        _help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(i1);
            }
        });
    }

    public void runtimePermission(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        // quyen truy cap da duoc cho phep, thuc hien chuong trinh ben trong ham displaySong()
                        displaySong();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        // quyen truy cap bi tu choi
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                        // hien thi ly do va tiep tuc cap quyen truy cap
                    }
                }).check();
    }
    public ArrayList<File> findSong(File file){
        ArrayList<File> arrayList = new ArrayList<File>();
        File[] files = file.listFiles();
        for (File singleFile:files){
            if(singleFile.isDirectory()&& !singleFile.isHidden()){
                arrayList.addAll(findSong(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    arrayList.add(singleFile);
                }
            }
        }
        return arrayList;
    }

    public void displaySong(){
        final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for (int i = 0; i < mySongs.size(); i++){
            items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
        }
        customAdapter customAdapter = new customAdapter();
        _listsong.setAdapter(customAdapter);
        _listsong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String songName = (String) _listsong.getItemAtPosition(position);
                    startActivity(new Intent(MainActivity.this, PlayActivity.class)
                            .putExtra("songs", mySongs)
                            .putExtra("songname", songName)
                            .putExtra("pos", position));
                } catch (RuntimeException e) {
                    Toast.makeText(MainActivity.this, "Runtime error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class customAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View myview = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textsong = myview.findViewById(R.id.songname);
            textsong.setSelected(true);
            textsong.setText(items[position]);
            return myview;
        }
    }
}