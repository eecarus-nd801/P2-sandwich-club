package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private ImageView mImageIv;
    private TextView mAlsoKnownAsTv;
    private TextView mDescriptionTv;
    private TextView mIngredientsTv;
    private TextView mPlaceOfOriginTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeViewReferences();

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mImageIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void initializeViewReferences() {
        mImageIv = findViewById(R.id.image_iv);
        mAlsoKnownAsTv = findViewById(R.id.also_known_tv);
        mDescriptionTv = findViewById(R.id.description_tv);
        mIngredientsTv = findViewById(R.id.ingredients_tv);
        mPlaceOfOriginTv = findViewById(R.id.origin_tv);
    }

    private void populateUI(Sandwich sandwich) {
        mAlsoKnownAsTv.setText(joinListWithNewlineSeparator(sandwich.getAlsoKnownAs()));
        mDescriptionTv.setText(defaultString(sandwich.getDescription()));
        mIngredientsTv.setText(joinListWithNewlineSeparator(sandwich.getIngredients()));
        mPlaceOfOriginTv.setText(defaultString(sandwich.getPlaceOfOrigin()));
    }

    private String joinListWithNewlineSeparator(List<String> listOfStrings) {
        if (listOfStrings == null || listOfStrings.isEmpty())
            return "";
        return TextUtils.join("\n", listOfStrings);
    }

    private String defaultString(String original) {
        return TextUtils.isEmpty(original) ? "" : original;
    }
}
