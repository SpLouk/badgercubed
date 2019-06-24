package activity;

import android.content.Context;
import android.content.Intent;

import java.util.List;

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

    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startProfileActivity(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }

    public static void startListContactsActivity(Context context) {
        Intent intent = new Intent(context, ListContactsActivity.class);
        context.startActivity(intent);
    }

    public static void startUserContactsActivity(Context context, String userUid) {
        Intent intent = new Intent(context, UserContactsActivity.class);
        intent.putExtra(INTENT_USER_UID, userUid);
        context.startActivity(intent);
    }
}
