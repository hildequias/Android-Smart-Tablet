package com.pixonsoft.carroapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pixonsoft.carroapp.R;
import com.pixonsoft.carroapp.adapter.CarroAdapter;
import com.pixonsoft.carroapp.model.Carro;
import com.pixonsoft.carroapp.services.CarroService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarrosFragment extends Fragment {

    private RecyclerView rvCarros;
    private LinearLayoutManager layoutManager;
    private List<Carro> carros;
    private final String TIPO = "tipo";
    private int tipo;

    public CarrosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle){

        super.onCreate(bundle);

        if(getArguments() != null){
            this.tipo = getArguments().getInt(TIPO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_carros, container, false);
        layoutManager = new LinearLayoutManager(getContext());

        rvCarros = (RecyclerView) view.findViewById(R.id.rvCarros);
        rvCarros.setLayoutManager(layoutManager);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle){

        super.onActivityCreated(bundle);
        taskCarros();
    }

    private void taskCarros() {

        CarroService carroService = new CarroService(getContext());
        carros = carroService.getCarros(carroService.loadJSONFromAsset(tipo));
        rvCarros.setAdapter(new CarroAdapter(getContext(), carros));
    }
}
