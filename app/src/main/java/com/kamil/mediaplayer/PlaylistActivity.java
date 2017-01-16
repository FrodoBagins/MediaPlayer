package com.kamil.mediaplayer;

import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Logger;

import android.net.Uri;
import android.content.ContentResolver;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.ListView;
import android.os.IBinder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.view.MenuItem;
import android.view.View;
import com.kamil.mediaplayer.MusicService.MusicBinder;
import android.widget.MediaController.MediaPlayerControl;

public class PlaylistActivity extends AppCompatActivity implements MediaPlayerControl{

    public final static String SONG_NUMBER = "NUMBER";


    private DbPlaylistAdapter todoDbAdapter;

    private Cursor todoCursor;
    private SongAdapter listAdapter;
    private ArrayList<Song> tasks;


    private ArrayList<Song> songList;
    private ListView songView;
    private static MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;
    private MusicController controller;
    private boolean paused=false, playbackPaused=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        songView = (ListView)findViewById(R.id.song_list2);
        songList = new ArrayList<Song>();

        getSongList();

        Collections.sort(tasks, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdt = new SongAdapter(this, tasks);
        songView.setAdapter(songAdt);
        setController();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    private void setController(){
        controller = new MusicController(this);
        controller.setPrevNextListeners(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });

        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.song_list2));
        controller.setEnabled(true);

    }

    //play next
    private void playNext(){
        musicSrv.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        musicSrv.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    public void songPicked(View view){
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();

        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);

      //  Intent intent = new Intent(this, PlayingSong.class);

      //  int ooomg = Integer.parseInt(view.getTag().toString());

      //  Song sonk = tasks.get(ooomg);

      //  intent.putExtra(SONG_NUMBER, sonk.getSongId());

      //  startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv=null;
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shuffle:
                musicSrv.setShuffle();
                break;
            case R.id.action_return:

                Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                startActivity(intent);

                break;
            case R.id.action_end:
                stopService(playIntent);
                musicSrv=null;
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            //pass list
            musicSrv.setList(tasks);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public void getSongList() {

        initListView();

    }

    private void initListView() {
        fillListViewData();

    }

    private void fillListViewData() {
        todoDbAdapter = new DbPlaylistAdapter(getApplicationContext());
        todoDbAdapter.open();


        getAllSongs();
        listAdapter = new SongAdapter(this, tasks);
        songView.setAdapter(listAdapter);

    }

    private void getAllSongs() {

        tasks = new ArrayList<Song>();
        todoCursor = getAllEntriesFromDb();
        updateSongList();

    }

    private Cursor getAllEntriesFromDb() {
        todoCursor = todoDbAdapter.getAllSongs();
        if(todoCursor != null) {
            startManagingCursor(todoCursor);
            todoCursor.moveToFirst();
        }
        return todoCursor;
    }

    private void updateSongList() {
        if(todoCursor != null && todoCursor.moveToFirst()) {
            do {
                long id = todoCursor.getLong(DbPlaylistAdapter.SONGID_COLUMN);
                String title = todoCursor.getString(DbPlaylistAdapter.TITLE_COLUMN);
                String author = todoCursor.getString(DbPlaylistAdapter.AUTHOR_COLUMN);
                String cover = todoCursor.getString(DbPlaylistAdapter.ALBUMPATH_COLUMN);
                int songid = todoCursor.getInt(DbPlaylistAdapter.ID_COLUMN);

                tasks.add(new Song(id,title,author,cover,songid));
            } while(todoCursor.moveToNext());
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }

    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }

    @Override
    public void start() {
        musicSrv.go();
    }

    @Override
    public void pause() {
        playbackPaused=true;
        musicSrv.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(musicSrv!=null && musicBound && musicSrv.isPng())
            return musicSrv.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        musicSrv.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(musicSrv!=null && musicBound)
            return musicSrv.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
