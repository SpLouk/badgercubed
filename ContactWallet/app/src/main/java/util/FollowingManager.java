package util;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void addFollowing(User follower, String email) {
        Task<QuerySnapshot> q = FBManager.getInstance().getUsersByEmail(email);
        q.addOnSuccessListener(result -> {
            if (result.isEmpty()) {
                return;
            }
            String id = result.getDocuments().get(0).getId();
            Following f = new Following();
            f.setFollowerUid(follower.getUid());
            f.setFollowingUid(id);
            f.setLevel("0"); // starts at public level by default

            FBManager.getInstance().getCollection(Following.getCollectionName()).add(f);
        });
    }

}
