package com.example.app1;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileFragment extends Fragment {

    ImageView img_edt,img_profile;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        //***********open Gallery to choose profile picture
        img_profile=v.findViewById(R.id.img_profile);
        img_edt=v.findViewById(R.id.img_edt);

        img_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent(Intent.ACTION_GET_CONTENT);
                in.setType("image/*");
                startActivityForResult(in,1);
            }
        });

        //************* select all info about current user from database
        TextView txt_pname=v.findViewById(R.id.txt_pname);
        TextView txt_cname=v.findViewById(R.id.txt_cname);
        TextView txt_cemail=v.findViewById(R.id.txt_cemail);
        TextView txt_cpassword=v.findViewById(R.id.txt_cpassword);
        TextView txt_about=v.findViewById(R.id.txt_about);
        String uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference().child("users").
                child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name=snapshot.child("name").getValue(String.class);
                    txt_pname.setText("Hi,"+snapshot.child("name").getValue(String.class));
                    txt_cname.setText(snapshot.child("name").getValue(String.class));
                    txt_cemail.setText(snapshot.child("email").getValue(String.class));
                    txt_cpassword.setText(snapshot.child("password").getValue(String.class));
                        Picasso.get()
                                .load(snapshot.child("pic").getValue(String.class))
                                .placeholder(R.drawable.yy2)
                                .error(R.drawable.yy2)
                                .into(img_profile);

                        txt_about.setText(snapshot.child("about").getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        //****************** logout from account
        ImageView img_logout1=v.findViewById(R.id.img_logout1);
        img_logout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent in=new Intent(getContext(), Login.class);
                startActivity(in);
            }
        });

        //Change about of user on click of pencil
        ImageView img_edit=v.findViewById(R.id.img_edit);
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(getContext());
                alert.setTitle("Quote of the day");
                alert.setMessage("What's in your mood");
                EditText input=new EditText(getContext());
                alert.setView(input);
                alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                     HashMap<String,Object> data=new HashMap<>();
                     data.put("about",input.getText().toString());
                     FirebaseDatabase.getInstance().getReference().child("users")
                             .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                             .updateChildren(data);
                        txt_about.setText(input.getText());
                     dialogInterface.dismiss();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            img_profile.setImageURI(data.getData());
            FirebaseStorage.getInstance().getReference().
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .putFile(data.getData()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                  @Override
                                  public void onSuccess(Uri uri) {
                                      HashMap<String,Object> data=new HashMap<>();
                                      data.put("pic",uri.toString());
                                      FirebaseDatabase.getInstance().getReference()
                                              .child("users")
                                              .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                              .updateChildren(data);
                                      Toast.makeText(getContext(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();

                                  }
                              });
                        }
                    });


        }
    }
}