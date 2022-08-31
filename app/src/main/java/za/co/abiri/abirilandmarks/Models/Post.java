package za.co.abiri.abirilandmarks.Models;

import com.google.firebase.database.ServerValue;

public class Post {

    //Class for post functionality
    private String PostKey;
    private String title;
    private String description;
    private String coordinates;
    private String picture;
    private String userId;
    private String userPhoto;
    private Object timeStamp;


    //Constructors
    public Post(String title, String description, String coordinates, String picture, String userId, String userPhoto) {
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.picture = picture;
        this.userId = userId;
        this.userPhoto = userPhoto;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public Post() {

    }

    //PostKey Getter and Setter
    public String getPostKey() {
        return PostKey;
    }

    public void setPostKey(String postKey) {
        PostKey = postKey;
    }
    //PostKey

    //Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getPicture() {
        return picture;
    }

    public String getUserId() {
        return userId;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }



    //SETTERS
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }
}
