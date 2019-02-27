package com.tallesperozzo.agileprocesschallenge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Malt;

import java.util.List;

public class MaltListAdapter extends RecyclerView.Adapter<MaltListAdapter.MaltViewHolder>{

    Context context;
    List<Malt> maltList;

    public MaltListAdapter(Context context, List<Malt> maltList) {
        this.context = context;
        this.maltList = maltList;
    }

    @NonNull
    @Override
    public MaltViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_malt, viewGroup, false);
        return new MaltViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MaltViewHolder hopsViewHolder, int position) {
        Malt malt = maltList.get(position);
        hopsViewHolder.name_tv.setText(malt.getName());
        hopsViewHolder.amount_tv.setText(malt.getAmount().getValue() + " " + malt.getAmount().getUnit());
    }

    @Override
    public int getItemCount() {
        return maltList.size();
    }

    class MaltViewHolder extends RecyclerView.ViewHolder {

        TextView name_tv;
        TextView amount_tv;

        public MaltViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            amount_tv = itemView.findViewById(R.id.amount_tv);
        }

    }

}
