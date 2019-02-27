package com.tallesperozzo.agileprocesschallenge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.MashTemp;


import java.util.List;

public class MashTempListAdapter extends RecyclerView.Adapter<MashTempListAdapter.MashTempViewHolder> {

    Context context;
    List<MashTemp> mashTempList;

    public MashTempListAdapter(Context context, List<MashTemp> mashTempList) {
        this.context = context;
        this.mashTempList = mashTempList;
    }

    @NonNull
    @Override
    public MashTempViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_mash_temp, viewGroup, false);
        return new MashTempViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MashTempViewHolder mashTempViewHolder, int position) {
        MashTemp mashTemp = mashTempList.get(position);
        mashTempViewHolder.temp_tv.setText(mashTemp.getTemp().getValue() + " " + mashTemp.getTemp().getUnit());
        if(!TextUtils.isEmpty(mashTemp.getDuration()))
            mashTempViewHolder.duration_tv.setText("Duration: " + mashTemp.getDuration());
        else
            mashTempViewHolder.duration_tv.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mashTempList.size();
    }

    class MashTempViewHolder extends RecyclerView.ViewHolder {

        TextView temp_tv;
        TextView duration_tv;

        public MashTempViewHolder(View itemView) {
            super(itemView);
            temp_tv = itemView.findViewById(R.id.temp_tv);
            duration_tv = itemView.findViewById(R.id.duration_tv);
        }

    }
}