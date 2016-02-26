package com.pixonsoft.myplayermusic.dao;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.pixonsoft.myplayermusic.model.Album;
import com.pixonsoft.myplayermusic.model.Artist;
import com.pixonsoft.myplayermusic.model.Song;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class MusicDao {

    // Lista de músicas a partir do dispositivo
    public static ArrayList<Song> getSongList(Context context){

        ArrayList<Song> liste =  new ArrayList<Song>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, MediaStore.Audio.Media.IS_MUSIC +"!=0", null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            do {


                long thisId = musicCursor.getLong(musicCursor.getColumnIndex("_ID"));
                String thisTitle =  musicCursor.getString(musicCursor.getColumnIndex("TITLE"));
                String thisArtist = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                String thisAlbum = musicCursor.getString(musicCursor.getColumnIndex("ALBUM"));
                String thisArtistId = musicCursor.getString(musicCursor.getColumnIndex("ARTIST_KEY"));
                String thisAlbumId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                String thisTrackId = musicCursor.getString(musicCursor.getColumnIndex("TRACK"));
                Song thisSong=new Song(thisTitle,thisArtist, thisAlbum,thisId, thisArtistId, thisAlbumId,thisTrackId);

                liste.add(thisSong);


            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }

    // Lista de álbuns a partir do dispositivo
    public static ArrayList<Album> getAlbumList(Context context){

        ArrayList<Album> liste =  new ArrayList<Album>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);



        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {

                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                String thisTitle = musicCursor.getString(musicCursor.getColumnIndex("ALBUM"));
                String thisArtist = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));

                Album thisAlbum=new Album(thisId,thisTitle,thisArtist);

                liste.add(thisAlbum);
            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }

    // Arte do álbum em cache baseado na chave do álbum
    public static String getAlbumArt(Context context,String albumkey){

        String albumart=null;
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {

                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ALBUM_KEY"));
                if(thisId.equals(albumkey)){
                    albumart=musicCursor.getString(musicCursor.getColumnIndex("ALBUM_ART"));
                    break;
                }
            }
            while (musicCursor.moveToNext());
        }
        return albumart;
    }

    // Lista de artistas do dispositivo
    public static ArrayList<Artist> getArtistList(Context context){

        ArrayList<Artist> liste =  new ArrayList<Artist>();
        ContentResolver musicResolver;

        musicResolver =  context.getContentResolver();

        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicUri,null,null,null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){

            do {

                String thisId = musicCursor.getString(musicCursor.getColumnIndex("ARTIST_KEY"));
                String thisTitle = musicCursor.getString(musicCursor.getColumnIndex("ARTIST"));
                String thisNOA = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_ALBUMS"));
                String thisNOT = musicCursor.getString(musicCursor.getColumnIndex("NUMBER_OF_TRACKS"));

                Artist thisArtist=new Artist(thisId,thisTitle,thisNOA,thisNOT);

                liste.add(thisArtist);
            }
            while (musicCursor.moveToNext());
        }
        return(liste);
    }

    public static ArrayList<Artist> getArtistListFromSongs(Context context){
        ArrayList<Artist> liste =  new ArrayList<Artist>();
        ArrayList<Song> allsongs=getSongList(context);
        for (Song item : allsongs) {
            Artist thisartist=new Artist(item.artistkey,item.artist,"-","-");
            if(!liste.contains(thisartist))
            {
                liste.add(thisartist);
            }
        }

        return liste;
    }

    public static ArrayList<Album> getAlbumListFromSongs(Context context){
        ArrayList<Album> liste =  new ArrayList<Album>();
        ArrayList<Song> allsongs=getSongList(context);
        for (Song item : allsongs) {
            Album thisalbum=new Album(item.albumkey,item.album, item.artist);
            if(!liste.contains(thisalbum))
            {
                liste.add(thisalbum);
            }
        }

        return liste;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Altura e largura da imagem Raw
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calcule o maior valor inSampleSize que é uma potência de 2 e mantém ambos
            // altura e a largura maior do que a altura e largura requerida.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    public static int getMax(int a, int b) {
        return (a>=b?a:b);
    }
}
