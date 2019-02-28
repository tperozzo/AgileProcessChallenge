package com.tallesperozzo.agileprocesschallenge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Malt;

/*
 * MaltListAdapter
 *
 * To see the Malt list of a beer in BeerDetailActivity
 *
 * Created by Talles Perozzo
 */

public class MaltListAdapter extends RecyclerView.Adapter<MaltListAdapter.MaltViewHolder>{

    private final Context context;
    private final List<Malt> maltList;

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
        hopsViewHolder.amount_tv.setText(context.getString(R.string.measure_placeholder, String.valueOf(malt.getAmount().getValue()), malt.getAmount().getUnit()));
    }

    @Override
    public int getItemCount() {
        return maltList.size();
    }

    class MaltViewHolder extends RecyclerView.ViewHolder {

        final TextView name_tv;
        final TextView amount_tv;

        MaltViewHolder(View itemView) {
            super(itemView);
            name_tv = itemView.findViewById(R.id.name_tv);
            amount_tv = itemView.findViewById(R.id.amount_tv);
        }

    }

}
