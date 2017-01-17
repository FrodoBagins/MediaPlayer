package com.kamil.mediaplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    private DbPlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void viewAllSong(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void viewPlaylist(View view) {
        Intent intent = new Intent(this, PlaylistActivity.class);
        startActivity(intent);
    }

    public void resetPlayList(View view) {
       getApplicationContext().deleteDatabase("database2.db");
        adapter = new DbPlaylistAdapter(getApplicationContext());
        adapter.open();
        adapter.close();
    }

    public void viewLibrary(View view) {
        Intent intent = new Intent(this, MusicLibrary.class);
        startActivity(intent);
    }

}
