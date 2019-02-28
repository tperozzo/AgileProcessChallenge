package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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
    int get_mode;
    boolean isLoading = false;
    Context context;
    private SharedPreferences sharedPrefSettings;
    Dialog loadingDialog;
    boolean goToDetails = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        setupViews();

        context = this;
        sharedPrefSettings = getSharedPreferences(Constants.SHARED_PREF_REF, 0);
        get_mode = getSetting(Constants.GET_MODE_PREF, Constants.API_MODE);

        beerList = new ArrayList<>();
        beerListAdapter = new BeerListAdapter(this, beerList, this);
        beerList_rv.setAdapter(beerListAdapter);

        if(savedInstanceState == null) {

            if(get_mode == Constants.API_MODE)
                getBeerListFromAPI();
            else
                getFavoriteBeers();
        }
        else
            beerList = (ArrayList<Beer>)savedInstanceState.getSerializable(Constants.RV_ITENS_SAVED);
    }

    @Override
    protected void onResume() {
        if(goToDetails && get_mode == Constants.FAVORITES_MODE)
            getFavoriteBeers();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.beer_list_menu, menu);
        if(isLoading){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        }
        else {
            if (get_mode == Constants.FAVORITES_MODE) {
                menu.getItem(0).setVisible(false);
                menu.getItem(1).getSubMenu().getItem(0).setEnabled(true);
                menu.getItem(1).getSubMenu().getItem(1).setEnabled(false);
            } else {
                menu.getItem(0).setVisible(true);
                menu.getItem(1).getSubMenu().getItem(0).setEnabled(false);
                menu.getItem(1).getSubMenu().getItem(1).setEnabled(true);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.load_more_btn:
                getBeerListFromAPI();
                return true;
            case R.id.get_from_api_btn:
                if(get_mode == Constants.FAVORITES_MODE){
                    beerListAdapter.clear();
                    page = 1;
                }
                getBeerListFromAPI();
                return true;
            case R.id.get_favorites_btn:
                getFavoriteBeers();
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

    private void getFavoriteBeers(){
        setTitle("Favorite Beers");
        invalidateOptionsMenu();
        FavoriteBeersTask fTask = new FavoriteBeersTask(context);
        fTask.execute();
        get_mode = Constants.FAVORITES_MODE;
        setSetting(Constants.GET_MODE_PREF, Constants.FAVORITES_MODE);
    }

    private void getBeerListFromAPI(){
        setTitle("Beers from Punk API");
        get_mode = Constants.API_MODE;
        setSetting(Constants.GET_MODE_PREF, Constants.API_MODE);
        StartLoading();
        BeerService service = RetrofitInitiazer.getRetrofitInstance().create(BeerService.class);
        Call<List<Beer>> call = service.getBeers(page, Constants.PER_PAGE);

        call.enqueue(new Callback<List<Beer>>() {
            @Override
            public void onResponse(Call<List<Beer>> call, Response<List<Beer>> response) {
                if(!response.body().isEmpty()){
                    beerList.addAll(response.body());
                    beerListAdapter.notifyDataSetChanged();
                    RVScroolToFirstLoaded();
                    page++;
                }
                else{
                    Snackbar.make(findViewById(R.id.beer_list_root_layout), "End of beers.", Snackbar.LENGTH_SHORT).show();
                }
                FinishLoading();
            }

            @Override
            public void onFailure(Call<List<Beer>> call, Throwable t) {
                FinishLoading();
                Snackbar.make(findViewById(R.id.beer_list_root_layout), "Connection Error", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void RVScroolToFirstLoaded(){
        RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        smoothScroller.setTargetPosition((page-1) * Constants.PER_PAGE);
        beerList_rv.getLayoutManager().startSmoothScroll(smoothScroller);
    }

    public void StartLoading(){
        ShowLoadingDialog();
        isLoading = true;
        invalidateOptionsMenu();
    }

    public void FinishLoading(){
        DismissLoadingDialog();
        isLoading = false;
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
            Intent i = new Intent(context, BeerDetailsActivity.class);
            i.putExtra(Constants.GET_MODE_PREF, get_mode);
            i.putExtra(Constants.BEER_TAG, beerList.get(clickedItemIndex));
            goToDetails = true;
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
            StartLoading();
            beerListAdapter.clear();
        }

        private List<Beer> getFavoriteBeers(Cursor cursor) {
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

            List<Beer> result = getFavoriteBeers(cursor);

            if(result != null) {
                if (!result.isEmpty()) {
                    beerList.addAll(result);
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(List<Beer> beers) {
            if(beers.isEmpty())
                Snackbar.make(findViewById(R.id.beer_list_root_layout), "You have not added any beer to favorites.", Snackbar.LENGTH_SHORT).show();

            isLoading = false;
            invalidateOptionsMenu();
            beerListAdapter.notifyDataSetChanged();
            FinishLoading();
        }
    }

    private int getSetting(String tag, int defaultReturn) {
        try {
            return sharedPrefSettings.getInt(tag, defaultReturn);
        } catch (Exception e) {
            return defaultReturn;
        }
    }

    private void setSetting(String tag, int value) {
        try {
            SharedPreferences.Editor editor = sharedPrefSettings.edit();
            editor.putInt(tag, value);
            editor.commit();
        } catch (Exception e) {

        }
    }

    private void ShowLoadingDialog(){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.dialog_loading);
        loadingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar loadind_pg = loadingDialog.findViewById(R.id.loading_pb);
        loadind_pg.setIndeterminate(true);
        loadind_pg.setVisibility(View.VISIBLE);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void DismissLoadingDialog(){
        if(loadingDialog.isShowing()) {
            ProgressBar loadind_pg = loadingDialog.findViewById(R.id.loading_pb);
            loadind_pg.setIndeterminate(false);
            loadind_pg.setVisibility(View.GONE);
            loadingDialog.dismiss();
        }
    }

}
