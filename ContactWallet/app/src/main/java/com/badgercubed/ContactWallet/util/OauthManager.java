package com.badgercubed.ContactWallet.util;

import android.app.Activity;

import com.badgercubed.ContactWallet.model.Service;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

public class OauthManager {
    private static OauthManager instance = null;
    OAuthProvider.Builder provider;

    public static OauthManager getInstance() {
        if (instance == null) {
            instance = new OauthManager();
        }
        return instance;
    }

    public Task<AuthResult> verifyService(Activity a, Service service) {
        FirebaseUser user = FBManager.getInstance().getCurrentFBUser();
        switch (service) {
            case TWITTER:
                provider = OAuthProvider.newBuilder("twitter.com");
                break;
            default:
                provider = null;
        }
        return user.startActivityForLinkWithProvider(a, provider.build());
    }
}
