package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {
    public static final String LOG_TAG=MainPageActivity.class.getName();
    public static final int SECRET_KEY=97;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;
    TextView name;

    EditText q1ET;
    EditText q2ET;
    EditText q3ET;
    EditText q4ET;
    EditText q5ET;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        int secret_key = getIntent().getExtras().getInt("SECRET_KEY", 0);
        if (secret_key != 98) finish();

        name = (TextView) findViewById(R.id.placeholder2TextView);
        title = "Form by "+getIntent().getExtras().getString("email", "User");
        name.setText(title);

        q1ET = findViewById(R.id.editq1);
        q2ET = findViewById(R.id.editq2);
        q3ET = findViewById(R.id.editq3);
        q4ET = findViewById(R.id.editq4);
        q5ET = findViewById(R.id.editq5);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();;
    }

    public void back(View view) {
        finish();
    }

    public void save_changes(View view) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String today = formatter.format(date);
        mFirestore.collection("forms").add(new Form(
                today,
                0,
                q1ET.getText().toString(),
                q2ET.getText().toString(),
                q3ET.getText().toString(),
                q4ET.getText().toString(),
                q5ET.getText().toString(),
                title
                )).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Successfully saved", Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
                else{
                    Toast.makeText(ProfileActivity.this, "Unsuccessful, please try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}