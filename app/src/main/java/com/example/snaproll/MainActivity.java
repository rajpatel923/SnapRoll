package com.example.snaproll;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // setting up the title
       // getSupportActionBar().setTitle("SnapRoll");

        //open login
//        Button logInButton = findViewById(R.id.logInButton);
//        logInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        //register activity
//        Button signUpButton = findViewById(R.id.signUpButton);
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, signUpActivity.class);
//                startActivity(intent);
//            }
//        });

    }
}