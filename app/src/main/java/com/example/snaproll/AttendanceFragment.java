package com.example.snaproll;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AttendanceFragment extends Fragment {

    private LinearLayout classContainer;
    private int classCounter = 1; // Counter to provide unique IDs for each class

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        classContainer = view.findViewById(R.id.classContainer);

        Button btnCreateClass = view.findViewById(R.id.btnCreateCourse);
        btnCreateClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewClass();
            }
        });

        return view;
    }

    private void addNewClass() {
        // Create a new RelativeLayout for the class container
        RelativeLayout classBoxLayout = new RelativeLayout(requireContext());
        RelativeLayout.LayoutParams classBoxLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classBoxLayoutParams.setMargins(dpToPx(16), dpToPx(14), dpToPx(16), dpToPx(14)); // Adjust margins here
        classBoxLayout.setLayoutParams(classBoxLayoutParams);
        classBoxLayout.setBackground(requireContext().getDrawable(R.drawable.rounded_class_box));

        // Create a TextView for the class name
        TextView classNameTextView = new TextView(requireContext());
        RelativeLayout.LayoutParams classNameParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classNameParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        classNameParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        classNameTextView.setLayoutParams(classNameParams);
        classNameTextView.setId(View.generateViewId()); // Generate a unique ID for this view
        classNameTextView.setText("Class " + classCounter++); // Increment the counter for each new class
        classNameTextView.setTextColor(getResources().getColor(R.color.black));
        classNameTextView.setTextSize(24);
        classBoxLayout.addView(classNameTextView);

        // Create buttons for attendance
        Button checkAttendanceButton = createButton("Check Attendance", R.drawable.checkattendanceicon);
        RelativeLayout.LayoutParams checkAttendanceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        checkAttendanceParams.addRule(RelativeLayout.BELOW, classNameTextView.getId());
        checkAttendanceParams.addRule(RelativeLayout.ALIGN_PARENT_START); // Align to the start (left) of the parent
        checkAttendanceParams.setMargins(dpToPx(10), dpToPx(8), -10, 15); // Adjust top margin here and add left margin
        checkAttendanceButton.setLayoutParams(checkAttendanceParams);

        Button takeAttendanceButton = createButton("Take Attendance", R.drawable.facerecognitionicon);
        RelativeLayout.LayoutParams takeAttendanceParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        takeAttendanceParams.addRule(RelativeLayout.BELOW, classNameTextView.getId());
        takeAttendanceParams.addRule(RelativeLayout.ALIGN_PARENT_END); // Align to the end (right) of the parent
        takeAttendanceParams.setMargins(-10, dpToPx(8), dpToPx(10), 15); // Adjust top margin here and add right margin
        takeAttendanceButton.setLayoutParams(takeAttendanceParams);

        classBoxLayout.addView(checkAttendanceButton);
        classBoxLayout.addView(takeAttendanceButton);

        // Add the class container to the main layout
        classContainer.addView(classBoxLayout);
    }

    private Button createButton(String text, int drawableId) {
        Button button = new Button(requireContext());
        button.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(text);
        button.setTextColor(getResources().getColor(R.color.white));
        button.setCompoundDrawablesWithIntrinsicBounds(0, drawableId, 0, 0);
        button.setBackgroundResource(R.drawable.rounded_button);
        return button;
    }

    // Convert dp to pixels
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}