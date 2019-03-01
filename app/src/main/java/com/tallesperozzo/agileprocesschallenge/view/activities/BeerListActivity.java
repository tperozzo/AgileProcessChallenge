package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.os.AsyncTask;

import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.data.FavoriteBeersContract;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.retrofit.RetrofitInitializer;
import com.tallesperozzo.agileprocesschallenge.retrofit.service.BeerService;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;
import com.tallesperozzo.agileprocesschallenge.view.components.SimpleDividerItemDecoration;
import com.tallesperozzo.agileprocesschallenge.view.adapters.BeerListAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
 * BeerListActivity
 *
 * This activity get and show the beer list.
 * The Get Mode can be from favorites database or Punk API.
 * Once the mode is selected it is saved in the application preferences file.
 * When a list item is selected, this goes to the beer detail screen.
 *
 * Created by Talles Perozzo
 */

public class BeerListActivity extends AppCompatActivity implements BeerListAdapter.ListItemClickListener{

    private RecyclerView beerList_rv;
    private Dialog loadingDialog;
    private ImageView noConnection_iv;
    private BeerListAdapter beerListAdapter;
    private Context context;
    private SharedPreferences sharedPrefSettings;
    private List<Beer> beerList;

    private int page = 1;
    private int get_mode;
    private boolean isLoading = false;
    private boolean goToDetails = false;

    //region Lifecycle Methods

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);
        SetupViews();
        SetupComponents();

        if(savedInstanceState == null) {

            //Get beers when create activity
            if(get_mode == Constants.API_MODE)
                GetBeerListFromAPI();
            else
                GetFavoriteBeers();
        }
        else {
            beerList = (ArrayList<Beer>) savedInstanceState.getSerializable(Constants.RV_ITENS_SAVED);
            beerListAdapter = new BeerListAdapter(this, beerList, this);
            beerList_rv.setAdapter(beerListAdapter);
        }
    }

    @Override
    protected void onResume() {
        //check if a favorite beer was removed from favorites
        //if yes reload activity
        if(goToDetails && get_mode == Constants.FAVORITES_MODE)
            GetFavoriteBeers();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(Constants.RV_ITENS_SAVED, (Serializable) beerList);
        super.onSaveInstanceState(outState);
    }

    //endregion

    //region Setup Views and Components

    private void SetupViews(){
        noConnection_iv = findViewById(R.id.no_connection_iv);
        beerList_rv = findViewById(R.id.beer_list_rv);
        beerList_rv.setLayoutManager(new LinearLayoutManager(this));
        //beerList_rv.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        beerList_rv.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    private void SetupComponents(){
        context = this;
        sharedPrefSettings = getSharedPreferences(Constants.SHARED_PREF_REF, 0);
        get_mode = getSettingGetMode();

        beerList = new ArrayList<>();
        beerListAdapter = new BeerListAdapter(this, beerList, this);
        beerList_rv.setAdapter(beerListAdapter);
    }

    //endregion

    //region Menu

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
                GetBeerListFromAPI();
                return true;
            case R.id.get_from_api_btn:
                if(get_mode == Constants.FAVORITES_MODE){
                    beerListAdapter.clear();
                    page = 1;
                }
                GetBeerListFromAPI();
                return true;
            case R.id.get_favorites_btn:
                GetFavoriteBeers();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion

    //region Get Beer List from Punk API

    // Punk API call
    private void GetBeerListFromAPI(){
        setTitle(getString(R.string.beers_from_punk_api));
        get_mode = Constants.API_MODE;
        setSettingGetMode(Constants.API_MODE);

        if(isOnline()) {
            StartLoading();
            BeerService service = RetrofitInitializer.getRetrofitInstance().create(BeerService.class);
            Call<List<Beer>> call = service.getBeers(page, Constants.PER_PAGE);

            call.enqueue(new Callback<List<Beer>>() {
                @Override
                public void onResponse(@NonNull Call<List<Beer>> call, @NonNull Response<List<Beer>> response) {
                    if (!Objects.requireNonNull(response.body()).isEmpty()) {
                        beerList.addAll(response.body());
                        beerListAdapter.notifyDataSetChanged();
                        RVScroolToFirstLoaded();
                        page++;
                    } else {
                        Snackbar.make(findViewById(R.id.beer_list_root_layout), getString(R.string.end_of_beers), Snackbar.LENGTH_SHORT).show();
                    }
                    FinishLoading();
                }

                @Override
                public void onFailure(@NonNull Call<List<Beer>> call, @NonNull Throwable t) {
                    FinishLoading();
                    Snackbar.make(findViewById(R.id.details_root_layout), getString(R.string.could_not_load_beer_list), Snackbar.LENGTH_SHORT).show();
                }
            });
        }
        else{
            ShowNoConnectionUI();
            invalidateOptionsMenu();
        }
    }

    //After load beers (from Punk API) the app scrolls to the first new loaded beer position
    private void RVScroolToFirstLoaded(){
        RecyclerView.SmoothScroller smoothScroller = new
                LinearSmoothScroller(context) {
                    @Override protected int getVerticalSnapPreference() {
                        return LinearSmoothScroller.SNAP_TO_START;
                    }
                };
        smoothScroller.setTargetPosition((page-1) * Constants.PER_PAGE);
        Objects.requireNonNull(beerList_rv.getLayoutManager()).startSmoothScroll(smoothScroller);
    }

    //endregion

    //region Get Favorite Beers from Database

    private void GetFavoriteBeers(){
        setTitle(getString(R.string.favorite_beers));
        FavoriteBeersTask fTask = new FavoriteBeersTask(context);
        fTask.execute();
        get_mode = Constants.FAVORITES_MODE;
        setSettingGetMode(Constants.FAVORITES_MODE);
    }

    //Async task to access the database
    @SuppressLint("StaticFieldLeak")
    class FavoriteBeersTask extends AsyncTask<Void, Void, List<Beer>> {

        private final Context mContext;

        FavoriteBeersTask(Context context) {
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
                Snackbar.make(findViewById(R.id.beer_list_root_layout), getString(R.string.favorites_empty), Snackbar.LENGTH_SHORT).show();

            FinishLoading();
            beerListAdapter.notifyDataSetChanged();
        }
    }

    //endregion

    //region onListItemClick to Start Details Activity

    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(!isLoading) {
            Intent i = new Intent(context, BeerDetailsActivity.class);
            i.putExtra(Constants.GET_MODE_PREF, get_mode);
            i.putExtra(Constants.BEER_TAG, beerList.get(clickedItemIndex));
            goToDetails = true;
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    //endregion

    //region SharedPreferences

    private int getSettingGetMode() {
        try {
            return sharedPrefSettings.getInt(Constants.GET_MODE_PREF, Constants.API_MODE);
        } catch (Exception e) {
            return Constants.API_MODE;
        }
    }

    private void setSettingGetMode(int value) {
        try {
            SharedPreferences.Editor editor = sharedPrefSettings.edit();
            editor.putInt(Constants.GET_MODE_PREF, value);
            editor.apply();
        } catch (Exception ignored) {

        }
    }

    //endregion

    //region Loading Methods

    private void StartLoading(){
        HideNoConnectionUI();
        ShowLoadingDialog();
        isLoading = true;
        invalidateOptionsMenu();
    }

    private void FinishLoading(){
        DismissLoadingDialog();
        isLoading = false;
        invalidateOptionsMenu();
    }

    private void ShowLoadingDialog(){
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.dialog_loading);
        Objects.requireNonNull(loadingDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ProgressBar loading_pg = loadingDialog.findViewById(R.id.loading_pb);
        loading_pg.setIndeterminate(true);
        loading_pg.setVisibility(View.VISIBLE);
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    private void DismissLoadingDialog(){
        if(loadingDialog.isShowing()) {
            ProgressBar loading_pg = loadingDialog.findViewById(R.id.loading_pb);
            loading_pg.setIndeterminate(false);
            loading_pg.setVisibility(View.GONE);
            loadingDialog.dismiss();
        }
    }

    //endregion

    //region Verify connection

    private boolean isOnline() {
        try {

            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = Objects.requireNonNull(cm).getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    private void ShowNoConnectionUI(){
        if(beerList.isEmpty())
            noConnection_iv.setVisibility(View.VISIBLE);
        Snackbar.make(findViewById(R.id.beer_list_root_layout), getString(R.string.connection_error), Snackbar.LENGTH_SHORT).show();
    }

    private void HideNoConnectionUI(){
        if(noConnection_iv.getVisibility() == View.VISIBLE)
            noConnection_iv.setVisibility(View.GONE);
    }

    //endregion

}
