package activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.badgercubed.ContactWallet.R;

public class UserContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usercontacts);

        // TODO : Get userid from savedInstanceState and pass to contactItemsFragment
        Bundle bundle = new Bundle();
        bundle.putString("userId", "23");

        activity.ContactItemsFragment contactItemsFragment = new activity.ContactItemsFragment();
        contactItemsFragment.setArguments(bundle);
    }
}
