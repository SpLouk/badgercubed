package com.badgercubed.ContactWallet.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.badgercubed.ContactWallet.fragment.ContactsFragment;
import com.badgercubed.ContactWallet.fragment.ProfileFragment;
import com.badgercubed.ContactWallet.model.User;
import com.badgercubed.ContactWallet.util.FBManager;
import com.badgercubed.ContactWallet.util.LoginManager;

public class NavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout m_drawerLayout;
    private TextView m_userName;
    private TextView m_userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        User user = LoginManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(this, "Error, user somehow logged out", Toast.LENGTH_SHORT).show();
            logoutUser();
            return;
        }

        setContentView(R.layout.activity_nav);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        m_drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        // If new instance
        if (savedInstanceState == null) {
            // start new fragment contacts as default
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_nav, new ContactsFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_contacts);
        }

        // Set current user's display name and email in navigation header
        View header = navigationView.getHeaderView(0);

        m_userName = header.findViewById(R.id.nav_header_user_name);
        m_userName.setText(user.getName());

        m_userEmail = header.findViewById(R.id.nav_header_user_email);
        m_userEmail.setText(user.getEmail());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_contacts:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_nav, new ContactsFragment())
                        .commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container_nav, new ProfileFragment())
                        .commit();
                break;
            case R.id.nav_logout:
                logoutUser();
                break;
        }

        m_drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (m_drawerLayout.isDrawerOpen(GravityCompat.START)) {
            m_drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            m_drawerLayout.openDrawer(GravityCompat.START);
            //super.onBackPressed();
        }
    }

    private void logoutUser() {
        FBManager.getInstance().logout();
        finish();
        Activities.startWelcomeActivity(this);
    }
}
