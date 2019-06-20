package model;

import android.text.TextUtils;

public class ContactItem {
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
        if (protectionLevel == null) {
            return false;
        }
        return true;
    }

    public String getUid() {
        return m_uid;
    }

    public String getUserId() {
        return m_userId;
    }

    public String getServiceId() {
        return m_serviceId;
    }

    public String getLink() {
        return m_link;
    }

    public String getDescription() {
        return m_description;
    }

    public Integer getProtectionLevel() {
        return m_protectionLevel;
    }
}
