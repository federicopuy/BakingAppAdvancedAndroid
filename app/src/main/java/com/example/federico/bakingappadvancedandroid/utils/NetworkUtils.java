package com.example.federico.bakingappadvancedandroid.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public final class NetworkUtils {

    private static final String URLBASE = "https://d17h27t6h515a5.cloudfront.net";

    private static final String APIURL = "/topher/2017/May/59121517_baking/";

    public static final String COMPLETEURL = URLBASE + APIURL;

    public static final String RECIPES_URL = "baking.json";

    public static boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
