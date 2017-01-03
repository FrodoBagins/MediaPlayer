package com.kamil.mediaplayer;

/**
 * Created by Kamil_2 on 2017-01-03.
 */



public class SongModel {

    private long id;
    private String title;
    private String author;
    private String album;
    private String albumpath;
    private long length;

    public SongModel(long id, String title, String author, String album, String albumpath, long length) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.album = album;
        this.albumpath = albumpath;
        this.length = length;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getAlbum() {
        return album;
    }

    public String getAlbumpath() {
        return albumpath;
    }

    public long getLength() {
        return length;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumpath(String albumpath) {
        this.albumpath = albumpath;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
