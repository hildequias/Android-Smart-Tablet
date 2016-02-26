package com.pixonsoft.myplayermusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pixonsoft.myplayermusic.R;
import com.pixonsoft.myplayermusic.model.Artist;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class ArtistAdapter extends ArrayAdapter<Artist> {
    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obter o item para esta posição
        Artist artist= getItem(position);
        // Verifique se uma view existente e está sendo reutilizado, caso contrário inflar a view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
        }
        // View de pesquisa para preenchimento de dados
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);
        // Preencher os dados na view de modelo usando o objeto.
        tvTitle.setText(artist.artisttitle);
        String psalbum;
        String pstrack;

        if(artist.numberofalbums.equals("1")) {
            psalbum="album";

        } else{
            psalbum="albums";
        }

        if(artist.numberoftracks.equals("1")) {
            pstrack="track";
        } else{
            pstrack="tracks";
        }

        tvArtist.setText(artist.numberofalbums+" "+psalbum+", "+artist.numberoftracks+" "+pstrack);
        tvId.setText(artist.artistkey);
        // returna a view concluída para exibir na tela
        return convertView;
    }
}
