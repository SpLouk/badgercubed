package com.badgercubed.ContactWallet.model;

public enum ProtectionLevel {
    PUBLIC("Public"),
    PROTECTED("Protected"),
    PRIVATE("Private");

    private final String m_name;

    ProtectionLevel(String name) {
        m_name = name;
    }

    public String getName() {
        return m_name;
    }
}
