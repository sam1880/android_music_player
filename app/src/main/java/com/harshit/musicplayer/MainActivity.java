package com.harshit.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView)findViewById(R.id.listView);
        runtimePermission();

    }
    public void runtimePermission()
    {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
            displaySong();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

         public ArrayList<File> findSong(File file)
         {
             ArrayList<File> arrayList = new ArrayList<>();
             File[] files = file.listFiles();

             for (File singleFile : files)
             {
                 if (singleFile.isDirectory() && !singleFile.isHidden())
                 {
                     arrayList.addAll(findSong(singleFile));
                 }
                 else
                     {
                     if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav"))
                     {
                         arrayList.add(singleFile);
                     }
                 }
             }
             return arrayList;
         }

         public void displaySong()
         {
             final ArrayList<File> mySongs = findSong(Environment.getExternalStorageDirectory());
             items = new String[mySongs.size()];
             for(int i= 0; i<mySongs.size(); i++)
             {
                 items[i] = mySongs.get(i).getName().toString().replace(".mp3", "").replace(".wav", "");
             }

             customAdapter customAdapter = new customAdapter();
             listView.setAdapter(customAdapter);
         }

         class customAdapter extends BaseAdapter
         {

             @Override
             public int getCount() {
                 return items.length;
             }

             @Override
             public Object getItem(int i) {
                 return null;
             }

             @Override
             public long getItemId(int i) {
                 return 0;
             }

             @Override
             public View getView(int i, View counterView, ViewGroup parent) {
                 View view = getLayoutInflater().inflate(R.layout.list_item, null);
                 TextView txtSong = view.findViewById(R.id.txtSong);
                 txtSong.setSelected(true);
                 txtSong.setText(items[i]);
                 return view;
             }
         }

}