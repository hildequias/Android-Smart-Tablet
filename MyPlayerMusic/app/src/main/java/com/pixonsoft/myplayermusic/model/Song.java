package com.pixonsoft.myplayermusic.model;

/**
 * Created by mobile6 on 1/24/16.
 */
public class Song {

    public String title;
    public String artist;
    public long songid;
    public String albumkey;
    public String artistkey;
    public String album;
    public String trackid;

    public Song(String title, String artist,String album, long songid,String artistkey, String albumkey, String trackid ) {
        this.title = title;
        this.artist = artist;
        this.songid = songid;
        this.albumkey = albumkey;
        this.album=album;
        this.artistkey=artistkey;
        this.trackid=trackid;

    }
    @Override
    public boolean equals (Object object) {
        boolean result = false;
        if (object == null) {
            result = false;
        } else {
            Song a = (Song) object;
            if (this.songid == a.songid) {
                result = true;
            }
        }
        return result;
    }
}
