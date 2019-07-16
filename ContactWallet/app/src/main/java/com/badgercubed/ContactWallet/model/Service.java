package com.badgercubed.ContactWallet.model;

public enum Service {
    // Display alphabetially
    FACEBOOK(0, "Facebook", "facebook.com/"),
    MESSENGER(1, "Messenger", "messenger.com/t/"),
    TWITTER(2, "Twitter", "twitter.com/"),
    TEST(3, "Test", "");

    private final int m_id;
    private final String m_name;
    private final String m_baseLink;

    Service(int id, String name, String baseLink) {
        m_id = id;
        m_name = name;
        m_baseLink = baseLink;
    }

    public int getId() {
        return m_id;
    }

    public String getName() {
        return m_name;
    }

    public String getLink() {
        return m_baseLink;
    }
}
