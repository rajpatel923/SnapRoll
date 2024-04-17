package com.example.snaproll;

<<<<<<< Updated upstream
=======
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
>>>>>>> Stashed changes
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

<<<<<<< Updated upstream
=======
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
>>>>>>> Stashed changes
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

<<<<<<< Updated upstream
=======
import com.example.snaproll.APIController.ApiUploadInterface;
import com.example.snaproll.APIController.RetrofitClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


>>>>>>> Stashed changes
public class NotesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
<<<<<<< Updated upstream
    }
}
=======
    }*/

    private static final int REQUEST_PICK_MP3_FILE = 1;
    private ActivityResultLauncher<String> mGetContent;
    private Uri mp3Uri;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the Activity Result Launcher
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                    if (uri != null) {
                        mp3Uri = uri;
                        Toast.makeText(getContext(), "MP3 file address"+mp3Uri.getPath(), Toast.LENGTH_SHORT).show();
                        // Perform operations with the Uri here, like updating the UI or starting upload
                    }
                });
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

        //check notes button listner
        view.findViewById(R.id.checkNotesButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckNotesFreg checkNotesFreg = new CheckNotesFreg();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frameLayout,checkNotesFreg);
                transaction.commit();
            }
        });
        view.findViewById(R.id.uploadmp3FileButton).setOnClickListener(new View.OnClickListener() {
            private static final int PICK_MP3_REQUEST_CODE = 1;

            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View view = inflater.inflate(R.layout.mp3upload_dialog_form, null);
                EditText editTextFileName = view.findViewById(R.id.editTextFileName);
                EditText editTextUserName = view.findViewById(R.id.editTextUserName);
                EditText editTextUserId = view.findViewById(R.id.editTextUserId);
                Button buttonSelectMp3 = view.findViewById(R.id.buttonSelectMp3);
                buttonSelectMp3.setOnClickListener(vi -> {
//                    Intent intent = new Intent();
//                    intent.setType("audio/mpeg"); // MIME type for MP3
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select MP3"), PICK_MP3_REQUEST_CODE);
                    mGetContent.launch("audio/mpeg");
                });
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(view)
                        .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Retrieve input values from EditText fields
                                String notesFileName = editTextFileName.getText().toString();
                                String userName = editTextUserName.getText().toString();
                                String userId = editTextUserId.getText().toString();
                                String classId = "001";

                                InputStream inputStream = getInputStreamFromUri(mp3Uri,getContext());
                                File file = convertInputStreamToFile(inputStream,getContext());

                                //File file = new File(Objects.requireNonNull(mp3Uri.getPath()));
                                RequestBody requestFile = RequestBody.create(MediaType.parse("audio/mp3"), file);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("audioFile", file.getName(), requestFile);
                                RequestBody fileNamePart = RequestBody.create(MediaType.parse("text/plain"), notesFileName);
                                RequestBody userNamePart = RequestBody.create(MediaType.parse("text/plain"), userName);
                                RequestBody userIdPart = RequestBody.create(MediaType.parse("text/plain"), userId);
                                RequestBody classIdPart = RequestBody.create(MediaType.parse("text/plain"), classId);
                                ApiUploadInterface apiUploadInterface = RetrofitClient.getRetrofitInstance().create(ApiUploadInterface.class);
                                Call<Void> call = apiUploadInterface.uploadFileWithPartMap(body, fileNamePart, userNamePart, userIdPart, classIdPart);

                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Toast.makeText(getContext(), "Data added to API", Toast.LENGTH_SHORT).show();
                                    }
                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getContext(), "Data fail to add to API"+t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                Dialog dialog = builder.create();
                dialog.show();

            }


        });
    }
    public InputStream getInputStreamFromUri(Uri uri, Context context) {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    public File convertInputStreamToFile(InputStream inputStream, Context context) {
        File file = new File(context.getCacheDir(), "tempFileToSend" + System.currentTimeMillis() + ".mp3"); // Specify .mp3 extension
        try (OutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[4 * 1024]; // buffer size
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
    private byte[] getBytes(Uri uri) throws IOException {
        InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
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


>>>>>>> Stashed changes
