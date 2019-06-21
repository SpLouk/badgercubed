package model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class User {
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

    public static boolean isValidInputs(String uid, String email, String name, String phoneNum) {
        if (TextUtils.isEmpty(uid)) {
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        /*if (m_contactItemIds == null) {
            return false;
        }*/
        return true;
    }

    /*private void validate() throws Exception {
        if (TextUtils.isEmpty(uid)) {
            throw new Exception("UID is empty");
        }
        if (TextUtils.isEmpty(email)) {
            throw new Exception("Email is empty");
        }
        if (TextUtils.isEmpty(name)) {
            throw new Exception("Name is empty");
        }
        if (TextUtils.isEmpty(phoneNum)) {
            throw new Exception("Phone # is empty");
        }
    }*/

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
}
