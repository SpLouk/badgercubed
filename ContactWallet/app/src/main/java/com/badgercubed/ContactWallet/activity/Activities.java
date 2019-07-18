package com.badgercubed.ContactWallet.activity;

import android.content.Context;
import android.content.Intent;

import com.badgercubed.ContactWallet.model.ProtectionLevel;

/* Used to launch activities from anywhere in the app */
public class Activities {
    public static final String INTENT_FOLLOWING_USER_UID = ".intent.following_user_uid";
    public static final String INTENT_REL_PROT_LEVEL = ".intent.relationship_protection_level";

    public static void startWelcomeActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startRegisterActivity(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void startContactDetailsActivity(Context context, String userUid, ProtectionLevel protectionLevel) {
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtra(INTENT_FOLLOWING_USER_UID, userUid);
        intent.putExtra(INTENT_REL_PROT_LEVEL, protectionLevel);
        context.startActivity(intent);
    }

    public static void startNavActivity(Context context) {
        Intent intent = new Intent(context, NavActivity.class);
        context.startActivity(intent);
    }
}
