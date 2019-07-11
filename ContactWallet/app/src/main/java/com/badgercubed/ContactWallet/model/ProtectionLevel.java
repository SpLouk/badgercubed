package com.badgercubed.ContactWallet.model;

public enum ProtectionLevel {
    PUBLIC (3, "Public"),
    PROTECTED (2, "Protected"),
    PRIVATE (1, "Private");

    private final int m_levelNum;
    private final String m_name;

    ProtectionLevel(int levelNum, String name) {
        m_levelNum = levelNum;
        m_name = name;
    }

    public String getName() {
        return m_name;
    }

    public int getInteger() {
        return m_levelNum;
    }
}
