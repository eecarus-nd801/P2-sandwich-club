package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json) {
        if (json == null)
            return null;

        try {
            JSONObject item = new JSONObject(json);
            return parseSandwich(item);
        } catch (JSONException e) {
            Log.e(TAG, "Unable to parse JSON String.", e);
            e.printStackTrace();
        }
        return null;
    }

    private static Sandwich parseSandwich(JSONObject item) throws JSONException {
        // set up variables for working with
        String mainName = null;
        List<String> alsoKnownAs = new ArrayList<>();
        String placeOfOrigin = null;
        String description = null;
        String image = null;
        List<String> ingredients = new ArrayList<>();

        // parse the name subgroup
        JSONObject name = item.getJSONObject("name");
        if (name != null) {
            mainName = parseString(name, "mainName");
            alsoKnownAs = parseStringArray(name, "alsoKnownAs");
        }

        // parse the rest
        placeOfOrigin = parseString(item, "placeOfOrigin");
        description = parseString(item, "description");
        image = parseString(item, "image");
        ingredients = parseStringArray(item, "ingredients");

        // return the parsed Object
        return new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, image, ingredients);
    }

    private static String parseString(final JSONObject jsonObject, final String stringPropertyName) {
        if (jsonObject == null || !jsonObject.has(stringPropertyName))
            return null;

        return jsonObject.optString(stringPropertyName, null);
    }

    private static List<String> parseStringArray(final JSONObject jsonObject, final String arrayPropertyName) throws JSONException {
        if (jsonObject == null || !jsonObject.has(arrayPropertyName))
            return null;

        JSONArray itemArray = jsonObject.optJSONArray(arrayPropertyName);
        if (itemArray != null && itemArray.length() > 0) {
            List<String> resultList = new ArrayList<>(itemArray.length());
            for (int index = 0; index < itemArray.length(); index++)
                resultList.add(itemArray.getString(index));
            return resultList;
        } else {
            return Collections.emptyList();
        }
    }

}
