package com.example.snaproll.APIController;

import com.example.snaproll.models.UserNotesInformationRespModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("/api/v1/getAllNotes")
    Call<List<UserNotesInformationRespModel>> getAllNotes();
}