package com.example.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {

    Button btn_continue;
    LinearLayout btn_back;
    EditText username, password, email_address;
    DatabaseReference reference_username,reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);
        btn_back = findViewById(R.id.btn_back);

        btn_continue = findViewById(R.id.btn_continue);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // membrikan id xml
        btn_continue = findViewById(R.id.btn_continue);
        // memberikan event pada btn akun baru
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                //mengambil user name dari database
                reference_username = FirebaseDatabase.getInstance().getReference().child("Users").
                        child(username.getText().toString());
                reference_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //jika username tersedia
                        if (dataSnapshot.exists()) {
                            Toast.makeText(getApplicationContext(), " Username Sudah Tersedia", Toast.LENGTH_SHORT).show();

                            // merubah state jadi avtive
                            btn_continue.setEnabled(true);
                            btn_continue.setText("CONTINUE");

                        } else {

                            // ubah state menjadi loading
                            btn_continue.setEnabled(false);
                            btn_continue.setText("Loading...");
                            // cek database
                            final String xxusername = username.getText().toString();
                            final String xxpassword = password.getText().toString();
                            final String xxemail_address = email_address.getText().toString();

                            if (xxusername.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "username kosong", Toast.LENGTH_SHORT).show();
                                // ubah state menjadi loading
                                btn_continue.setEnabled(true);
                                btn_continue.setText("Masuk");
                            } else {
                                if (xxpassword.isEmpty()) {
                                    Toast.makeText(getApplicationContext(), "Password kosong", Toast.LENGTH_SHORT).show();
                                    // ubah state menjadi loading
                                    btn_continue.setEnabled(true);
                                    btn_continue.setText("Masuk");

                                } else {
                                    if (xxemail_address.isEmpty()) {
                                        Toast.makeText(getApplicationContext(), "Email address kosong", Toast.LENGTH_SHORT).show();
                                        // ubah state menjadi loading
                                        btn_continue.setEnabled(true);
                                        btn_continue.setText("Masuk");

                                    } else {

                                        // menyimpan dta kepada lokal storage (handphone)
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, username.getText().toString());
                                        editor.apply();

                                        //simpan kepada database
                                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(username.getText().toString());
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                // memasukan data pada database
                                                dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                                                dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                                                dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                                                dataSnapshot.getRef().child("user_balance").setValue(800);


                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        //menuju halaman lain
                                        Intent gotonewakun = new Intent(RegisterOneAct.this, RegisterTwoAct.class);
                                        startActivity(gotonewakun);
                                    }
                                }
                            }

                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}

