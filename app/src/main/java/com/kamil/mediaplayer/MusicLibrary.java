package com.kamil.mediaplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MusicLibrary extends AppCompatActivity {

    private TextView txtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_library);


        txtView = (TextView) findViewById(R.id.libraryTextView);
    }


    public void createLibrary(View view){

        txtView.setText("OOOOOOO");

    }

}
