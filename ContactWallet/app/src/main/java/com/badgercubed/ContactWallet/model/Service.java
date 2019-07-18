package com.badgercubed.ContactWallet.model;

import android.content.Context;

import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.util.App;

public enum Service {
    // Display alphabetially
    FACEBOOK (0,"Facebook", "facebook.com/"),
    MESSENGER (1, "Messenger", "messenger.com/t/"),
    TWITTER (2, "Twitter", "twitter.com/"),
    GITHUB (3, "Github", "github.com/"),
    CUSTOMURL(4, "Custom URL", ""),
    CUSTOM (5,"Custom", "", false),
    EMAIL (6,"Email", "", false),
    PHONENUM (7,"Phone Number", "", false);

    private final int m_id;
    private final String m_name;
    private final String m_baseLink;
    private final boolean m_isHttpLinkUsed;

    Service(int id, String name, String baseLink) {
        m_id = id;
        m_name = name;
        m_baseLink = baseLink;
        m_isHttpLinkUsed = true;
    }

    Service(int id, String name, String baseLink, boolean isHttpLinkUsed) {
        m_id = id;
        m_name = name;
        m_baseLink = baseLink;
        m_isHttpLinkUsed = isHttpLinkUsed;
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

    public boolean isHttpLinkUsed() {
        return m_isHttpLinkUsed;
    }

    public void openLink(String link) {
        Context context = App.getContext();
        switch(Service.this) {
            case FACEBOOK:
            case MESSENGER:
            case TWITTER:
            case GITHUB:
            case CUSTOMURL:
                Activities.startBrowserActivity(context, link);
                break;
            case EMAIL:
                Activities.startEmailActivity(context, link);
                break;
            case PHONENUM:
                Activities.startCallActivity(context, link);
                break;
            default:
                // code block
                break;
        }
    }
}
