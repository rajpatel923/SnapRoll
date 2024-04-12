package com.example.snaproll;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class NotesFragment extends Fragment {

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }*/

    private static final int REQUEST_PICK_MP3_FILE = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // takeNotesButton triggers file picking
        view.findViewById(R.id.takeNotesButton).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                pickMp3File();
            }
        });
    }

    private void pickMp3File()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");
        startActivityForResult(intent, REQUEST_PICK_MP3_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_MP3_FILE && resultCode == Activity.RESULT_OK)
        {
            if (data != null && data.getData() != null) {
                Uri selectedFileUri = data.getData();
                // selectedFileUri holds the Uri of the selected MP3 file

                Log.d("NotesFragment", "MP3 file has begun copy to app directory: ");
                copyMP3FileToAppDirectory(selectedFileUri);
            }
            //added after working
            else {
                Log.d("NotesFragment", "MP3 file did not begin copy to app directory: ");

            }
        }
    }








    //everything above works

    private void copyMP3FileToAppDirectory(Uri mp3Uri)
    {
        // Define the destination directory within your app's directory
        File destinationDirectory = new File(requireActivity().getFilesDir(), "mp3s");
        if (!destinationDirectory.exists()) {
            destinationDirectory.mkdirs();
        }

        // Extract the file name from the URI
        String fileName = mp3Uri.getLastPathSegment();
        //String fileName = mp3Uri.substring(mp3Uri.lastIndexOf('/') + 1);

        // Create a destination file within the destination directory
        File destinationFile = new File(destinationDirectory, fileName);

        try {
            // Open the input and output streams

            InputStream inputStream = requireActivity().getContentResolver().openInputStream(mp3Uri);
            //InputStream inputStream = new FileInputStream(new File(mp3Uri));

            OutputStream outputStream = new FileOutputStream(destinationFile);

            // Copy the file content
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            // Notify that the file has been copied successfully
            Log.d("NotesFragment", "MP3 file copied to app directory successfully: " + destinationFile.getAbsolutePath());

            //NotesFragment
            //Log.d("FileCopy", "MP3 file copied to app directory successfully: " + destinationFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("NotesFragment", "MP# file copied to app directory UN - successfully: ");

            // Handle any errors that occur during the file copy process
        }
    }






}


