/*
 * Forums Class
 * Samba Diagne
 */
package edu.uncc.hw07;

import java.io.Serializable;
import java.util.ArrayList;


public class Forums implements Serializable {

    public String forumTitle;
    public String forumCreatedBy;
    public String forumText;
    public String forumCreatedAt;
    public String forumId;
    public String userID;
    ArrayList<String> forumLiked;

    public Forums(String forumId, String forumTitle, String forumCreatedBy, String forumText, String forumCreatedAt, String userID, ArrayList<String> liked) {
        this.forumId = forumId;
        this.forumTitle = forumTitle;
        this.forumCreatedBy = forumCreatedBy;
        this.forumText = forumText;
        this.forumCreatedAt = forumCreatedAt;
        this.userID = userID;
        this.forumLiked = liked;
    }


    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumCreatedBy() {
        return forumCreatedBy;
    }

    public void setForumCreatedBy(String forumCreatedBy) {
        this.forumCreatedBy = forumCreatedBy;
    }

    public String getForumText() {
        return forumText;
    }

    public void setForumText(String forumText) {
        this.forumText = forumText;
    }

    public String getForumCreatedAt() {
        return forumCreatedAt;
    }

    public void setForumCreatedAt(String forumCreatedAt) {
        this.forumCreatedAt = forumCreatedAt;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public ArrayList<String> getForumLiked() {
        return forumLiked;
    }

    public void setForumLiked(ArrayList<String> forumLiked) {
        this.forumLiked = forumLiked;
    }
}
