package com.badgercubed.ContactWallet.activity;

import android.content.Context;
import android.content.Intent;

/* Used to launch activities from anywhere in the app */
public class Activities {
    public static final String INTENT_USER_UID = ".intent.user_uid";

    public static void startWelcomeActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void startContactDetailsActivity(Context context, String userUid) {
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtra(INTENT_USER_UID, userUid);
        context.startActivity(intent);
    }

    public static void startNavActivity(Context context) {
        Intent intent = new Intent(context, NavActivity.class);
        context.startActivity(intent);
    }
}
