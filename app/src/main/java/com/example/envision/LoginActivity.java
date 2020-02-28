package com.example.envision;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;

public class LoginActivity extends AppCompatActivity {

    String emailPattern,email;
    EditText emailValidate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailValidate = (EditText)findViewById(R.id.editText);
        email = emailValidate.getText().toString().trim();
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";



    }

    public void clicked(View view) {
        Intent intent=new Intent(LoginActivity.this,OptionActivity.class);
        startActivity(intent);
        finish();
        if (email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                 }
        else
        {
            emailValidate.setError("Invalid email");
        }
    }
}
