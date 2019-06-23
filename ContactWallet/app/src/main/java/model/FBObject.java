package model;

public abstract class FBObject {
    public abstract void validate() throws Exception;
    public abstract String getCollectionName();
    public abstract String getDocReference();
}
