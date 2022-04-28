package com.example.emailauthentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    EditText email,password,name,no;
    Button button;
    ProgressBar progressbar;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        connectxml();
        firestore=firestore.getInstance();
        firebaseAuth=firebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtext = email.getText().toString().trim();
                String passwordtext = password.getText().toString().trim();
                String nametext = name.getText().toString().trim();
                String notext = no.getText().toString().trim();

                if (TextUtils.isEmpty(emailtext)) {
                    email.setError("empty error");
                }
                else if (TextUtils.isEmpty(passwordtext))
                {
                    password.setError("empty error");
                }
                else if (TextUtils.isEmpty(nametext))
                {
                    name.setError("empty error");
                }
                else if (TextUtils.isEmpty(notext))
                {
                    no.setError("empty error");

                }
                else {
                    progressbar.setVisibility(View.VISIBLE);
                    createuser(emailtext,passwordtext,nametext,notext);
//                    Toast.makeText(getApplicationContext(),"operation will show here",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    private void createuser(String email, String password, String name, String no)
    {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    String user=firebaseAuth.getCurrentUser().getUid();
                    HashMap<String,String> usermap=new HashMap<>();
                    usermap.put("email",email);
                    usermap.put("password",password);
                    usermap.put("name",name);
                    usermap.put("no",no);
                    usermap.put("user",user);

                    firestore.collection("login").document(user).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
//                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "successful" , Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                                finish();
                            }
                            else
                            {
//                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "error"+ task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    progressbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "error"+ task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void connectxml() {
        email=findViewById(R.id.emailregister);
        password=findViewById(R.id.passwordregister);
        name=findViewById(R.id.nameregister);
        no=findViewById(R.id.numberregister);
        button=findViewById(R.id.signinbutton);
        progressbar=findViewById(R.id.progress);
    }
}