package com.tallesperozzo.agileprocesschallenge.view.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Beer;

import java.util.List;

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerViewHolder> {

    Context context;
    List<Beer> beerList;
    final private ListItemClickListener mOnClickListener;

    public BeerListAdapter(Context context, List<Beer> beerList, ListItemClickListener listener) {
        this.context = context;
        this.beerList = beerList;
        mOnClickListener = listener;
    }

    public void clear() {
        final int size = beerList.size();
        beerList.clear();
        notifyItemRangeRemoved(0, size);
    }

    @NonNull
    @Override
    public BeerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_beer, viewGroup, false);
        return new BeerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BeerViewHolder beerViewHolder, int position) {
        Beer beer = beerList.get(position);

        Picasso.with(context)
                .load(beer.getImage_url())
                .into(beerViewHolder.imageUrl_iv, new Callback() {
                            @Override
                            public void onSuccess() {
                                beerViewHolder.imageUrl_pb.setIndeterminate(false);
                                beerViewHolder.imageUrl_pb.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                beerViewHolder.imageUrl_pb.setIndeterminate(false);
                                beerViewHolder.imageUrl_pb.setVisibility(View.GONE);
                            }
                        });

        beerViewHolder.name_tv.setText(beer.getName());
        beerViewHolder.tagline_tv.setText(beer.getTagline());
        beerViewHolder.abv_tv.setText(String.valueOf(beer.getAbv()));
        beerViewHolder.first_brewed_tv.setText(beer.getFirst_brewed());
    }

    @Override
    public int getItemCount() {
        return beerList.size();
    }

    class BeerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageUrl_iv;
        ProgressBar imageUrl_pb;
        TextView name_tv;
        TextView tagline_tv;
        TextView abv_tv;
        TextView first_brewed_tv;

        public BeerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageUrl_iv = itemView.findViewById(R.id.image_url_iv);
            imageUrl_pb = itemView.findViewById(R.id.image_url_pb);
            name_tv = itemView.findViewById(R.id.name_tv);
            tagline_tv = itemView.findViewById(R.id.tagline_tv);
            abv_tv = itemView.findViewById(R.id.abv_tv);
            first_brewed_tv = itemView.findViewById(R.id.first_brewed_tv);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onListItemClick(getAdapterPosition());
        }
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }
}
