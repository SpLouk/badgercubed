package com.badgercubed.ContactWallet.model;

import android.text.TextUtils;

import com.google.firebase.firestore.Exclude;

public class ContactItem extends FBObject {
    public static final String m_collectionName = "contactItems";

    private String m_uid;
    private String m_userId;
    private String m_serviceId;
    private String m_link;
    private String m_description;
    private Integer m_protectionLevel;

    public ContactItem() {
    }

    public ContactItem(String uid, String userId, String serviceId, String link, String description, Integer protectionLevel) {
        m_uid = uid;
        m_userId = userId;
        m_serviceId = serviceId;
        m_link = link;
        m_description = description;
        m_protectionLevel = protectionLevel;
    }

    public static boolean isValidInputs(String uid, String userId, String serviceId, String link, String description, Integer protectionLevel) {
        if (TextUtils.isEmpty(uid)) {
            return false;
        }
        if (TextUtils.isEmpty(userId)) {
            return false;
        }
        if (TextUtils.isEmpty(serviceId)) {
            return false;
        }
        if (TextUtils.isEmpty(link)) {
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            return false;
        }
        // TODO: better verification
        return protectionLevel != null;
    }

    public String getUid() {
        return m_uid;
    }

    public void setUid(String uid) {
        m_uid = uid;
    }

    public String getUserId() {
        return m_userId;
    }

    public void setUserId(String userId) {
        m_userId = userId;
    }

    public String getServiceId() {
        return m_serviceId;
    }

    public void setServiceId(String serviceId) {
        m_serviceId = serviceId;
    }

    public String getLink() {
        return m_link;
    }

    public void setLink(String link) {
        m_link = link;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public Integer getProtectionLevel() {
        return m_protectionLevel;
    }

    public void setProtectionLevel(Integer protectionLevel) {
        m_protectionLevel = protectionLevel;
    }

    public void validate() throws Exception {
        if (TextUtils.isEmpty(m_uid)) {
            throw new Exception("UID is empty");
        }
        if (TextUtils.isEmpty(m_userId)) {
            throw new Exception("User ID is empty");
        }
        if (TextUtils.isEmpty(m_serviceId)) {
            throw new Exception("Service ID is empty");
        }
        if (TextUtils.isEmpty(m_link)) {
            throw new Exception("Contact link is empty");
        }
        if (TextUtils.isEmpty(m_description)) {
            throw new Exception("Description is empty");
        }
        // TODO: better verification
        if (m_protectionLevel == null) {
            throw new Exception("Protection level is null");
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
