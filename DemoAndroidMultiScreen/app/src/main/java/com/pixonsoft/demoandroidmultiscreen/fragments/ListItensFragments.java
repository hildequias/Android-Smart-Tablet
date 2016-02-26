package com.pixonsoft.demoandroidmultiscreen.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pixonsoft.demoandroidmultiscreen.R;
import com.pixonsoft.demoandroidmultiscreen.model.Item;


public class ListItensFragments extends Fragment {

    private ArrayAdapter<Item> adapter;
    private ListView lvItens;

    public ListItensFragments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ArrayAdapter<Item>(getContext(),
                android.R.layout.simple_list_item_activated_1,
                Item.getItens());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.list_itens_fragments, container, false);
        lvItens = (ListView) v.findViewById(R.id.listView);
        lvItens.setAdapter(adapter);
        // Inflate the layout for this fragment
        return v;
    }


}
