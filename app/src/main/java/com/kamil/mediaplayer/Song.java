package com.kamil.mediaplayer;

/**
 * Created by Kamil_2 on 2016-12-17.
 */

public class Song {

    private long id;

    private int songid1;

    private String title;
    private String artist;
    private String albumid;

    public Song(long songID, String songTitle, String songArtist, String songAlbumid, int songid2) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        albumid=songAlbumid;
        songid1=songid2;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbumid(){return albumid;}
    public int getSongId(){return songid1;}

}
