package com.kamil.mediaplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MusicLibrary extends Activity {

    private TextView txtView;

    private DbAdapter adapterrr;
    private Cursor songCursor;
    private List<SongModel> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_library);
        initListView();

        txtView = (TextView) findViewById(R.id.libraryTextView);
    }


    public void createLibrary(View view){

        txtView.setText("OOOOOOO");

        getSongList();
      //  adapterrr.insertSong("Titul","AUthor","Sdasd","sdfsdfdsf",234234234,324234234);

    }


    private void initListView() {
        begin();

    }

    private void begin(){
        adapterrr = new DbAdapter(getApplicationContext());
        adapterrr.open();
        getAllSongs();

    }

    private void getAllSongs() {
        songs = new ArrayList<SongModel>();
        songCursor = getAllEntriesFromDb();
        updateSongList();
    }

    private Cursor getAllEntriesFromDb() {
        songCursor = adapterrr.getAllSongs();
        if(songCursor != null) {
            startManagingCursor(songCursor);
            songCursor.moveToFirst();
        }
        return songCursor;
    }

    private void updateSongList() {
        if(songCursor != null && songCursor.moveToFirst()) {
            do{
                long id = songCursor.getLong(DbAdapter.ID_COLUMN);
                String title = songCursor.getString(DbAdapter.TITLE_COLUMN);
                String author = songCursor.getString(DbAdapter.AUTHOR_COLUMN);
                String album = songCursor.getString(DbAdapter.ALBUM_COLUMN);
                String albumpath = songCursor.getString(DbAdapter.ALBUMPATH_COLUMN);
                long length = songCursor.getLong(DbAdapter.LENGTH_COLUMN);
                long songid = songCursor.getLong(DbAdapter.SONGID_COLUMN);
                songs.add(new SongModel(id, title, author, album, albumpath, length, songid));
            } while(songCursor.moveToNext());
        }
    }

    @Override
    protected void onDestroy() {
        if(adapterrr != null)
            adapterrr.close();
        super.onDestroy();
    }

    public void getSongList() {

        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);


        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns

            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumID = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int lengthColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);
            //add songs to list
            do {

                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                long thisLength = musicCursor.getLong(lengthColumn);

                long thisAlbumId = musicCursor.getLong(albumID);

                String cover = SongAdapter.getCoverArtPath(thisAlbumId,this);

                adapterrr.insertSong(thisTitle,thisArtist,thisAlbum,cover,thisLength,thisId);

            }
            while (musicCursor.moveToNext());
        }
        txtView.setText("Tworzenie biblioteki zakonczone");
    }

}
