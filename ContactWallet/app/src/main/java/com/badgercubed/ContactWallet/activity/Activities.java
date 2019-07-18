package com.badgercubed.ContactWallet.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

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

    public static void startContactDetailsActivity(Context context, String userUid, Integer protectionLevelId) {
        Intent intent = new Intent(context, ContactDetailsActivity.class);
        intent.putExtra(INTENT_FOLLOWING_USER_UID, userUid);
        intent.putExtra(INTENT_REL_PROT_LEVEL, protectionLevelId);
        context.startActivity(intent);
    }

    public static void startNavActivity(Context context) {
        Intent intent = new Intent(context, NavActivity.class);
        context.startActivity(intent);
    }

    public static void startBrowserActivity(Context context, String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void startEmailActivity(Context context, String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");
        emailIntent.setData(Uri.parse("mailto:" + email));
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    public static void startCallActivity(Context context, String phoneNum) {
        // get digits only in phone number
        phoneNum = phoneNum.replaceAll("\\D+","");
        if (phoneNum.length() != 9 && phoneNum.length() != 10 ) {
            Toast.makeText(context, "This phone number is invalid", Toast.LENGTH_LONG).show();
        }

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNum));

        context.startActivity(Intent.createChooser(callIntent, "Make Call"));
    }
}
