package activity;

import android.content.Context;
import android.content.Intent;

/* Used to launch activities from anywhere in the app */
public class Activities {
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

    public static void startUserContactsActivity(Context context) {
        Intent intent = new Intent(context, UserContactsActivity.class);
        context.startActivity(intent);
    }
}
