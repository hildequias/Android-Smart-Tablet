package com.pixonsoft.myplayermusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pixonsoft.myplayermusic.R;
import com.pixonsoft.myplayermusic.model.Album;

import java.util.ArrayList;

/**
 * Created by mobile6 on 1/24/16.
 */
public class AlbumAdapter extends ArrayAdapter<Album> {
    public AlbumAdapter(Context context, ArrayList<Album> albums) {
        super(context, 0, albums);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Obter o item de dados para esta posição
        Album album = getItem(position);
        // Verifique se uma view existente está sendo reutilizado, caso contrário inflar a view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_song, parent, false);
        }
        // View de pesquisa para preenchimento de dados
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvSongTitle);
        TextView tvArtist = (TextView) convertView.findViewById(R.id.tvSongArtist);
        TextView tvId= (TextView) convertView.findViewById(R.id.tvSongId);
        // Preencher os dados na view de modelo usando o objeto de dados
        tvTitle.setText(album.albumtitle);
        tvArtist.setText(album.albumartist);
        tvId.setText(""+album.albumkey);
        // retorna a vista concluída para exibir na tela
        return convertView;
    }
}
