package util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import activity.ProfileActivity;
import model.Following;
import model.User;

public class FollowingManager {
    private static FollowingManager instance = null;

    public static FollowingManager getInstance() {
        if (instance == null) {
            instance = new FollowingManager();
        }
        return instance;
    }

    public void addFollowing(Context context, User follower, String email) {
        Task<QuerySnapshot> usersByEmail = FBManager.getInstance().getUsersByEmail(email);
        usersByEmail.addOnSuccessListener(result -> {
            if (result.isEmpty()) {
                Toast.makeText(context, "Contact does not exist!", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO : Check for repeats when adding, currently can add multiple entries in following table
            //  with same relationship, also don't allow follows to self

            String id = result.getDocuments().get(0).getId();
            Following f = new Following();
            f.setFollowerUid(follower.getUid());
            f.setFollowingUid(id);
            f.setLevel("0"); // starts at public level by default
            FBManager.getInstance().getCollection(Following.mCollectionName).add(f);
            Toast.makeText(context, "Contact added!", Toast.LENGTH_SHORT).show();
        });
    }
}
