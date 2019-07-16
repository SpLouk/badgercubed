package com.badgercubed.ContactWallet.util;

import android.app.Activity;
import android.util.Log;

import com.badgercubed.ContactWallet.model.Service;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.List;

public class OauthManager {
    OAuthProvider.Builder provider;
    private static OauthManager instance = null;

    public static OauthManager getInstance() {
        if (instance == null) {
            instance = new OauthManager();
        }
        return instance;
    }

    public Task<AuthResult> verifyService(Activity a, Service service) {
        FirebaseUser user = AuthManager.getInstance().getAuthUser();
        switch (service) {
            case TWITTER:
                provider = OAuthProvider.newBuilder("twitter.com");
                break;
            case GITHUB:
                provider = OAuthProvider.newBuilder("github.com");
                break;
            default:
                provider = null;
        }

        List<? extends UserInfo> providers = AuthManager.getInstance().getAuthUser().getProviderData();
        for (int i = 0; i < providers.size(); i++) {
            boolean equals = providers.get(i).getProviderId() + "/" == service.getLink();
            if (service.getLink().equals(providers.get(i).getProviderId() + "/")) {
                return user.startActivityForReauthenticateWithProvider(a, provider.build());
            }
        }

        return user.startActivityForLinkWithProvider(a, provider.build());
    }
}
