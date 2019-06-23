package model;

import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User extends FBObject {
    private final String m_collectionName = "users";

    private String m_uid;
    private String m_email;
    private String m_name;
    private String m_phoneNum;
    private List<String> m_contactItemIds;

    public User() {
        m_contactItemIds = new ArrayList<>();
    }

    public User(String uid, String email, String name, String phoneNum, List<String> contactItemIds) {
        m_uid = uid;
        m_email = email;
        m_name = name;
        m_phoneNum = phoneNum;
        m_contactItemIds = contactItemIds;
    }

    public String getUid() {
        return m_uid;
    }

    public String getEmail() {
        return m_email;
    }

    public String getName() {
        return m_name;
    }

    public String getPhoneNum() {
        return m_phoneNum;
    }

    public List<String> getContactItemIds() {
        return m_contactItemIds;
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
        if (m_contactItemIds == null) {
            throw new Exception("Contact item id's null");
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
