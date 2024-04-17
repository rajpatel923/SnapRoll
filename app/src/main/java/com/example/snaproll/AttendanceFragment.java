package com.example.snaproll;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.List;

public class AttendanceFragment extends Fragment {

    private LinearLayout classContainer;

    private static final String TAG = "AttendanceFragment";
    private int classCounter = 1;
    private TextureView textureView;
    private CameraDevice cameraDevice;
    private CameraCaptureSession captureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private Button btnCreateClass;
    private Button btnBackFromCamera;
    private ClassViewModel classViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);
        classContainer = view.findViewById(R.id.classContainer);
        textureView = view.findViewById(R.id.textureView);
        btnCreateClass = view.findViewById(R.id.btnCreateCourse);
        btnBackFromCamera = view.findViewById(R.id.btnBackFromCamera);
        btnBackFromCamera.setVisibility(View.GONE);
        classViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(ClassViewModel.class);
        btnCreateClass.setOnClickListener(v -> showAddClassDialog());
        btnBackFromCamera.setOnClickListener(v -> toggleCameraView());
        classViewModel.getClasses().observe(getViewLifecycleOwner(), this::updateClassList);
        return view;
    }

    private void addNewClass(String className) {
        RelativeLayout classBoxLayout = new RelativeLayout(requireContext());
        RelativeLayout.LayoutParams classBoxLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classBoxLayoutParams.setMargins(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14));
        classBoxLayout.setLayoutParams(classBoxLayoutParams);
        classBoxLayout.setBackground(requireContext().getDrawable(R.drawable.rounded_class_box));
        TextView classNameTextView = new TextView(requireContext());
        classNameTextView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        classNameTextView.setId(View.generateViewId());
        classNameTextView.setText(className);
        classNameTextView.setTextColor(getResources().getColor(R.color.black));
        classNameTextView.setTextSize(24);
        classBoxLayout.addView(classNameTextView);
        Button checkAttendanceButton = createButton("Check Attendance", R.drawable.checkattendanceicon);
        RelativeLayout.LayoutParams checkParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        checkParams.addRule(RelativeLayout.BELOW, classNameTextView.getId());
        checkParams.addRule(RelativeLayout.ALIGN_PARENT_START);
        checkParams.setMargins(dpToPx(10), dpToPx(8), 0, 0);
        checkAttendanceButton.setLayoutParams(checkParams);
        classBoxLayout.addView(checkAttendanceButton);
        Button takeAttendanceButton = createButton("Take Attendance", R.drawable.facerecognitionicon);
        takeAttendanceButton.setOnClickListener(v -> toggleCameraView());
        RelativeLayout.LayoutParams takeParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        takeParams.addRule(RelativeLayout.BELOW, classNameTextView.getId());
        takeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
        takeParams.setMargins(0, dpToPx(8), dpToPx(10), 0);
        takeAttendanceButton.setLayoutParams(takeParams);
        classBoxLayout.addView(takeAttendanceButton);
        classContainer.addView(classBoxLayout);
    }

    private Button createButton(String text, int drawableId) {
        Button button = new Button(requireContext());
        button.setText(text);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
        button.setBackgroundResource(R.drawable.rounded_button);
        return button;
    }

    private void toggleCameraView() {
        if (textureView.getVisibility() == View.VISIBLE) {
            textureView.setVisibility(View.GONE);
            closeCamera();
            btnCreateClass.setVisibility(View.VISIBLE);
            btnBackFromCamera.setVisibility(View.GONE);
        } else {
            if (checkCameraPermission()) {
                textureView.setVisibility(View.VISIBLE);
                btnCreateClass.setVisibility(View.GONE);
                btnBackFromCamera.setVisibility(View.VISIBLE);
                openCamera();
            } else {
                requestCameraPermission();
            }
        }
    }

    private void openCamera() {
        if (!checkCameraPermission()) {
            Log.e(TAG, "Camera permission is not granted");
            return;
        }
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = manager.getCameraIdList()[0];
            manager.openCamera(cameraId, stateCallback, null);
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException in openCamera.", e);
        } catch (CameraAccessException e) {
            Log.e(TAG, "CameraAccessException in openCamera.", e);
        }
    }


    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            closeCamera();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            closeCamera();
        }
    };

    private void startPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(1920, 1080);
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(java.util.Collections.singletonList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) return;
                    captureSession = session;
                    try {
                        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
                        session.setRepeatingRequest(captureRequestBuilder.build(), null, null);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.e(TAG, "Failed to configure camera.");
                }
            }, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Failed to start camera preview.", e);
        }
    }

    private void closeCamera() {
        if (captureSession != null) {
            captureSession.close();
            captureSession = null;
        }
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
        textureView.setVisibility(View.GONE);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private boolean checkCameraPermission() {
        return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
    }

    private void showAddClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Enter Class Name");
        final EditText classNameEditText = new EditText(requireContext());
        classNameEditText.setHint("Class Name");
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        classNameEditText.setLayoutParams(layoutParams);
        builder.setView(classNameEditText);
        builder.setPositiveButton("OK", (dialog, which) -> {
            String className = classNameEditText.getText().toString().trim();
            if (!className.isEmpty()) {
                classViewModel.addClass(className);
            } else {
                Toast.makeText(requireContext(), "Please enter a class name", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    public static class ClassViewModel extends ViewModel {
        private MutableLiveData<List<String>> classes = new MutableLiveData<>();

        public LiveData<List<String>> getClasses() {
            if (classes.getValue() == null) {
                classes.setValue(new ArrayList<>());
            }
            return classes;
        }

        public void addClass(String className) {
            List<String> currentClasses = classes.getValue();
            if (currentClasses == null) {
                currentClasses = new ArrayList<>();
            }
            currentClasses.add(className);
            classes.setValue(currentClasses);
        }
    }



    private void updateClassList(List<String> classes) {
        classContainer.removeAllViews();
        for (String className : classes) {
            addNewClass(className);
        }
    }
}
