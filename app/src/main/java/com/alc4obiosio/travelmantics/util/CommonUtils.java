package com.alc4obiosio.travelmantics.util;

import android.content.Context;
import android.os.Build;
import android.widget.ImageView;

import com.alc4obiosio.travelmantics.GlideApp;
import com.bumptech.glide.signature.ObjectKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mbuodile Obiosio on Aug 02,2019
 * https://twitter.com/cazewonder
 * Nigeria.
 */
public class CommonUtils {

    public static boolean checkStringNotEmpty(String str) {
        return (str != null && !str.equals(""));
    }

    public static void loadGlideImage(Context context, ImageView imageView, String url) {
        if (CommonUtils.checkStringNotEmpty(url)) {
            GlideApp.with(context.getApplicationContext())
                    .load(url)
                    .signature(new ObjectKey(url))
                    .dontAnimate()
                    .into(imageView);
        }
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public  static String getDate() {
        SimpleDateFormat formatD = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        formatD.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatD.format(new Date());
    }
}
