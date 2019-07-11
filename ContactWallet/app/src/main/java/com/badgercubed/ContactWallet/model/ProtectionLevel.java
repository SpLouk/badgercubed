package com.badgercubed.ContactWallet.model;

public enum ProtectionLevel {
    PRIVATE (1, "Private"),
    PROTECTED (2, "Protected"),
    PUBLIC (3, "Public");

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
