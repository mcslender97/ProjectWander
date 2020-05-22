package com.mcslender.wander;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private Button myLogin;
    private EditText myEmail, myPassword;

    //Firebase Authentication entry point
    private FirebaseAuth myAuth;
    //FirebaseAuth listener for change of Login state (login/logout)
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAuth = FirebaseAuth.getInstance();
        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //get current user info to see whether login or logout
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null){
                    Intent intent = new Intent(Login.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        myLogin = (Button) findViewById(R.id.login);
        myEmail = (EditText) findViewById(R.id.email);
        myPassword = (EditText) findViewById(R.id.password);

        myLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = myEmail.getText().toString();
                final String password = myPassword.getText().toString();
                myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){//Shows error if login failed
                            Toast.makeText(Login.this, "Sign in error", Toast.LENGTH_SHORT).show();
                        }
                        if(task.isSuccessful()){//show welcome message
                            Toast.makeText(Login.this, "Login successful", Toast.LENGTH_SHORT).show();
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
