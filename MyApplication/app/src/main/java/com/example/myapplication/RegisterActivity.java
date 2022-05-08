package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    public static final String LOG_TAG=RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();
    private FirebaseAuth mAuth;
    EditText usernameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;


    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        int secret_key = bundle.getInt("SECRET_KEY", 0);

        if (secret_key != 99){
            finish();
        }

        usernameET = findViewById(R.id.usernameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        emailET.setText(preferences.getString("email",""));
        passwordET.setText(preferences.getString("password",""));
        passwordAgainET.setText(preferences.getString("password",""));

        mAuth=FirebaseAuth.getInstance();

        Log.i(LOG_TAG,"onCreate");
    }

    public void register(View view) {

        String username = usernameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        Intent intent = new Intent(this, MainActivity.class);

        if(!password.equals(passwordAgain)){
            Log.e(LOG_TAG, "password not equal");
        }else{
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d(LOG_TAG, "register successful");
                        startActivity(intent);
                    }
                    else{
                        Log.d(LOG_TAG, "register unsuccessful");
                        Toast.makeText(RegisterActivity.this, "unsuccessful register because: "+ task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }


    }

    public void cancel(View view) {
        finish();
        Log.i(LOG_TAG,"register cancel");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"register onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"register onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"register onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"register onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"register onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"main onRestart");
    }
}