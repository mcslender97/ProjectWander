package com.mcslender.wander;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOrRegistration extends AppCompatActivity {
    private Button nLogin, nRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_registration);
        nLogin = (Button) findViewById(R.id.login);
        nRegister = (Button) findViewById(R.id.register);

        nLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOrRegistration.this, Login.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        nRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOrRegistration.this, Register.class);
                startActivity(intent);
                finish();
                return;

            }
        });
    }
}
