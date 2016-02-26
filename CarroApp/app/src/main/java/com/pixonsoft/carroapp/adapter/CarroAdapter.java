package com.pixonsoft.carroapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.tv.TvContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pixonsoft.carroapp.R;
import com.pixonsoft.carroapp.model.Carro;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by mobile6 on 11/19/15.
 */
public class CarroAdapter extends RecyclerView.Adapter<CarroAdapter.CarroViewHolder> {

    private Context context;
    private List<Carro> carros;

    public CarroAdapter (Context context, List<Carro> carros){
        this.context    = context;
        this.carros     = carros;
    }

    @Override
    public CarroViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(this.context).inflate(R.layout.carro_card_row, parent, false);
        return new CarroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarroViewHolder holder, int position) {

        try {

            holder.tvNomeCarro.setText(carros.get(position).getNome());
            holder.tvDescCarro.setText(carros.get(position).getDesc());

            InputStream is = context.getAssets().open(carros.get(position).getFoto());
            Drawable fotoCarro = Drawable.createFromStream(is, null);
            holder.ivCarro.setImageDrawable(fotoCarro);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return carros.size();
    }

    public static class CarroViewHolder extends RecyclerView.ViewHolder {

        TextView tvNomeCarro;
        TextView tvDescCarro;
        ImageView ivCarro;
        ImageView ivOpcoes;
        ProgressBar pbCarro;

        public CarroViewHolder(View itemView) {
            super(itemView);

            this.tvNomeCarro = (TextView) itemView.findViewById(R.id.tvNomeCarro);
            this.tvDescCarro = (TextView) itemView.findViewById(R.id.tvDescCarro);
            this.ivCarro = (ImageView) itemView.findViewById(R.id.ivCarro);
            this.ivOpcoes = (ImageView) itemView.findViewById(R.id.ivOpcoes);
            this.pbCarro = (ProgressBar) itemView.findViewById(R.id.pbCarro);
        }
    }
}
