package com.badgercubed.ContactWallet.model;

import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

public class Following extends FBObject {
    public static final String mCollectionName = "followers";

    private String mUid;
    private String mFollowingUid;
    private String mFollowerUid;
    private String mLevel;

    public Following() {
    }

    public Following(String uid, String followerUid, String followingUid, String protectionLevel) {
        mUid = uid;
        mFollowerUid = followerUid;
        mFollowingUid = followingUid;
        mLevel = protectionLevel;
    }

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        mUid = uid;
    }

    public String getFollowingUid() {
        return mFollowingUid;
    }

    public void setFollowingUid(String mFollowingUid) {
        this.mFollowingUid = mFollowingUid;
    }

    public String getFollowerUid() {
        return mFollowerUid;
    }

    public void setFollowerUid(String mFollowerUid) {
        this.mFollowerUid = mFollowerUid;
    }

    public String getLevel() {
        return mLevel;
    }

    public void setLevel(String mLevel) {
        this.mLevel = mLevel;
    }


    public void validate() throws Exception {
        if (TextUtils.isEmpty(mLevel)) {
            throw new Exception("Level is empty");
        }
        if (TextUtils.isEmpty(mFollowerUid)) {
            throw new Exception("Follower uid is empty");
        }
        if (TextUtils.isEmpty(mFollowingUid)) {
            throw new Exception("Following uid is empty");
        }
    }

    @Exclude
    public String getCollectionName() {
        return mCollectionName;
    }

    @Exclude
    public String getDocReference() {
        return mUid;
    }
}
