package com.badgercubed.ContactWallet.model;

import android.content.Context;

import com.badgercubed.ContactWallet.activity.Activities;
import com.badgercubed.ContactWallet.util.App;

public enum Service {
    // Display alphabetially
    FACEBOOK ("Facebook", "facebook.com/"),
    MESSENGER ("Messenger", "messenger.com/t/"),
    TWITTER ("Twitter", "twitter.com/"),
    GITHUB ( "Github", "github.com/"),
    CUSTOMURL ("Custom URL", ""),
    CUSTOM ("Custom", "", false),
    EMAIL ("Email", "", false),
    PHONENUM ("Phone Number", "", false);

    private final String m_name;
    private final String m_baseLink;
    private final boolean m_isHttpLinkUsed;

    Service(String name, String baseLink) {
        m_name = name;
        m_baseLink = baseLink;
        m_isHttpLinkUsed = true;
    }

    Service(String name, String baseLink, boolean isHttpLinkUsed) {
        m_name = name;
        m_baseLink = baseLink;
        m_isHttpLinkUsed = isHttpLinkUsed;
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

    public void openLink(Context context, String link) {
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
