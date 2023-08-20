package com.example.friendly_chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {
        DatabaseReference ref;
    EditText email;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        email=findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();

        if(TextUtils.isEmpty(email1)){
            Toast.makeText(this, "Please Enter Email id", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password1)){
            Toast.makeText(this, "Please Enter Your PASSWORDS", Toast.LENGTH_SHORT).show();
            return;
        }
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


        firebaseAuth.signInWithEmailAndPassword(email1,password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                            //Toast.makeText(signinActivity.this, "Successfully Signed In", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SignIn.this, "Could not login, password or email wrong , bullcraps", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void onAuthSuccess(FirebaseUser user) {

        //String username = usernameFromEmail(user.getEmail())
        if (user != null) {
            //Toast.makeText(signinActivity.this, user.getUid(), Toast.LENGTH_SHORT).show();
            ref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("type");
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    //for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Toast.makeText(signinActivity.this, value, Toast.LENGTH_SHORT).show();
                    if (Integer.parseInt(value) == 1) {
                        //String jason = (String) snapshot.getValue();
                        //Toast.makeText(signinActivity.this, jason, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignIn.this, MainActivity.class));
                        Toast.makeText(SignIn.this, "You're Logged in as Seller", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                     //   startActivity(new Intent(SignIn.this, BuyerActivity.class));
                        Toast.makeText(SignIn.this, "You're Logged in as Buyer", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}