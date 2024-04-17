package com.example.snaproll.models;

public class UserNotesInformationRespModel {
    private String Id;
    private String userName;
    private String userId;
    private String textFileUrlFromCloudnary;
    private String uploadTime;
    private String noteTitle;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTextFileUrlFromCloudnary() {
        return textFileUrlFromCloudnary;
    }

    public void setTextFileUrlFromCloudnary(String textFileUrlFromCloudnary) {
        this.textFileUrlFromCloudnary = textFileUrlFromCloudnary;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    @Override
    public String toString() {
        return "UserNotesInformationRespModel{" +
                "Id='" + Id + '\'' +
                ", userName='" + userName + '\'' +
                ", userId='" + userId + '\'' +
                ", textFileUrlFromCloudnary='" + textFileUrlFromCloudnary + '\'' +
                ", uploadTime=" + uploadTime +
                ", noteTitle='" + noteTitle + '\'' +
                '}';
    }
}
