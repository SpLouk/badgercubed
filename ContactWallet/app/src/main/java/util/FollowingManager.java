package util;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

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

            String id = result.getDocuments().get(0).getId();

            // TODO : Check for repeats when adding, currently can add multiple entries in following table
            //  with same relationship, also don't allow follows to self
            FBManager.getInstance().getCollection(Following.mCollectionName)
                    .whereEqualTo("followerUid", follower.getUid())
                    .whereEqualTo("followingUid", id)
                    .get()
                    .addOnSuccessListener(res -> {
                        if (res.isEmpty() && !follower.getUid().equals(id)) {
                            Following f = new Following(UUID.randomUUID().toString(), follower.getUid(), id,
                                    "0"); // starts at public level by default

                            OnCompleteListener<Void> onCompleteListener = t -> {
                                Toast.makeText(context, "Contact added!", Toast.LENGTH_SHORT).show();
                            };

                            FBManager.getInstance().saveFBObject(context, f, onCompleteListener);
                        }
                        else {
                            Toast.makeText(context, "Not Valid!", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
