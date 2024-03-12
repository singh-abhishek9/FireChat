package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
TextView txtlogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtlogin=findViewById(R.id.txtlogin);
        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(MainActivity.this, Login.class);
                startActivity(in);
            }
        });
        //-----click here to register
        ImageView img_register=findViewById(R.id.img_register);
        img_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // FirebaseDatabase.getInstance().getReference().setValue("Techpile Technology Pvt. Ltd.");
                // Toast.makeText(MainActivity.this,"clicked",Toast.LENGTH_LONG).show();
                EditText txtname = findViewById(R.id.txtname);
                EditText txtemail = findViewById(R.id.txtemail);
                EditText txtpassword = findViewById(R.id.txtpassword);
                String name = txtname.getText().toString();
                String email = txtemail.getText().toString();
                String password = txtpassword.getText().toString();
                RadioGroup rdbgender = findViewById(R.id.rdb_gender);
                RadioButton rdbselectedgen = findViewById(rdbgender.getCheckedRadioButtonId());
                String gender = rdbselectedgen.getText().toString();
                //let"s save the data in authentication and realtime database
                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() &&
                        !gender.isEmpty()) {
                    ProgressDialog dialog=new ProgressDialog(MainActivity.this);
                    dialog.setTitle("Please Wait...");
                    dialog.show();

                    FirebaseAuth.getInstance()
                            .createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dialog.dismiss();
                                    if (task.isSuccessful()) {

                                        //--------- Download UID of user from firebase auth

                                        String uid = task.getResult().getUser().getUid();
                                        HashMap<String, String> user = new HashMap<>();
                                        user.put("name", name);
                                        user.put("email", email);
                                        user.put("password", password);
                                        user.put("gender", gender);

                                        FirebaseDatabase.getInstance().getReference().child("users").child(uid)
                                                .setValue(user);

                                        Toast.makeText(MainActivity.this, "Now! You are registered",
                                                Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        Intent in = new Intent(MainActivity.this, Login.class);
                                        startActivity(in);
                                    } else {
                                        Toast.makeText(MainActivity.this,
                                                task.getException().getMessage() + "", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(MainActivity.this, "Please fill this all properly",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}