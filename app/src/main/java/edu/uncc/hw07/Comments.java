/*
 * Comments Class
 * Samba Diagne
 */

package edu.uncc.hw07;

public class Comments {
    String commentID;
    String commentCreator;
    String commentText;
    String dateCreated;




    public Comments(String commentID, String commentCreator, String commentText, String dateCreated) {
       this.commentID = commentID;
        this.commentCreator = commentCreator;
        this.commentText = commentText;
        this.dateCreated = dateCreated;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(String commentCreator) {
        this.commentCreator = commentCreator;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }


}
