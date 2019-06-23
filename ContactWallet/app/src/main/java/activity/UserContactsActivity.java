package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badgercubed.ContactWallet.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontacts);

        // TODO : Get userid from savedInstanceState and pass to contactItemsFragment
    }
}
