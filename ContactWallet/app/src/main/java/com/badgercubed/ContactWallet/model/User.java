package com.badgercubed.ContactWallet.model;

import android.content.Context;
import android.text.TextUtils;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.util.App;
import com.google.firebase.firestore.Exclude;

import java.util.Random;

public class User extends FBObject {
    public static final String m_collectionName = "users";

    private String m_uid;
    private String m_email;
    private String m_name;

    private String m_publicHandle;
    private String m_protectedHandle;
    private String m_privateHandle;

    public User() {
    }

    public User(String uid, String email, String name) {
        m_uid = uid;
        m_email = email;
        m_name = name;

        if (TextUtils.isEmpty(m_email)) {
            throw new IllegalArgumentException("Email can't be empty");
        }

        Context context = App.getContext();
        String handleBase = context.getString(R.string.handle_base);
        m_publicHandle = handleBase + generateRandString(6);
        m_protectedHandle = handleBase + generateRandString(6);
        m_privateHandle = handleBase + generateRandString(6);
    }

    protected String generateRandString(int length) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder builder = new StringBuilder();
        Random rnd = new Random();
        while (builder.length() < length) {
            int index = (int) (rnd.nextFloat() * alphabet.length());
            builder.append(alphabet.charAt(index));
        }
        return builder.toString();
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

    public String getPublicHandle() {
        return m_publicHandle;
    }

    public void setPublicHandle(String publicHandle) {
        m_publicHandle = publicHandle;
    }

    public String getProtectedHandle() {
        return m_protectedHandle;
    }

    public void setProtectedHandle(String protectedHandle) {
        m_protectedHandle = protectedHandle;
    }

    public String getPrivateHandle() {
        return m_privateHandle;
    }

    public void setPrivateHandle(String privateHandle) {
        m_privateHandle = privateHandle;
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
