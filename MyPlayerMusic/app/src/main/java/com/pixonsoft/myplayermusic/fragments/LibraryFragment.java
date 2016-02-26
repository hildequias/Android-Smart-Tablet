package com.pixonsoft.myplayermusic.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.pixonsoft.myplayermusic.R;
import com.pixonsoft.myplayermusic.SongListActivity;
import com.pixonsoft.myplayermusic.adapter.AlbumAdapter;
import com.pixonsoft.myplayermusic.adapter.ArtistAdapter;
import com.pixonsoft.myplayermusic.adapter.SongAdapter;
import com.pixonsoft.myplayermusic.dao.MusicDao;
import com.pixonsoft.myplayermusic.model.Album;
import com.pixonsoft.myplayermusic.model.Artist;
import com.pixonsoft.myplayermusic.model.Song;
import com.pixonsoft.myplayermusic.service.MusicService;
import com.pixonsoft.myplayermusic.service.MusicService.MusicBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by mobile6 on 1/24/16.
 */
public class LibraryFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;
    public MusicService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;

            musicSrv = binder.getService();

            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(getActivity().getApplicationContext(), MusicService.class);
            getActivity().getApplicationContext().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().getApplicationContext().startService(playIntent);
        }
    }

    public static LibraryFragment newInstance(int position) {
        LibraryFragment f = new LibraryFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Intent servicestart=new Intent(getActivity(), MusicService.class);

        getActivity().startService(servicestart);
        View sarkilar = inflater.inflate(R.layout.fragment_library, container, false);
        ListView listemiz = (ListView)sarkilar.findViewById(R.id.list1);

        // Lista de Artistas
        if(position==0){
            final ArrayList<Artist> arrLArtists=MusicDao.getArtistList(getActivity().getApplicationContext());
            ArtistAdapter adapter = new ArtistAdapter(getActivity(), arrLArtists);
            listemiz.setAdapter(adapter);
            listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView selectedview = (TextView)view.findViewById(R.id.tvSongTitle);
                    String selectedsongid=selectedview.getText().toString();
                    Intent intent=new Intent(getActivity(),SongListActivity.class);
                    intent.putExtra("type", "albumsofartist");
                    intent.putExtra("key", selectedsongid);

                    startActivity(intent);

                }
            });
        }
        else if(position==1){
            final ArrayList<Album> arrLAlbums=MusicDao.getAlbumList(getActivity().getApplicationContext());
            Collections.sort(arrLAlbums, new Comparator<Album>() {
                public int compare(Album s1, Album s2) {
                    return s1.albumtitle.compareToIgnoreCase(s2.albumtitle);
                }
            });
            AlbumAdapter adapter = new AlbumAdapter(getActivity(), arrLAlbums);
            listemiz.setAdapter(adapter);
            listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView selectedview = (TextView)view.findViewById(R.id.tvSongId);
                    String selectedsongid=selectedview.getText().toString();
                    Intent intent=new Intent(getActivity(),SongListActivity.class);
                    intent.putExtra("type", "songsofalbum");
                    intent.putExtra("key", selectedsongid);

                    startActivity(intent);

                }
            });
        }else {
            final ArrayList<Song> arrLSongs = MusicDao.getSongList(getActivity().getApplicationContext());
            Collections.sort(arrLSongs, new Comparator<Song>(){
                public int compare(Song s1, Song s2) {
                    return s1.title.compareToIgnoreCase(s2.title);
                }
            });
            SongAdapter adapter = new SongAdapter(getActivity(), arrLSongs);
            listemiz.setAdapter(adapter);

            listemiz.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    TextView selectedview = (TextView)view.findViewById(R.id.tvSongId);
                    String selectedsongid=selectedview.getText().toString();

                    if(musicBound) {
                        musicSrv.getSongListAndSong(arrLSongs,position);
                    }
                }
            });
        }
        return sarkilar;
    }
}
