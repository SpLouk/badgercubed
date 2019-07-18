package com.badgercubed.ContactWallet.model;

public enum ProtectionLevel {
    PUBLIC(0, "Public"),
    PROTECTED(1, "Protected"),
    PRIVATE(2, "Private");

    private final int m_levelNum;
    private final String m_name;

    ProtectionLevel(int levelNum, String name) {
        m_levelNum = levelNum;
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public int getInt() {
        return m_levelNum;
    }
}
