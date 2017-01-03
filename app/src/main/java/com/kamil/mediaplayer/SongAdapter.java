package com.kamil.mediaplayer;

import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Kamil_2 on 2016-12-17.
 */

public class SongAdapter extends BaseAdapter {

    private ArrayList<Song> songs;
    private LayoutInflater songInf;

    public SongAdapter(Context c, ArrayList<Song> theSongs){
        songs=theSongs;
        songInf=LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return songs.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //map to song layout
        LinearLayout songLay = (LinearLayout)songInf.inflate
                (R.layout.song, parent, false);
        //get title and artist views

        TextView songView = (TextView)songLay.findViewById(R.id.song_title);
        TextView artistView = (TextView)songLay.findViewById(R.id.song_artist);
        ImageView coverView = (ImageView)songLay.findViewById(R.id.imageView);
        //get song using position
        Song currSong = songs.get(position);
        //get title and artist strings

      // String cover = getCoverArtPath(currSong.getID(),convertView.getContext());


        if(!(currSong.getAlbumid()==null)){

            Bitmap myBitmap = BitmapFactory.decodeFile(currSong.getAlbumid());

           coverView.setImageBitmap(myBitmap);

        }


        songView.setText(currSong.getTitle());
        artistView.setText(currSong.getArtist());
        //set position as tag
        songLay.setTag(position);
        return songLay;
    }

    public static String getCoverArtPath(long albumId, Context context) {
        Cursor albumCursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(albumId)},
                null
        );
        boolean queryResult = albumCursor.moveToFirst();
        String result = null;
        if (queryResult) {
            result = albumCursor.getString(0);
        }
        albumCursor.close();
        return result;
    }
}
