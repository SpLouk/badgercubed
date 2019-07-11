package com.badgercubed.ContactWallet.model;

import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User extends FBObject {
    public static final String m_collectionName = "users";

    private String m_uid;
    private String m_email;
    private String m_name;
    private String m_phoneNum;
    private List<String> m_connectionIds;
    private List<String> m_followingIds;

    public User() {
        m_connectionIds = new ArrayList<>();
        m_followingIds = new ArrayList<>();
    }

    public User(String uid, String email, String name, String phoneNum,
                List<String> connectionIds, List<String> followingIds) {
        m_uid = uid;
        m_email = email;
        m_name = name;
        m_phoneNum = phoneNum;
        m_connectionIds = connectionIds;
        m_followingIds = followingIds;
    }

    public String getUid() {
        return m_uid;
    }

    public void setUid(String uid) {
        m_uid = uid;
    }

    public String getEmail() {
        return m_email;
    }

    public void setEmail(String email) {
        m_email = email;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getPhoneNum() {
        return m_phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        m_phoneNum = phoneNum;
    }

    public List<String> getConnectionIds() {
        return m_connectionIds;
    }

    public void setConnectionIds(List<String> connectionIds) {
        m_connectionIds = connectionIds;
    }

    public List<String> getFollowingIds() {
        return m_followingIds;
    }

    public void setFollowingIds(List<String> followingIds) {
        m_followingIds = followingIds;
    }

    public void validate() throws Exception {
        if (TextUtils.isEmpty(m_uid)) {
            throw new Exception("UID is empty");
        }
        if (TextUtils.isEmpty(m_email)) {
            throw new Exception("Email is empty");
        }
        if (TextUtils.isEmpty(m_name)) {
            throw new Exception("Name is empty");
        }
        if (TextUtils.isEmpty(m_phoneNum)) {
            throw new Exception("Phone # is empty");
        }
        if (m_connectionIds == null) {
            throw new Exception("Connection id's null");
        }
        if (m_followingIds == null) {
            throw new Exception("Following id's null");
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
