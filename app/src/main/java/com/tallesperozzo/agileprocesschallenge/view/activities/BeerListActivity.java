package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.retrofit.RetrofitInitiazer;
import com.tallesperozzo.agileprocesschallenge.retrofit.service.BeerService;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;
import com.tallesperozzo.agileprocesschallenge.view.adapters.BeerListAdapter;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeerListActivity extends AppCompatActivity {

    RecyclerView beerList_rv;
    BeerListAdapter beerListAdapter;
    ProgressBar beers_pb;
    List<Beer> beerList;
    int page = 1;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        setupViews();

        if(savedInstanceState == null)
            beerList = new ArrayList<>();
        else
            beerList = (ArrayList<Beer>)savedInstanceState.getSerializable(Constants.RV_ITENS_SAVED);

        beerListAdapter = new BeerListAdapter(this, beerList);
        beerList_rv.setAdapter(beerListAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beer_list_menu, menu);
        if(isLoading)
            menu.getItem(0).setVisible(false);
        else
            menu.getItem(0).setVisible(true);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.load_more_btn:
                getBeerList();
                invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupViews(){
        beers_pb = findViewById(R.id.beers_pb);
        beerList_rv = findViewById(R.id.beer_list_rv);
        beerList_rv.setHasFixedSize(true);
        beerList_rv.setLayoutManager(new LinearLayoutManager(this));
        beerList_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private void getBeerList(){
        beers_pb.setIndeterminate(true);
        beers_pb.setVisibility(View.VISIBLE);
        isLoading = true;
        BeerService service = RetrofitInitiazer.getRetrofitInstance().create(BeerService.class);
        Call<List<Beer>> call = service.getBeers(page, Constants.PER_PAGE);

        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                beerList.addAll(response.body());
                beerListAdapter.notifyDataSetChanged();
                beers_pb.setIndeterminate(false);
                beers_pb.setVisibility(View.GONE);
                page++;
                isLoading = false;
                invalidateOptionsMenu();
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                beers_pb.setIndeterminate(false);
                beers_pb.setVisibility(View.GONE);
                isLoading = false;
                invalidateOptionsMenu();
            }
        });
    }
}
