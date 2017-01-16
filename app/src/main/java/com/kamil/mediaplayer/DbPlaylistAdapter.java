package com.kamil.mediaplayer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * Created by Kamil_2 on 2017-01-03.
 */

public class DbPlaylistAdapter {

    private static final String DEBUG_TAG = "SqLiteTodoManager";

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper dbHelper;

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "database2.db";
    private static final String DB_TODO2_TABLE = "todo2";

    public static final String KEY_ID = "_id";
    public static final String ID_OPTIONS = "INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN = 0;

    public static final String KEY_TITLE = "title";
    public static final String TITLE_OPTIONS = "TEXT NOT NULL";
    public static final int TITLE_COLUMN = 1;

    public static final String KEY_AUTHOR = "author";
    public static final String AUTHOR_OPTIONS = "TEXT NOT NULL";
    public static final int AUTHOR_COLUMN = 2;

    public static final String KEY_ALBUM = "album";
    public static final String ALBUM_OPTIONS = "TEXT NOT NULL";
    public static final int ALBUM_COLUMN = 3;

    public static final String KEY_ALBUMPATH = "albumpath";
    public static final String ALBUMPATH_OPTIONS = "TEXT NOT NULL";
    public static final int ALBUMPATH_COLUMN = 4;

    public static final String KEY_LENGTH = "length";
    public static final String LENGTH_OPTIONS = "INTEGER DEFAULT 0";
    public static final int LENGTH_COLUMN = 5;

    public static final String KEY_SONGID = "songid";
    public static final String SONGID_OPTIONS = "INTEGER DEFAULT 0";
    public static final int SONGID_COLUMN = 6;

    private static final String DB_CREATE_TODO2_TABLE =
            "CREATE TABLE " + DB_TODO2_TABLE + "( " +
                    KEY_ID + " " + ID_OPTIONS + ", " +
                    KEY_TITLE + " " + TITLE_OPTIONS + ", " +
                    KEY_AUTHOR + " " + AUTHOR_OPTIONS + ", " +
                    KEY_ALBUM + " " + ALBUM_OPTIONS + ", " +
                    KEY_ALBUMPATH + " " + ALBUMPATH_OPTIONS + ", " +
                    KEY_LENGTH + " " + LENGTH_OPTIONS + ", " +
                    KEY_SONGID + " " + SONGID_OPTIONS +
                    ");";

    private static final String DROP_TODO2_TABLE =
            " DROP TABLE IF EXISTS " + DB_TODO2_TABLE;

    public DbPlaylistAdapter(Context context) {
        this.context = context;
    }

    public DbPlaylistAdapter open(){
        dbHelper = new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            db = dbHelper.getReadableDatabase();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertSong(String title, String author, String album, String albumpath, long length, long songid) {
        ContentValues newSongValues = new ContentValues();
        newSongValues.put(KEY_TITLE, title);
        newSongValues.put(KEY_AUTHOR, author);
        newSongValues.put(KEY_ALBUM, album);
        newSongValues.put(KEY_ALBUMPATH, albumpath);
        newSongValues.put(KEY_LENGTH, length);
        newSongValues.put(KEY_SONGID, songid);
        return db.insert(DB_TODO2_TABLE, null, newSongValues);
    }

    public boolean updateSong(SongModel song) {
        long id = song.getId();
        String title = song.getTitle();
        String author = song.getAuthor();
        String album = song.getAlbum();
        String albumpath = song.getAlbumpath();
        long length = song.getLength();
        long songid = song.getSongid();
        return updateSong(id, title, author, album, albumpath, length, songid);
    }

    public boolean updateSong(long id, String title, String author, String album, String albumpath, long length, long songid) {
        String where = KEY_ID + "=" + id;
        ContentValues updateSongValues = new ContentValues();
        updateSongValues.put(KEY_TITLE, title);
        updateSongValues.put(KEY_AUTHOR, title);
        updateSongValues.put(KEY_ALBUM, title);
        updateSongValues.put(KEY_ALBUMPATH, title);
        updateSongValues.put(KEY_LENGTH, title);
        updateSongValues.put(KEY_SONGID, songid);
        return db.update(DB_TODO2_TABLE, updateSongValues, where, null) > 0;
    }

    public boolean deleteSOng(long id){
        String where = KEY_ID + "=" + id;
        return db.delete(DB_TODO2_TABLE, where, null) > 0;
    }

    public Cursor getAllSongs(){
        String [] columns = {KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_ALBUM, KEY_ALBUMPATH, KEY_LENGTH, KEY_SONGID};
        return db.query(DB_TODO2_TABLE, columns, null, null, null, null, null);
    }

    public SongModel getSong(long id) {
        String[] columns = {KEY_ID, KEY_TITLE, KEY_AUTHOR, KEY_ALBUM, KEY_ALBUMPATH, KEY_LENGTH, KEY_SONGID};
        String where = KEY_ID + "=" + id;
        Cursor cursor = db.query(DB_TODO2_TABLE, columns, where, null, null, null, null);
        SongModel song = null;
        if(cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(TITLE_COLUMN);
            String author = cursor.getString(AUTHOR_COLUMN);
            String album = cursor.getString(ALBUM_COLUMN);
            String albumpath = cursor.getString(ALBUMPATH_COLUMN);
            long length = cursor.getLong(LENGTH_COLUMN);
            long songid = cursor.getLong(SONGID_COLUMN);
            song = new SongModel(id, title, author, album, albumpath, length, songid);
        }
        return song;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {

            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_TODO2_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TODO2_TABLE);
            onCreate(db);
        }
    }

}

