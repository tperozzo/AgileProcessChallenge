package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.data.FavoriteBeersContract;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.retrofit.RetrofitInitializer;
import com.tallesperozzo.agileprocesschallenge.retrofit.service.BeerService;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;
import com.tallesperozzo.agileprocesschallenge.view.adapters.HopsListAdapter;
import com.tallesperozzo.agileprocesschallenge.view.adapters.MaltListAdapter;
import com.tallesperozzo.agileprocesschallenge.view.adapters.MashTempListAdapter;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

/*
 * BeerDetailsActivity
 *
 * In this activity, the user can see the beer details and add/remove the beer to favorites.
 *
 * Created by Talles Perozzo
 */

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;
    private Context context;
    private Dialog loadingDialog;
    private ProgressBar imageUrl_pb;

    private boolean isFavorite = false;
    private boolean isFavoriteAndLoaded = false;
    private boolean canCreateMenu = false;
    private int get_mode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details);

        context = this;
        final Intent intent = getIntent();

        get_mode = intent.getIntExtra(Constants.GET_MODE_PREF, 0);

        beer = (Beer) intent.getSerializableExtra(Constants.BEER_TAG);

        if(get_mode == Constants.API_MODE) {
            GetBeerFromFavorites();
        }

        else{
            GetBeerByID();
        }

        invalidateOptionsMenu();
    }

    //region GetBeerFromFavorites
    // Get beer from favorites (or not) to setup (or not) favorites button

    private void GetBeerFromFavorites(){
        if (beer != null) {
            SetupViews();
        }

        Cursor result = context.getContentResolver().query(
                FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI,
                null,
                FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ID_BEER + "=?",
                new String[]{String.valueOf(beer.getId())},
                null
        );

        if (result != null) {
            if (result.getCount() > 0)
                isFavorite = true;
        } else
            isFavorite = false;

        Objects.requireNonNull(result).close();
        canCreateMenu = true;
        //invalidateOptionsMenu();
    }

    //endregion

    //region GetBeerByID from Punk API

    private void GetBeerByID(){
        final LinearLayout root_ll = findViewById(R.id.details_beer_layout);
        root_ll.setVisibility(View.GONE);
        isFavorite = true;
        StartLoading();
        BeerService service = RetrofitInitializer.getRetrofitInstance().create(BeerService.class);
        Call<List<Beer>> call = service.getBeerById(beer.getId());

        call.enqueue(new retrofit2.Callback<List<Beer>>() {
            @Override
            public void onResponse(@NonNull Call<List<Beer>> call, @NonNull  Response<List<Beer>> response) {
                if(!Objects.requireNonNull(response.body()).isEmpty()) {
                    beer = response.body().get(0);
                    FinishLoading();
                    root_ll.setVisibility(View.VISIBLE);
                    SetupViews();
                    isFavoriteAndLoaded = true;
                }
                else{
                    FinishLoading();
                    Snackbar.make(findViewById(R.id.beer_list_root_layout), getString(R.string.could_not_load_beer_info), Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Beer>> call, @NonNull Throwable t) {
                FinishLoading();
                Snackbar.make(findViewById(R.id.details_root_layout), getString(R.string.could_not_load_beer_info), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    //endregion

    //region SetupViews
    // Load interface after getting beer

    private void SetupViews(){
        SetupGeralInfo();
        SetupSpecialInfo();
        SetupTechnicalInfo();
        SetupCookingInfo();
    }

    private void SetupGeralInfo(){
        ImageView imageUrl_iv = findViewById(R.id.image_url_iv);
        imageUrl_pb = findViewById(R.id.image_url_pb);
        TextView name_tv = findViewById(R.id.name_tv);
        TextView tagline_tv = findViewById(R.id.tagline_tv);
        TextView abv_tv = findViewById(R.id.abv_tv);
        TextView ibu_tv = findViewById(R.id.ibu_tv);
        TextView firstBrewed_tv = findViewById(R.id.first_brewed_tv);
        TextView contributed_by = findViewById(R.id.contributed_by_tv);

        Picasso.with(context)
                .load(beer.getImage_url())
                .into(imageUrl_iv, new Callback() {
                    @Override
                    public void onSuccess() {
                        imageUrl_pb.setIndeterminate(false);
                        imageUrl_pb.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        imageUrl_pb.setIndeterminate(false);
                        imageUrl_pb.setVisibility(View.GONE);
                    }
                });

        name_tv.setText(beer.getName());
        tagline_tv.setText(beer.getTagline());
        abv_tv.setText(getString(R.string.abv, String.valueOf(beer.getAbv())));
        ibu_tv.setText(getString(R.string.ibu, String.valueOf(beer.getIbu())));
        firstBrewed_tv.setText(beer.getFirst_brewed());
        contributed_by.setText(getString(R.string.by, beer.getContributed_by()));
    }

    private void SetupSpecialInfo(){
        TextView description_tv = findViewById(R.id.description_tv);
        TextView foodPairing_1_tv = findViewById(R.id.food_pairing_1_tv);
        TextView foodPairing_2_tv = findViewById(R.id.food_pairing_2_tv);
        TextView foodPairing_3_tv = findViewById(R.id.food_pairing_3_tv);
        TextView brewersTips_tv = findViewById(R.id.brewers_tips_tv);

        description_tv.setText(beer.getDescription());
        foodPairing_1_tv.setText(getString(R.string.food_pairing_placeholder, beer.getFood_pairing().get(0)));
        foodPairing_2_tv.setText(getString(R.string.food_pairing_placeholder, beer.getFood_pairing().get(1)));
        foodPairing_3_tv.setText(getString(R.string.food_pairing_placeholder, beer.getFood_pairing().get(2)));
        brewersTips_tv.setText(beer.getBrewers_tips());
    }

    private void SetupTechnicalInfo(){
        TextView targetFg_tv = findViewById(R.id.target_fg_tv);
        TextView targetOg_tv = findViewById(R.id.target_og_tv);
        TextView ebc_tv = findViewById(R.id.ebc_tv);
        TextView srm_tv = findViewById(R.id.srm_tv);
        TextView ph_tv = findViewById(R.id.ph_tv);
        TextView attenuationLevel_tv = findViewById(R.id.attenuation_level_tv);
        TextView volume_tv = findViewById(R.id.volume_tv);
        TextView boilVolume_tv = findViewById(R.id.boil_volume_tv);

        targetFg_tv.setText(getString(R.string.target_fg, String.valueOf(beer.getTarget_fg())));
        targetOg_tv.setText(getString(R.string.target_og, String.valueOf(beer.getTarget_og())));
        ebc_tv.setText(getString(R.string.ebc, String.valueOf(beer.getEbc())));
        srm_tv.setText(getString(R.string.srm, String.valueOf(beer.getSrm())));
        ph_tv.setText(getString(R.string.ph, String.valueOf(beer.getPh())));
        attenuationLevel_tv.setText(getString(R.string.attenuation_level, String.valueOf(beer.getAttenuation_level())));
        volume_tv.setText(getString(R.string.volume, String.valueOf(beer.getVolume().getValue()) + " " + beer.getVolume().getUnit()));
        boilVolume_tv.setText(getString(R.string.boil_volume, String.valueOf(beer.getSrm()) + " " + beer.getBoil_volume().getUnit()));
    }

    private void SetupCookingInfo(){
        RecyclerView malt_rv = findViewById(R.id.malt_rv);
        malt_rv.setLayoutManager(new LinearLayoutManager(context));
        MaltListAdapter maltListAdapter = new MaltListAdapter(context, beer.getIngredients().getMalt());
        malt_rv.setAdapter(maltListAdapter);

        RecyclerView hops_rv = findViewById(R.id.hops_rv);
        hops_rv.setLayoutManager(new LinearLayoutManager(context));
        HopsListAdapter hopsListAdapter = new HopsListAdapter(context, beer.getIngredients().getHops());
        hops_rv.setAdapter(hopsListAdapter);

        TextView yeast_tv = findViewById(R.id.yeast_tv);
        yeast_tv.setText(beer.getIngredients().getYeast());

        RecyclerView mashTemp_rv = findViewById(R.id.mash_temp_rv);
        mashTemp_rv.setLayoutManager(new LinearLayoutManager(context));
        MashTempListAdapter mashTempListAdapter = new MashTempListAdapter(context, beer.getMethod().getMash_temp());
        mashTemp_rv.setAdapter(mashTempListAdapter);

        TextView fermantation_tv = findViewById(R.id.fermantation_tv);
        fermantation_tv.setText(getString(R.string.measure_placeholder, String.valueOf(beer.getMethod().getFermentation().getTemp().getValue()), beer.getMethod().getFermentation().getTemp().getUnit()));
        TextView twist_tv = findViewById(R.id.twist_tv);
        TextView twistLabel_tv = findViewById(R.id.twist_label_tv);
        if(!TextUtils.isEmpty(beer.getMethod().getTwist()))
            twist_tv.setText(beer.getMethod().getTwist());
        else{
            twist_tv.setVisibility(View.GONE);
            twistLabel_tv.setVisibility(View.GONE);
        }
    }

    //endregion

    //region Menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(canCreateMenu) {
            if (isFavorite)
                getMenuInflater().inflate(R.menu.details_favorite_menu, menu);
            else
                getMenuInflater().inflate(R.menu.details_not_favorite_menu, menu);

            if(get_mode == Constants.FAVORITES_MODE) {
                if (isFavoriteAndLoaded) {
                    menu.getItem(0).setVisible(false);
                    menu.getItem(1).setVisible(true);
                } else {
                    menu.getItem(0).setVisible(true);
                    menu.getItem(1).setVisible(false);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.not_favorite:
                //become isFavorite
                if(insert()){
                    isFavorite = true;
                    invalidateOptionsMenu();
                    Snackbar.make(findViewById(R.id.details_root_layout), "Beer added to favorites", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    isFavorite = false;
                    invalidateOptionsMenu();
                    Snackbar.make(findViewById(R.id.details_root_layout), "Beer can not be added to favorites", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.favorite:
                //become not isFavorite
                if(delete()){
                    isFavorite = false;
                    invalidateOptionsMenu();
                    Snackbar.make(findViewById(R.id.details_root_layout), "Beer removed from favorites", Snackbar.LENGTH_SHORT).show();
                }
                else{
                    isFavorite = true;
                    invalidateOptionsMenu();
                    Snackbar.make(findViewById(R.id.details_root_layout), "Beer can not be removed from favorites", Snackbar.LENGTH_SHORT).show();
                }
                return true;

            case R.id.refresh:
                GetBeerByID();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //endregion

    //region ContentProvider Methods

    private boolean insert(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ID_BEER, beer.getId());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_NAME, beer.getName());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_TAGLINE, beer.getTagline());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ABV, beer.getAbv());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_IMAGE_URL, beer.getImage_url());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_FIRST_BREWED, beer.getFirst_brewed());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_CONTRIBUTED_BY, beer.getContributed_by());

        Uri uri = getContentResolver().insert(FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI, contentValues);

        return uri != null;
    }

    private boolean delete(){
        Uri uri = FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(beer.getId())).build();
        return getContentResolver().delete(uri, null, null) > 0;
    }

    //endregion

    //region Loading Methods

    private void StartLoading(){
        ShowLoadingDialog();
    }

    private void FinishLoading(){
        canCreateMenu = true;
        invalidateOptionsMenu();
        DismissLoadingDialog();
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
}
