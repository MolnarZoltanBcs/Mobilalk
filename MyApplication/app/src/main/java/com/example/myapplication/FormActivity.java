package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FormActivity extends AppCompatActivity {
    public static final String LOG_TAG=FormActivity.class.getName();
    public static final int SECRET_KEY=97;
    private FirebaseFirestore mFirestore;
    TextView formTitle;
    TextView formQ1;
    TextView formQ2;
    TextView formQ3;
    TextView formQ4;
    TextView formQ5;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        int secret_key = getIntent().getExtras().getInt("SECRET_KEY", 0);
        if (secret_key != 98) finish();

        mFirestore = FirebaseFirestore.getInstance();;

//        name = (TextView) findViewById(R.id.placeholder2TextView);
//        name.setText(getIntent().getExtras().getString("username", "User"));

        String title = getIntent().getExtras().getString("title", "Title");
        String Q1 = getIntent().getExtras().getString("q1", "q1");
        String Q2 = getIntent().getExtras().getString("q2", "q2");
        String Q3 = getIntent().getExtras().getString("q3", "q3");
        String Q4 = getIntent().getExtras().getString("q4", "q4");
        String Q5 = getIntent().getExtras().getString("q5", "q5");
        id = getIntent().getExtras().getString("doc", "");
        formTitle = (TextView) findViewById(R.id.formTitleTextView);
        formTitle.setText(title);
        formQ1 = (TextView) findViewById(R.id.viewQ1);
        formQ1.setText(Q1);
        formQ2 = (TextView) findViewById(R.id.viewQ2);
        formQ2.setText(Q2);
        formQ3 = (TextView) findViewById(R.id.viewQ3);
        formQ3.setText(Q3);
        formQ4 = (TextView) findViewById(R.id.viewQ4);
        formQ4.setText(Q4);
        formQ5 = (TextView) findViewById(R.id.viewQ5);
        formQ5.setText(Q5);
    }

    public void back(View view) {
        finish();
    }

    public void submit(View view) {
        Task<DocumentSnapshot> a = mFirestore.collection("forms").document(id).get();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DocumentSnapshot e = a.getResult();
        Form f = e.toObject(Form.class);

        int fills= f.getFills();
        mFirestore.collection("forms").document(id).update(
                "fills",
                fills+1
        );
        Toast.makeText(FormActivity.this, "Submited", Toast.LENGTH_LONG).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
        finish();
    }
}