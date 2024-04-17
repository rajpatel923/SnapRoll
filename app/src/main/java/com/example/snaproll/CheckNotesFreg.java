package com.example.snaproll;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.snaproll.APIController.APIService;
import com.example.snaproll.APIController.RetrofitClient;
import com.example.snaproll.models.UserNotesInformationRespModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckNotesFreg extends Fragment {
    private LinearLayout notesLink;
    private TextView userInfoTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_notes_freg, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        notesLink = view.findViewById(R.id.notesLinkLayout);
        userInfoTextView = view.findViewById(R.id.userInfoTextView);
        fetchUserData();
    }
    private void fetchUserData(){
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://localhost:8500/") // Replace with your actual URL
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        APIService apiService = RetrofitClient.getRetrofitInstance().create(APIService.class);
        Call<List<UserNotesInformationRespModel>> userNotesList = apiService.getAllNotes();
        userNotesList.enqueue(new Callback<List<UserNotesInformationRespModel>>() {
            @Override
            public void onResponse(Call<List<UserNotesInformationRespModel>> call, Response<List<UserNotesInformationRespModel>> response) {
                if (response.isSuccessful()) {
                    userInfoTextView.setText("Save Notes");
                    List<UserNotesInformationRespModel> users = response.body();
                    for (UserNotesInformationRespModel userNote: users) {

                        // Create a new horizontal LinearLayout for each item
                        LinearLayout horizontalLayout = new LinearLayout(getContext());
                        horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                        horizontalLayout.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);

                        // Create an ImageButton
//                        ImageButton imageButton = new ImageButton(getContext());
//                        imageButton.setLayoutParams(new LinearLayout.LayoutParams(
//                                LinearLayout.LayoutParams.WRAP_CONTENT,
//                                LinearLayout.LayoutParams.WRAP_CONTENT));
//                        imageButton.setImageResource();
//                        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
//                        imageButton.setOnClickListener(v -> {
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(userNote.getTextFileUrlFromCloudnary()));
//                            startActivity(browserIntent);
//                        });
//                        horizontalLayout.addView(imageButton);

                        //userInfoTextView.append("\n"+userNote.getTextFileUrlFromCloudnary());
                        Button button = new Button(getContext());
                        button.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                               LinearLayout.LayoutParams.MATCH_PARENT));
                        button.setText(userNote.getNoteTitle());
                        //userInfoTextView.append(userNote.getTextFileUrlFromCloudnary());
                        button.setOnClickListener(v -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(userNote.getTextFileUrlFromCloudnary()));
                            startActivity(browserIntent);
                        });

                        horizontalLayout.addView(button);
                        horizontalLayout.setOnClickListener(v -> {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(userNote.getTextFileUrlFromCloudnary()));
                            startActivity(browserIntent);
                        });
                        //notesLink.addView(button);
                        notesLink.addView(horizontalLayout);
                    }
                    // Handle the received users
                } else {
                    userInfoTextView.setText("fail"+ response.code());
                    Log.e("API_CALL", "Error response code: " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<UserNotesInformationRespModel>> call, Throwable t) {
                userInfoTextView.setText(t.getMessage());
                Log.e("API_CALL", "Failed to fetch users", t);
            }
        });
    }
}