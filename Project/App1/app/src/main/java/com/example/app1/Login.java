package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
   TextView txtregister;
   ImageView img_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtregister=findViewById(R.id.txtregister);
        img_login=findViewById(R.id.img_login);
        //---------------------Click Here To Register
        txtregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Login.this,MainActivity.class);
                startActivity(in);
            }
        });
        //---------------------Click Here To Login
        EditText loginemail=findViewById(R.id.loginemail);
        EditText loginpassword=findViewById(R.id.loginpassword);


        img_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=loginemail.getText().toString();
                String password=loginpassword.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener
                            (new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                               Intent in=new Intent(Login.this, ChatBox.class);
                               startActivity(in);
                            }
                            else{

                                Toast.makeText(Login.this,"Please Register Yourself First",Toast.LENGTH_LONG).show();;
                            }
                        }
                    });
                }
                else{

                    Toast.makeText(Login.this,"Please Fill All Fields Properly",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            Intent in=new Intent(this, ChatBox.class);
            startActivity(in);
        }
    }
}