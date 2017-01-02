package com.kamil.mediaplayer;

/**
 * Created by Kamil_2 on 2016-12-17.
 */

public class Song {

    private long id;

    private String title;
    private String artist;
    private String albumid;



    public Song(long songID, String songTitle, String songArtist, String songAlbumid) {
        id=songID;
        title=songTitle;
        artist=songArtist;
        albumid=songAlbumid;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}
    public String getAlbumid(){return albumid;}


}
