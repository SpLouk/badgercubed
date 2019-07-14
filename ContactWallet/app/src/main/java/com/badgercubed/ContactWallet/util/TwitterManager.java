package com.badgercubed.ContactWallet.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;

import java.util.Map;

public class TwitterManager {
    OAuthProvider.Builder provider = OAuthProvider.newBuilder("twitter.com");
    private static TwitterManager instance = null;

    public static TwitterManager getInstance() {
        if (instance == null) {
            instance = new TwitterManager();
        }
        return instance;
    }

    public void verifyTwitter(Activity a) {
        FirebaseUser user = FBManager.getInstance().getCurrentFBUser();
        user.startActivityForLinkWithProvider(a, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(a, "Success!", Toast.LENGTH_SHORT).show();
                                Map<String, Object> foo = authResult.getAdditionalUserInfo().getProfile();
                                Log.i("twitter stuff", foo.toString());
                            }
                        }
                )
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("twitter stuff", e.toString());
                        Toast.makeText(a, "Fail!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
