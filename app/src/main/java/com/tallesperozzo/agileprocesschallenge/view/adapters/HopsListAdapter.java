package com.tallesperozzo.agileprocesschallenge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Hops;

import java.util.List;

public class HopsListAdapter extends RecyclerView.Adapter<HopsListAdapter.HopsViewHolder> {

    private final Context context;
    private final List<Hops> hopsList;

    public HopsListAdapter(Context context, List<Hops> hopsList) {
        this.context = context;
        this.hopsList = hopsList;
    }

    @NonNull
    @Override
    public HopsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_hops, viewGroup, false);
        return new HopsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HopsViewHolder hopsViewHolder, int position) {
        Hops hops = hopsList.get(position);
        hopsViewHolder.name_tv.setText(hops.getName());
        hopsViewHolder.amount_tv.setText(context.getString(R.string.measure_placeholder, String.valueOf(hops.getAmount().getValue()), hops.getAmount().getUnit()));
        hopsViewHolder.add_tv.setText(context.getString(R.string.add, String.valueOf(hops.getAdd())));
        hopsViewHolder.attr_tv.setText(context.getString(R.string.attr, hops.getAttribute()));
    }

    @Override
    public int getItemCount() {
        return hopsList.size();
    }

    class HopsViewHolder extends RecyclerView.ViewHolder {

        final TextView name_tv;
        final TextView amount_tv;
        final TextView add_tv;
        final TextView attr_tv;

        HopsViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            amount_tv = itemView.findViewById(R.id.amount_tv);
            add_tv = itemView.findViewById(R.id.add_tv);
            attr_tv = itemView.findViewById(R.id.attr_tv);
        }

    }
}