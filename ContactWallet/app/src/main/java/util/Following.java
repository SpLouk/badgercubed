package util;

public class Following {
    public static final String m_collectionName = "following";

    private String m_uid;
    private String m_followerId;
    private String m_followingId;

    public Following() {

    }

    public Following(String m_uid, String m_followerId, String m_followingId) {
        this.m_uid = m_uid;
        this.m_followerId = m_followerId;
        this.m_followingId = m_followingId;
    }

    public String getUid() {
        return m_uid;
    }

    public String getFollowerId() {
        return m_followerId;
    }

    public void setFollowerId(String m_followerId) {
        this.m_followerId = m_followerId;
    }

    public String getFollowingId() {
        return m_followingId;
    }

    public void setFollowingId(String m_followingId) {
        this.m_followingId = m_followingId;
    }
}
