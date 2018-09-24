package com.narmware.vvmcoordinator.support;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by comp16 on 12/19/2017.
 */

public class SharedPreferencesHelper {

    private static final String IS_LOGIN="login";
    private static final String IS_PAID="isPaid";
    private static final String IS_UNPAID="isUnpaid";
    private static final String IS_ALL="isAll";
    private static final String IS_CONTACTED="isContacted";
    private static final String IS_NOT_CONTACTED="isNotContacted";



    public static void setIsLogin(boolean login, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_LOGIN,login);
        edit.commit();
    }

    public static boolean getIsLogin(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean login=pref.getBoolean(IS_LOGIN,false);
        return login;
    }

    public static void setIsPaid(boolean filter, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_PAID,filter);
        edit.commit();
    }

    public static boolean getIsPaid(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter=pref.getBoolean(IS_PAID,false);
        return filter;
    }

    public static void setIsUnpaid(boolean filter, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_UNPAID,filter);
        edit.commit();
    }

    public static boolean getIsUnpaid(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter=pref.getBoolean(IS_UNPAID,false);
        return filter;
    }

    public static void setIsAll(boolean filter, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_ALL,filter);
        edit.commit();
    }

    public static boolean getIsAll(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter=pref.getBoolean(IS_ALL,false);
        return filter;
    }

    public static void setIsContacted(boolean filter, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_CONTACTED,filter);
        edit.commit();
    }

    public static boolean getIsContacted(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter=pref.getBoolean(IS_CONTACTED,false);
        return filter;
    }

    public static void setIsNotContacted(boolean filter, Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit=pref.edit();
        edit.putBoolean(IS_NOT_CONTACTED,filter);
        edit.commit();
    }

    public static boolean getIsNotContacted(Context context)
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(context);
        boolean filter=pref.getBoolean(IS_NOT_CONTACTED,false);
        return filter;
    }
}
