package com.pixonsoft.myplayermusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pixonsoft.myplayermusic.R;
import com.pixonsoft.myplayermusic.model.Song;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    public SongAdapter(Context context, ArrayList<Song> songs) {
        super(context, 0, songs);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obter o item de dados para esta posição
        Song song = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
        }

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);

        tvTitle.setText(song.title);
        tvArtist.setText(song.artist);
        tvId.setText(""+song.songid);

        return convertView;
    }
}
