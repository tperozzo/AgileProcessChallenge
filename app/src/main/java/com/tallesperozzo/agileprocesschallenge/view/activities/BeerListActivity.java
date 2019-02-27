package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.os.AsyncTask;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.data.FavoriteBeersContract;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.retrofit.RetrofitInitiazer;
import com.tallesperozzo.agileprocesschallenge.retrofit.service.BeerService;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;
import com.tallesperozzo.agileprocesschallenge.view.adapters.BeerListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BeerListActivity extends AppCompatActivity implements BeerListAdapter.ListItemClickListener{

    RecyclerView beerList_rv;
    BeerListAdapter beerListAdapter;
    ProgressBar beers_pb;
    List<Beer> beerList;
    int page = 1;
    int get_mode = Constants.API_MODE; //TODO tirar depois de shared pref
    boolean isLoading = false;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        setupViews();

        ctx = this;

        if(savedInstanceState == null)
            beerList = new ArrayList<>();
        else
            beerList = (ArrayList<Beer>)savedInstanceState.getSerializable(Constants.RV_ITENS_SAVED);

        beerListAdapter = new BeerListAdapter(this, beerList, this);
        beerList_rv.setAdapter(beerListAdapter);

        getBeerList();

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
                if(get_mode == Constants.FAVORITES_MODE){
                    beerListAdapter.clear();
                    getBeerList();
                    get_mode = Constants.API_MODE;
                    page = 1;
                }
                else{
                    getBeerList();
                }
                return true;
            case R.id.get_from_api_btn:
                if(get_mode == Constants.FAVORITES_MODE){
                    beerListAdapter.clear();
                    getBeerList();
                    get_mode = Constants.API_MODE;
                    page = 1;
                }
                else{
                    getBeerList();
                }
                return true;
            case R.id.get_favorites_btn:
                FavoriteBeersTask fTask = new FavoriteBeersTask(ctx);
                fTask.execute();
                get_mode = Constants.FAVORITES_MODE;
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
        StartLoading();
        BeerService service = RetrofitInitiazer.getRetrofitInstance().create(BeerService.class);
        Call<List<Beer>> call = service.getBeers(page, Constants.PER_PAGE);

        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                beerList.addAll(response.body());
                beerListAdapter.notifyDataSetChanged();
                FinishLoading();
                RVScroolToFirstLoaded();
                page++;
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                FinishLoading();
            }
        });
    }

    public void RVScroolToFirstLoaded(){
        RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(ctx) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        smoothScroller.setTargetPosition((page-1) * Constants.PER_PAGE);
        beerList_rv.getLayoutManager().startSmoothScroll(smoothScroller);
    }

    public void FinishLoading(){
        beers_pb.setIndeterminate(false);
        beers_pb.setVisibility(View.GONE);
        isLoading = false;
        invalidateOptionsMenu();
    }

    public void StartLoading(){
        beers_pb.setIndeterminate(true);
        beers_pb.setVisibility(View.VISIBLE);
        isLoading = true;
        invalidateOptionsMenu();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.RV_ITENS_SAVED, (Serializable) beerList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(!isLoading) {
            Intent i = new Intent(ctx, BeerDetailsActivity.class);
            i.putExtra(Constants.BEER_TAG, beerList.get(clickedItemIndex));
            startActivity(i);
        }
    }

    public class FavoriteBeersTask extends AsyncTask<Void, Void, List<Beer>> {

        private Context mContext;

        public FavoriteBeersTask(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            beerListAdapter.clear();
        }

        private List<Beer> getFavoriteMovies(Cursor cursor) {
            List<Beer> results = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Beer beer = new Beer(cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getFloat(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));
                    results.add(beer);
                } while (cursor.moveToNext());
                cursor.close();
            }
            return results;
        }

        @Override
        protected List<Beer> doInBackground(Void... params) {
            Cursor cursor = mContext.getContentResolver().query(
                    FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
            );

            List<Beer> result = getFavoriteMovies(cursor);

            if(result != null) {
                if (!result.isEmpty()) {
                    beerList.addAll(result);

                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Beer> eers) {

            beerListAdapter.notifyDataSetChanged();

        }
    }
}
