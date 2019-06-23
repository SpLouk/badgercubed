package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import model.User;
import util.FBManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView m_email;
    private EditText m_name;
    private EditText m_phoneNum;
    private Button m_save;
    private Button m_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseUser user = FBManager.getInstance().getCurrentUser();
        if (user == null) {
            // User somehow logged out
            Toast.makeText(ProfileActivity.this, "ERROR, user somehow logged out", Toast.LENGTH_SHORT).show();
            finish();
            Activities.startLoginActivity(this);
        }

        m_email = findViewById(R.id.profile_email);
        m_email.setText(user.getEmail());

        m_name = findViewById(R.id.profile_enterName);
        m_phoneNum = findViewById(R.id.profile_enterPhoneNum);

        m_save = findViewById(R.id.profile_saveData);
        m_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                saveData();
            }
        });

        m_logout = findViewById(R.id.profile_logout);
        m_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                logoutUser();
            }
        });
    }

    public void saveData() {
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = fbUser.getUid();
        String email = fbUser.getEmail();
        String name = m_name.getText().toString().trim();
        String phoneNum = m_phoneNum.getText().toString().trim();

        User user = new User(uid, email, name, phoneNum, new ArrayList<String>());
        FBManager.getInstance().saveFBObject(this, user, null);
    }

    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Activities.startLoginActivity(this);
    }
}
