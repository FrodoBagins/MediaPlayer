package com.kamil.mediaplayer;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PlayingSong extends AppCompatActivity {

    private DbAdapter todoDbAdapter2;
    private DbPlaylistAdapter todoDbAdapter;

    private ImageView cover;

    private Button addplaylist;
    private Button returnbutton;

    private TextView author;
    private TextView album;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_song);

        Intent intent = getIntent();
        int songid = intent.getIntExtra(MainActivity.SONG_NUMBER, 0);

        todoDbAdapter2 = new DbAdapter(getApplicationContext());
        todoDbAdapter2.open();

        final SongModel song = todoDbAdapter2.getSong(songid);

        author = (TextView) findViewById(R.id.artist_label);
        album = (TextView) findViewById(R.id.album_label);
        title = (TextView) findViewById(R.id.song_label);
        addplaylist = (Button) findViewById(R.id.addplaylist);
        returnbutton = (Button) findViewById(R.id.returnbutton);

        addplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoDbAdapter = new DbPlaylistAdapter(getApplicationContext());
                todoDbAdapter.open();
                todoDbAdapter.insertSong(song.getTitle(),song.getAuthor(),song.getAlbum(),song.getAlbumpath(),song.getLength(),song.getSongid());
                todoDbAdapter.close();

            }
        });

        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        author.setText(song.getAuthor());
        album.setText(song.getAlbum());
        title.setText(song.getTitle());

        cover = (ImageView) findViewById(R.id.album_cover);

        Context context = cover.getContext();

        if(!(cover==null)){
            Picasso.with(context).load(new File(song.getAlbumpath())).resize(200,200).placeholder(R.drawable.note).into(cover);
        }

    }

}
