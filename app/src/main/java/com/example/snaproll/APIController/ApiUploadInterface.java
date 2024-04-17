package com.example.snaproll.APIController;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiUploadInterface {
    @Multipart
    @POST("/api/v1/uploadNotes")
    Call<Void> uploadFileWithPartMap(
            @Part MultipartBody.Part audioFile,
            @Part("notesFileName") RequestBody notesFileName,
            @Part("userName") RequestBody userName,
            @Part("userId") RequestBody userId,
            @Part("classId") RequestBody classId
    );
}
