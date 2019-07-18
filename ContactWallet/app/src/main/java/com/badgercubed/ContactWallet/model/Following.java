package com.badgercubed.ContactWallet.model;

import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

import java.util.UUID;

public class Following extends FBObject {
    public static final String m_collectionName = "followers";

    private String m_uid;
    private String m_followingUid;
    private String m_followerUid;
    private ProtectionLevel m_protectionLevel;

    public Following() {
    }

    public Following(String followerUid, String followingUid, ProtectionLevel protectionLevel) {
        m_uid = UUID.randomUUID().toString();
        m_followerUid = followerUid;
        m_followingUid = followingUid;
        m_protectionLevel = protectionLevel;
    }

    public String getUid() {
        return m_uid;
    }

    public void setUid(String uid) {
        m_uid = uid;
    }

    public String getFollowingUid() {
        return m_followingUid;
    }

    public void setFollowingUid(String followingUid) {
        this.m_followingUid = followingUid;
    }

    public String getFollowerUid() {
        return m_followerUid;
    }

    public void setFollowerUid(String followerUid) {
        this.m_followerUid = followerUid;
    }

    public ProtectionLevel getProtectionLevel() {
        return m_protectionLevel;
    }

    public void setProtectionLevel(ProtectionLevel level) {
        this.m_protectionLevel = level;
    }


    public void validate() throws Exception {
        if (m_protectionLevel == null) {
            throw new Exception("Protection level not set");
        }
        if (TextUtils.isEmpty(m_followerUid)) {
            throw new Exception("Follower uid is empty");
        }
        if (TextUtils.isEmpty(m_followingUid)) {
            throw new Exception("Following uid is empty");
        }
    }

    @Exclude
    public String getCollectionName() {
        return m_collectionName;
    }

    @Exclude
    public String getDocReference() {
        return m_uid;
    }
}
