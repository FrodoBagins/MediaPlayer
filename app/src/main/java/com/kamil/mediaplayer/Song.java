package com.kamil.mediaplayer;

/**
 * Created by Kamil_2 on 2016-12-17.
 */

public class Song {

    private long id;

    private String title;
    private String artist;



    public Song(long songID, String songTitle, String songArtist) {
        id=songID;
        title=songTitle;
        artist=songArtist;
    }

    public long getID(){return id;}
    public String getTitle(){return title;}
    public String getArtist(){return artist;}



}
