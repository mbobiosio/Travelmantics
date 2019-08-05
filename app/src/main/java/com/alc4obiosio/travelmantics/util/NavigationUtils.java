package com.alc4obiosio.travelmantics.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.alc4obiosio.travelmantics.ui.activity.CreateDealActivity;
import com.alc4obiosio.travelmantics.ui.activity.LoginActivity;
import com.alc4obiosio.travelmantics.ui.activity.MainActivity;
import com.alc4obiosio.travelmantics.ui.activity.SignUpActivity;
import com.alc4obiosio.travelmantics.ui.activity.WelcomeActivity;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class NavigationUtils {

    public static void navigateWelcome(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        context.startActivity(intent);
    }

    public static void navigateMain(Activity context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        context.finish();
    }

    public static void navigateMainFromLogin(Activity activity, String string) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra("name", string);
        activity.startActivity(intent);
        activity.finish();
    }

    public static void navigateLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void navigateSignUp(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    public static void navigateCreatePlace(Context context) {
        context.startActivity(new Intent(context, CreateDealActivity.class));
    }
}
