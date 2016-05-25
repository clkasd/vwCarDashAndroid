package com.developwear.vwcardash;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Aykut on 21/05/16.
 */
public class SharedPrefsHelper {

    public static void addString(Context con, String key,String data)
    {
        SharedPreferences sharedPref = con.getSharedPreferences("vwcardash", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key,data);
        editor.commit();
    }
    public static String getString(Context con, String key)
    {
        SharedPreferences sharedPref = con.getSharedPreferences("vwcardash", Context.MODE_PRIVATE);
        String defaultValue ="89,0";
        return sharedPref.getString(key,defaultValue);
    }
}
