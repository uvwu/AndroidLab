package com.example.voca;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

public class DarkModeUtil {
    public static final String LIGHT_MODE="light";
    public static final String DARK_MODE="dark";
    public static final String DEFAULT_MODE="default";

    public static void applyTheme(String themeColor){
        switch (themeColor){
            case LIGHT_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;

            case DARK_MODE:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
        }
    }
    public static void modSave(Context context,String selectedMode){
        SharedPreferences sp;
        sp=context.getSharedPreferences("mode",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("mode",selectedMode);
        editor.commit();
    }
    public static String modLoad(Context context){
        SharedPreferences sp;
        sp=context.getSharedPreferences("mode",context.MODE_PRIVATE);
        String loadMode=sp.getString("mode","light");
        return loadMode;
    }
}
