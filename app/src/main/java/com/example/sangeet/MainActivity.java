package com.example.sangeet;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView l = findViewById(R.id.list);
        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                        ArrayList<File> songs = search(Environment.getExternalStorageDirectory());
                        String[] names = new String[songs.size()];
                        for(int i=0 ; i<songs.size() ; i++){
                            names[i] = songs.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> ad = new ArrayAdapter<String>(MainActivity.this , android.R.layout.simple_list_item_1 , names);
                        l.setAdapter(ad);
                        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long li) {
                                Intent intent = new Intent(MainActivity.this , playSong.class);
                                String cur = l.getItemAtPosition(i).toString();
                                intent.putExtra("name",cur);
                                intent.putExtra("position",i);
                                intent.putExtra("songlist",songs);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }
    public ArrayList<File> search(File f){
        ArrayList<File> songs = new ArrayList<>();
        File[] files = f.listFiles();
        if(files != null){
            for(File i : files){
                if(!i.isHidden() && i.isDirectory()){
                    songs.addAll(search(i));
                }
                else if(!i.getName().startsWith(".") && i.getName().endsWith(".mp3")){
                    songs.add(i);
                }
            }
        }
        return songs;
    }
}