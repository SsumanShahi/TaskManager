package com.suman.taskmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.suman.taskmanager.Url.url;
import com.suman.taskmanager.api.Userapi;
import com.suman.taskmanager.model.User;
import com.suman.taskmanager.serverrespone.SignupResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private ImageView img;
    private EditText Firstname, Lastname, username, password, cmpassword;
    private Button btnreg;
    private TextView txtlog;
    private         String imagePath;
    private String imageName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        img = findViewById(R.id.img);
        Firstname = findViewById(R.id.Firstname);
        Lastname = findViewById(R.id.Lastname);
        username=findViewById(R.id.username);
        password = findViewById(R.id.password);
        cmpassword=findViewById(R.id.cmpassword);
        btnreg=findViewById(R.id.btnreg);
        txtlog=findViewById(R.id.txtlog);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browseimage();
            }
        });

        btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(password.getText().toString().equals((cmpassword.getText().toString()))){
//                    saveImageOnly();
                        signup();
                }else
                {
                    Toast.makeText(RegisterActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    password.requestFocus();
                    return;
                }
            }
        });
    }

    private void signup() {

        String fname = Firstname.getText().toString();
        String lname =Lastname.getText().toString();
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        User user = new User(fname, lname, Username, Password);

        Userapi userapi = url.getInstance().create(Userapi.class);
        Call<SignupResponse> signupResponseCall = userapi.resgisterUser(user);

        signupResponseCall.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void browseimage() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(data == null)
            {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }

        Uri uri = data.getData();
        img.setImageURI(uri);
        imagePath = getRealPathFromUri(uri);

//        imageView = getRealPathFromUri(uri);
//        previewImage(imageView);


        //forshortcut

    }

    private String getRealPathFromUri(Uri uri) {

        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null
                                    ,null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
}
