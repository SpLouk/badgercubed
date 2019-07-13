package com.badgercubed.ContactWallet.util;

import android.app.Activity;
import android.app.ProgressDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

public class TwitterManager {
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");

    public void verifyTwitter(Activity a) {
        FirebaseUser user = FBManager.getInstance().getCurrentFBUser();
        user.startActivityForLinkWithProvider(a, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                final ProgressDialog progressDialog = new ProgressDialog(a); // TODO: replace w progress bar
                                progressDialog.setMessage("Success!");
                                progressDialog.show();
                            }
                        }
                );
    }
}
