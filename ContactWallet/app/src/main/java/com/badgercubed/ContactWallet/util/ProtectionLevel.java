package com.badgercubed.ContactWallet.util;

public enum ProtectionLevel {
    PUBLIC(0),
    PROTECTED(1),
    PRIVATE(2);

    private int level;

    ProtectionLevel(final int l) {
        this.level = l;
    }

    public int getProtectionLevel() {
        return this.level;
    }
}
