package com.example.tiketsaya;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterTwoAct extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue, btn_add_photo;
    ImageView pic_photo_register_user;
    EditText bio, nama_lengkap;

    Uri photo_location;
    Integer photo_max = 1;

    DatabaseReference reference;
    private StorageReference storage;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_two);

        getUsernameLocal();

        btn_continue = findViewById(R.id.btn_continue);
        btn_back = findViewById(R.id.btn_back);
        btn_add_photo = findViewById(R.id.btn_add_photo);
        pic_photo_register_user = findViewById(R.id.pic_photo_register_user);
        bio = findViewById(R.id.bio);
        nama_lengkap = findViewById(R.id.nama_lengkap);


        btn_add_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findphoto();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ubah state menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading...");

                final String xxnama_lengkap = nama_lengkap.getText().toString();
                final String xxnohp = bio.getText().toString();

                if (xxnama_lengkap.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Nama kosong", Toast.LENGTH_SHORT).show();
                    // ubah state menjadi loading
                    btn_continue.setEnabled(true);
                    btn_continue.setText("Masuk");
                } else {
                    if (xxnohp.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "No telpon kosong", Toast.LENGTH_SHORT).show();
                        // ubah state menjadi loading
                        btn_continue.setEnabled(true);
                        btn_continue.setText("Masuk");

                    } else {

                        // menyimpan data ke firebase
                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username_key_new);


                        storage = FirebaseStorage.getInstance().getReference().child("Photousers").child(username_key_new);



                        // validasi untuk file (apakah ada?)
                        if (photo_location != null) {
                            final StorageReference storageReference1 =
                                    storage.child(System.currentTimeMillis() + "." +
                                            getFileExtension(photo_location));


                            storageReference1.putFile(photo_location)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    String uri_photo = uri.toString();
                                                    reference.getRef().child("url_photo_profile").setValue(uri_photo);
                                                    reference.getRef().child("nama_lengkap").setValue(nama_lengkap.getText().toString());
                                                    reference.getRef().child("bio").setValue(bio.getText().toString());
                                                }
                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    // berpindah activity
                                                    Intent gotosuccess = new Intent(RegisterTwoAct.this, SuccessRegisterAct.class);
                                                    startActivity(gotosuccess);
                                                }
                                            });

                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    // berpindah activity
                                    //Intent gotosuccess = new Intent(NewAccountTwoAct.this, SuccesRegisterAct.class);startActivity(gotosuccess);
                                }
                            });
                        }
                    }
                }

            }
        });
        // memberikan event pada btn akun baru
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    String getFileExtension (Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    public void findphoto() {
        Intent pic = new Intent();
        pic.setType("image/*");
        pic.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(pic, photo_max);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == photo_max && resultCode == RESULT_OK && data != null && data.getData() !=null)
        {
            photo_location = data.getData();
            Picasso.with(this).load(photo_location).centerCrop().fit().into(pic_photo_register_user);

        }


    }

    public  void getUsernameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }


}
