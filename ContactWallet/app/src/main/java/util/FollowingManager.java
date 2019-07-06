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

            // TODO : Check for repeats when adding, currently can add multiple entries in following table
            //  with same relationship, also don't allow follows to self

            String id = result.getDocuments().get(0).getId();
            Following f = new Following(UUID.randomUUID().toString(), follower.getUid(), id,
                    "0"); // starts at public level by default

            OnCompleteListener<Void> onCompleteListener = t -> {
                Toast.makeText(context, "Contact added!", Toast.LENGTH_SHORT).show();
            };

            FBManager.getInstance().saveFBObject(context, f, onCompleteListener);
        });
    }

    public void removeFollowing(String followerUid, String followingUid,
                                @Nullable OnCompleteListener onCompleteListener) {

        List<Task<Void>> deleteTasks = new ArrayList<>();

        Task<QuerySnapshot> querySnapshotTask = FBManager.getInstance()
                .getCollection(Following.mCollectionName)
                .whereEqualTo("followerUid", followerUid)
                .whereEqualTo("followingUid", followingUid)
                .get();

        querySnapshotTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot snapshot : task.getResult()) {
                    deleteTasks.add(snapshot.getReference().delete());
                }

                if (onCompleteListener != null) {
                    Tasks.whenAllComplete(deleteTasks).addOnCompleteListener(onCompleteListener);
                }
            }
        });
    }
}
