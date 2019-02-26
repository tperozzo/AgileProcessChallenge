package com.tallesperozzo.agileprocesschallenge.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tallesperozzo.agileprocesschallenge.R;
import com.tallesperozzo.agileprocesschallenge.model.Beer;
import com.tallesperozzo.agileprocesschallenge.utils.Constants;

public class BeerDetailsActivity extends AppCompatActivity {

    private Beer beer;
    private Context context;

    ImageView imageUrl_iv;
    ProgressBar imageUrl_pb;
    TextView name_tv;
    TextView tagline_tv;
    TextView abv_tv;
    TextView ibu_tv;
    TextView first_brewed_tv;

    TextView description_tv;
    TextView food_pairing_1_tv;
    TextView food_pairing_2_tv;
    TextView food_pairing_3_tv;
    TextView brewers_tips_tv;

    TextView target_fg_tv;
    TextView target_og_tv;
    TextView ebc_tv;
    TextView srm_tv;
    TextView ph_tv;
    TextView attenuation_level_tv;
    TextView volume_tv;
    TextView boil_volume_tv;

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
    }

    private void SetupViews(){
        SetupGeralInfo();
        SetupSpecialInfo();
        SetupTechnicalInfo();
    }

    public void SetupGeralInfo(){
        imageUrl_iv = findViewById(R.id.image_url_iv);
        imageUrl_pb = findViewById(R.id.image_url_pb);
        name_tv = findViewById(R.id.name_tv);
        tagline_tv = findViewById(R.id.tagline_tv);
        abv_tv = findViewById(R.id.abv_tv);
        ibu_tv = findViewById(R.id.ibu_tv);
        first_brewed_tv = findViewById(R.id.first_brewed_tv);

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
        first_brewed_tv.setText(beer.getFirst_brewed());
    }

    public void SetupSpecialInfo(){
        description_tv = findViewById(R.id.description_tv);
        food_pairing_1_tv = findViewById(R.id.food_pairing_1_tv);
        food_pairing_2_tv = findViewById(R.id.food_pairing_2_tv);
        food_pairing_3_tv = findViewById(R.id.food_pairing_3_tv);
        brewers_tips_tv = findViewById(R.id.brewers_tips_tv);

        description_tv.setText(beer.getDescription());
        food_pairing_1_tv.setText(" - " + beer.getFood_pairing().get(0));
        food_pairing_2_tv.setText(" - " + beer.getFood_pairing().get(1));
        food_pairing_3_tv.setText(" - " + beer.getFood_pairing().get(2));
        brewers_tips_tv.setText(beer.getBrewers_tips());
    }

    public void SetupTechnicalInfo(){
        target_fg_tv = findViewById(R.id.target_fg_tv);
        target_og_tv = findViewById(R.id.target_og_tv);
        ebc_tv = findViewById(R.id.ebc_tv);
        srm_tv = findViewById(R.id.srm_tv);
        ph_tv = findViewById(R.id.ph_tv);
        attenuation_level_tv = findViewById(R.id.attenuation_level_tv);
        volume_tv = findViewById(R.id.volume_tv);
        boil_volume_tv = findViewById(R.id.boil_volume_tv);

        target_fg_tv.setText("Target FG: " + String.valueOf(beer.getTarget_fg()));
        target_og_tv.setText("Target OG: " + String.valueOf(beer.getTarget_og()));
        ebc_tv.setText("EBC: " + String.valueOf(beer.getEbc()));
        srm_tv.setText("SRM: " + String.valueOf(beer.getSrm()));
        ph_tv.setText("PH: " + String.valueOf(beer.getPh()));
        attenuation_level_tv.setText("Attenuation Level: " + String.valueOf(beer.getAttenuation_level()));
        volume_tv.setText("Volume: " + String.valueOf(beer.getVolume().getValue()) + " " + beer.getVolume().getUnit());
        boil_volume_tv.setText("Boil Volume: " + String.valueOf(beer.getSrm()) + " " + beer.getBoil_volume().getUnit());
    }
}
