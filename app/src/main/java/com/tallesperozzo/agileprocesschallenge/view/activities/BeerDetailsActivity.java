package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.data.FavoriteBeersContract;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;
import com.tallesperozzo.agileprocesschallenge.view.adapters.HopsListAdapter;
import com.tallesperozzo.agileprocesschallenge.view.adapters.MaltListAdapter;
import com.tallesperozzo.agileprocesschallenge.view.adapters.MashTempListAdapter;

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;
    private Context context;
    private boolean isFavorite = false;

    ImageView imageUrl_iv;
    ProgressBar imageUrl_pb;
    TextView name_tv;
    TextView tagline_tv;
    TextView abv_tv;
    TextView ibu_tv;
    TextView firstBrewed_tv;
    TextView contributed_by;

    TextView description_tv;
    TextView foodPairing_1_tv;
    TextView foodPairing_2_tv;
    TextView foodPairing_3_tv;
    TextView brewersTips_tv;

    TextView targetFg_tv;
    TextView targetOg_tv;
    TextView ebc_tv;
    TextView srm_tv;
    TextView ph_tv;
    TextView attenuationLevel_tv;
    TextView volume_tv;
    TextView boilVolume_tv;

    RecyclerView malt_rv;
    MaltListAdapter maltListAdapter;
    RecyclerView hops_rv;
    HopsListAdapter hopsListAdapter;
    TextView yeast_tv;
    RecyclerView mashTemp_rv;
    MashTempListAdapter mashTempListAdapter;
    TextView fermatation_tv;
    TextView twist_tv;
    TextView twistLabel_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details);

        context = this;
        final Intent intent = getIntent();

        beer = (Beer) intent.getSerializableExtra(Constants.BEER_TAG);
        if(beer != null) {
            SetupViews();
        }

        Cursor result = context.getContentResolver().query(
                FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI,
                null,
                FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ID_BEER + "=?",
                new String[]{String.valueOf(beer.getId())},
                null
        );


        if(result != null) {
            if (result.getCount() > 0)
                isFavorite = true;
        }
        else
            isFavorite = false;

        invalidateOptionsMenu();
    }

    private void SetupViews(){
        SetupGeralInfo();
        SetupSpecialInfo();
        SetupTechnicalInfo();
        SetupCookingInfo();
    }

    public void SetupGeralInfo(){
        imageUrl_iv = findViewById(R.id.image_url_iv);
        imageUrl_pb = findViewById(R.id.image_url_pb);
        name_tv = findViewById(R.id.name_tv);
        tagline_tv = findViewById(R.id.tagline_tv);
        abv_tv = findViewById(R.id.abv_tv);
        ibu_tv = findViewById(R.id.ibu_tv);
        firstBrewed_tv = findViewById(R.id.first_brewed_tv);
        contributed_by = findViewById(R.id.contributed_by_tv);

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
        abv_tv.setText("ABV: " + String.valueOf(beer.getAbv()));
        ibu_tv.setText("IBU: " + String.valueOf(beer.getIbu()));
        firstBrewed_tv.setText(beer.getFirst_brewed());
        contributed_by.setText("By " + beer.getContributed_by());
    }

    public void SetupSpecialInfo(){
        description_tv = findViewById(R.id.description_tv);
        foodPairing_1_tv = findViewById(R.id.food_pairing_1_tv);
        foodPairing_2_tv = findViewById(R.id.food_pairing_2_tv);
        foodPairing_3_tv = findViewById(R.id.food_pairing_3_tv);
        brewersTips_tv = findViewById(R.id.brewers_tips_tv);

        description_tv.setText(beer.getDescription());
        foodPairing_1_tv.setText(beer.getFood_pairing().get(0) + ";");
        foodPairing_2_tv.setText(beer.getFood_pairing().get(1) + ";");
        foodPairing_3_tv.setText(beer.getFood_pairing().get(2) + ".");
        brewersTips_tv.setText(beer.getBrewers_tips());
    }

    public void SetupTechnicalInfo(){
        targetFg_tv = findViewById(R.id.target_fg_tv);
        targetOg_tv = findViewById(R.id.target_og_tv);
        ebc_tv = findViewById(R.id.ebc_tv);
        srm_tv = findViewById(R.id.srm_tv);
        ph_tv = findViewById(R.id.ph_tv);
        attenuationLevel_tv = findViewById(R.id.attenuation_level_tv);
        volume_tv = findViewById(R.id.volume_tv);
        boilVolume_tv = findViewById(R.id.boil_volume_tv);

        targetFg_tv.setText("Target FG: " + String.valueOf(beer.getTarget_fg()));
        targetOg_tv.setText("Target OG: " + String.valueOf(beer.getTarget_og()));
        ebc_tv.setText("EBC: " + String.valueOf(beer.getEbc()));
        srm_tv.setText("SRM: " + String.valueOf(beer.getSrm()));
        ph_tv.setText("PH: " + String.valueOf(beer.getPh()));
        attenuationLevel_tv.setText("Attenuation Level: " + String.valueOf(beer.getAttenuation_level()));
        volume_tv.setText("Volume: " + String.valueOf(beer.getVolume().getValue()) + " " + beer.getVolume().getUnit());
        boilVolume_tv.setText("Boil Volume: " + String.valueOf(beer.getSrm()) + " " + beer.getBoil_volume().getUnit());
    }

    public void SetupCookingInfo(){
        malt_rv = findViewById(R.id.malt_rv);
        malt_rv.setLayoutManager(new LinearLayoutManager(context));
        maltListAdapter = new MaltListAdapter(context, beer.getIngredients().getMalt());
        malt_rv.setAdapter(maltListAdapter);

        hops_rv = findViewById(R.id.hops_rv);
        hops_rv.setLayoutManager(new LinearLayoutManager(context));
        hopsListAdapter = new HopsListAdapter(context, beer.getIngredients().getHops());
        hops_rv.setAdapter(hopsListAdapter);

        yeast_tv = findViewById(R.id.yeast_tv);
        yeast_tv.setText(beer.getIngredients().getYeast());

        mashTemp_rv = findViewById(R.id.mash_temp_rv);
        mashTemp_rv.setLayoutManager(new LinearLayoutManager(context));
        mashTempListAdapter = new MashTempListAdapter(context, beer.getMethod().getMash_temp());
        mashTemp_rv.setAdapter(mashTempListAdapter);

        fermatation_tv = findViewById(R.id.fermatation_tv);
        fermatation_tv.setText(beer.getMethod().getFermentation().getTemp().getValue() + " " + beer.getMethod().getFermentation().getTemp().getUnit());
        twist_tv = findViewById(R.id.twist_tv);
        twistLabel_tv = findViewById(R.id.twist_label_tv);
        if(!TextUtils.isEmpty(beer.getMethod().getTwist()))
            twist_tv.setText(beer.getMethod().getTwist());
        else{
            twist_tv.setVisibility(View.GONE);
            twistLabel_tv.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(isFavorite)
            getMenuInflater().inflate(R.menu.details_favorite_menu, menu);
        else
            getMenuInflater().inflate(R.menu.details_not_favorite_menu, menu);
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
                    //TODO
                }
                else{
                    //TODO
                }
                return true;
            case R.id.favorite:
                //become not isFavorite
                if(delete()){
                    isFavorite = false;
                    invalidateOptionsMenu();
                    //TODO
                }
                else{
                    //TODO
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean insert(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ID_BEER, beer.getId());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_NAME, beer.getName());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_TAGLINE, beer.getTagline());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_ABV, beer.getAbv());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_IMAGE_URL, beer.getImage_url());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_FIRST_BREWED, beer.getFirst_brewed());
        contentValues.put(FavoriteBeersContract.FavoriteBeersEntry.COLUMN_CONTRIBUTED_BY, beer.getContributed_by());

        Uri uri = getContentResolver().insert(FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI, contentValues);

        if(uri != null)
            return true;
        else
            return false;
    }

    public boolean delete(){
        Uri uri = FavoriteBeersContract.FavoriteBeersEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(beer.getId())).build();
        if(getContentResolver().delete(uri, null, null) > 0)
            return true;
        else
            return false;
    }
}
