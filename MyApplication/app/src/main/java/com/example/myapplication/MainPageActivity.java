package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {
    private static final String LOG_TAG=MainPageActivity.class.getName();
    private static final int SECRET_KEY=98;
    private FirebaseFirestore mFirestore;
    private Form mForm;
    private Form mForm2;
    private NotificationHandler mNotificationHandler;
    private ArrayList<Form> mFormList=new ArrayList<Form>();
    private ArrayList<Form> mFormList2=new ArrayList<Form>();


    private FirebaseUser user;
    private CollectionReference mItems;
    TextView greetings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        int secret_key = getIntent().getExtras().getInt("SECRET_KEY", 0);

        if (secret_key != 99 /*|| user==null*/){
            finish();
        }
        greetings = (TextView) findViewById(R.id.placeholderTextView);
        greetings.setText("Welcome, "+getIntent().getExtras().getString("email", "User")+"!");

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("forms");

        mNotificationHandler = new NotificationHandler(this);

        mItems.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        mFormList.add(document.toObject(Form.class));
                        Log.d(LOG_TAG,"async");
                    }
                }else{
                    Task<QuerySnapshot> item = mItems.get();
                    for( QueryDocumentSnapshot i : item.getResult()){
                        mFormList.add(i.toObject(Form.class));
                        Log.d(LOG_TAG,"manual");
                    }
                }
            }
        });

    }


    public void create_form(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        intent.putExtra("email",getIntent().getExtras().getString("email", "User"));
        startActivity(intent);
    }

    public void fill_form(View view) {
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("forms");
        int index = (int)(Math.random() * (mFormList.size()));
        mForm = mFormList.get(index);
        Log.d(LOG_TAG,"hi");

        Intent intent = new Intent(this, FormActivity.class);
        intent.putExtra("SECRET_KEY", SECRET_KEY);
        intent.putExtra("username",getIntent().getExtras().getString("email", "User"));
        intent.putExtra("title",mForm.getTitle());
        intent.putExtra("q1",mForm.getQ1());
        intent.putExtra("q2",mForm.getQ2());
        intent.putExtra("q3",mForm.getQ3());
        intent.putExtra("q4",mForm.getQ4());
        intent.putExtra("q5",mForm.getQ5());
        intent.putExtra("fills", mForm.getFills());
        startActivity(intent);
    }

    public void delete_forms(View view) {
        String username ="Form by "+ getIntent().getExtras().getString("email", "User");
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("forms");
        mFormList2.clear();

        mItems.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        mFormList2.add(document.toObject(Form.class));
                        Log.d(LOG_TAG,"async");
                    }
                }
            }
        });
        int j=0;
        if(mFormList2.isEmpty()){ //sometime async perfomrs nothing, it is a bug because it just never inters the onComplete method
            Task<QuerySnapshot> item = mItems.get();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for( QueryDocumentSnapshot i : item.getResult()){
                Form f=i.toObject(Form.class);
                if(username.equals(f.getTitle())){
                    mItems.document(i.getId()).delete();
                    j++;
                }
                Log.d(LOG_TAG,"manual");
            }
        }

//        Toast.makeText(MainPageActivity.this, "You have deleted all your forms!", Toast.LENGTH_LONG).show();
        mNotificationHandler.send("You have deleted "+j+" forms!");
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i(LOG_TAG,"main onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(LOG_TAG,"main onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG,"main onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(LOG_TAG,"main onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(LOG_TAG,"main onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(LOG_TAG,"main onRestart");
    }

    public void log_out(View view) {
        finish();
    }

    public void fill_my_form(View view) { //latest form
        Intent intent = new Intent(this, FormActivity.class);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("forms");
        mFormList2.clear();
        String username="Form by "+getIntent().getExtras().getString("email", "User");
        mItems.whereEqualTo("title", username).orderBy("creation_date", Query.Direction.DESCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        mFormList2.add(document.toObject(Form.class));
                        Log.d(LOG_TAG,"async");
                    }
                }else{
                    Task<QuerySnapshot> item = mItems.get();
                    for( QueryDocumentSnapshot i : item.getResult()){
                        mFormList2.add(i.toObject(Form.class));
                        Log.d(LOG_TAG,"manual");
                    }
                }
            }
        });
        if(mFormList2.isEmpty()) {//sometime async perfomrs nothing, it is a bug because it just never inters the onComplete method
            Task<QuerySnapshot> item =mItems.whereEqualTo("title", username).orderBy("creation_date", Query.Direction.DESCENDING).limit(1).get();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (QueryDocumentSnapshot i : item.getResult()) {
                mFormList2.add(i.toObject(Form.class));
                intent.putExtra("doc", i.getId());
                Log.d(LOG_TAG, "manual");
            }
            if (mFormList2.isEmpty()){
                Toast.makeText(MainPageActivity.this, "You have no forms yet", Toast.LENGTH_LONG).show();
                return;
            }
        }

        mForm = mFormList2.get(0);
        Log.d(LOG_TAG, "hi");

        intent.putExtra("SECRET_KEY", SECRET_KEY);
        intent.putExtra("email", getIntent().getExtras().getString("email", "User"));
        intent.putExtra("title", mForm.getTitle());
        intent.putExtra("q1", mForm.getQ1());
        intent.putExtra("q2", mForm.getQ2());
        intent.putExtra("q3", mForm.getQ3());
        intent.putExtra("q4", mForm.getQ4());
        intent.putExtra("q5", mForm.getQ5());
        intent.putExtra("fills", mForm.getFills());
        startActivity(intent);

    }

    public void fill_lowest_fills_form(View view) {
        Intent intent = new Intent(this, FormActivity.class);
        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("forms");
        mFormList2.clear();
        mItems.orderBy("fills", Query.Direction.ASCENDING).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        mFormList2.add(document.toObject(Form.class));
                        Log.d(LOG_TAG,"async");
                    }
                }
            }
        });
        if(mFormList2.isEmpty()){ //sometime async perfomrs nothing, it is a bug because it just never inters the onComplete method
            Task<QuerySnapshot> item = mItems.orderBy("fills", Query.Direction.ASCENDING).limit(1).get();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for( QueryDocumentSnapshot i : item.getResult()){
                mFormList2.add(i.toObject(Form.class));
                intent.putExtra("doc", i.getId());
                Log.d(LOG_TAG,"manual");
            }
        }

        mForm = mFormList2.get(0);

        intent.putExtra("SECRET_KEY", SECRET_KEY);
        intent.putExtra("username",getIntent().getExtras().getString("username", "User"));
        intent.putExtra("title",mForm.getTitle());
        intent.putExtra("q1",mForm.getQ1());
        intent.putExtra("q2",mForm.getQ2());
        intent.putExtra("q3",mForm.getQ3());
        intent.putExtra("q4",mForm.getQ4());
        intent.putExtra("q5",mForm.getQ5());
        intent.putExtra("fills", mForm.getFills());

        startActivity(intent);

    }
}