package com.badgercubed.ContactWallet.model;

public enum Service {
    // Display alphabetially
    FACEBOOK ("Facebook", "facebook.com/"),
    MESSENGER ("Messenger", "messenger.com/t/"),
    TWITTER ("Twitter", "twitter.com/"),
    TEST ("Test", "");

    private final String m_name;
    private final String m_baseLink;

    Service(String name, String baseLink) {
        m_name = name;
        m_baseLink = baseLink;
    }

    public String getName() {
        return m_name;
    }

    public String getLink() {
        return m_baseLink;
    }
}
