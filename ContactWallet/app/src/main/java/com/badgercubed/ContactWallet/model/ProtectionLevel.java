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

    public int getInt() {
        return m_levelNum;
    }

    public static ProtectionLevel fromInt(Integer protLevelNum) {
        if (protLevelNum == null) return null;

        switch(protLevelNum) {
            case 3: return PUBLIC;
            case 2: return PROTECTED;
            case 1: return PRIVATE;
            default: return null;
        }
    }
}
