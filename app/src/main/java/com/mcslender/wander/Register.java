package com.mcslender.wander;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class    Register extends AppCompatActivity {
    public static final String GENERIC_USER_IMAGE = "https://icon-library.net/images/default-profile-icon/default-profile-icon-5.jpg";
    private Button myRegiser;
    private EditText myEmail, myPassword, myName;

    private RadioGroup myRadioGroup;

    //Firebase Authentication entry point
    private FirebaseAuth myAuth;
    //FirebaseAuth listener for change of Login state (login/logout)
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //get current user info to see whether login or logout
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Register.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
                else {
                    //do nothing
                }
            }
        };

        myRegiser = (Button) findViewById(R.id.register);
        myName = (EditText) findViewById(R.id.name);
        myEmail = (EditText) findViewById(R.id.email);
        myPassword = (EditText) findViewById(R.id.email);
        myRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        myRegiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectId = myRadioGroup.getCheckedRadioButtonId();
                final RadioButton radioButton = (RadioButton) findViewById(selectId);

                if(radioButton.getText() == null){
                    return;
                }

                final String email = myEmail.getText().toString();
                final String password = myPassword.getText().toString();
                final String name = myName.getText().toString();
                myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){//Shows error if registration failed
                            Toast.makeText(Register.this, "Sign up error", Toast.LENGTH_SHORT);
                        }else{
                            String userId = myAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(radioButton.getText().toString()).child(userId);
                            Map userInfo = new HashMap<>();
                            userInfo.put("name", name);

                            userInfo.put("profileImageUrl", GENERIC_USER_IMAGE);
                            //userInfo.put("profileImageUrl", "default");



                            currentUserDb.updateChildren(userInfo);
                        }
                    }
                });
            }
        });
    }
    @Override
    protected void onStart(){//start the Firebase listener when program starts
        super.onStart();
        myAuth.addAuthStateListener(firebaseAuthStateListener);
    }
    @Override
    protected void onStop(){//stop the Firebase listener
        super.onStop();//
        myAuth.removeAuthStateListener(firebaseAuthStateListener);
    }
}
