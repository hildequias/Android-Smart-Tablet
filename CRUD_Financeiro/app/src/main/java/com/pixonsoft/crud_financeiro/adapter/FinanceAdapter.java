package com.pixonsoft.crud_financeiro.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixonsoft.crud_financeiro.R;
import com.pixonsoft.crud_financeiro.model.Finance;

import java.util.List;

/**
 * Created by mobile6 on 11/26/15.
 */
public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.FinanceViewHolder> {

    private Context context;
    private List<Finance> finances;
    private Double total;

    public FinanceAdapter(Context context, List<Finance> finances){
        this.context = context;
        this.finances = finances;
        this.total = 0.0;
    }

    @Override
    public FinanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(this.context).inflate(R.layout.item_list, parent, false);
        return new FinanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FinanceViewHolder holder, int position) {

        Log.i("TESTE:",finances.get(position).getValue().toString());
        holder.tvValue.setText(finances.get(position).getValue().toString());
        holder.tvType.setText(finances.get(position).getType_finance());
        holder.tvDescription.setText(finances.get(position).getDescription());
    }

    @Override
    public int getItemCount() {

        return finances.size();
    }

    public static class FinanceViewHolder extends RecyclerView.ViewHolder {

        TextView tvDescription;
        TextView tvType;
        TextView tvValue;

        public FinanceViewHolder(View itemView) {
            super(itemView);

            this.tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            this.tvType = (TextView) itemView.findViewById(R.id.tvType);
            this.tvValue = (TextView) itemView.findViewById(R.id.tvValue);
        }
    }
}
