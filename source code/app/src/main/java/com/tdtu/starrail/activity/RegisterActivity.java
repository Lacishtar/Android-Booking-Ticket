package com.tdtu.starrail.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;

public class RegisterActivity extends AppCompatActivity {
    public  static  Mydatabase mydb;
    EditText medt_username,medt_password, medt_cpassword;
    Button mbtn_regis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
         medt_username = findViewById(R.id.edt_uesrname);
         medt_password = findViewById(R.id.edt_password);
         medt_cpassword = findViewById(R.id.edt_cpassword);
         mbtn_regis = findViewById(R.id.btn_reg);
         mydb = new Mydatabase(this);

         mbtn_regis.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 String username = medt_username.getText().toString().trim();
                 String password = medt_password.getText().toString().trim();
                 String cpassword = medt_cpassword.getText().toString().trim();
                 if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password))
                 {
                     Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                 }
                 else if(username.length() < 8 || username.length() > 32)
                 {
                     Toast.makeText(RegisterActivity.this, "Username length must be between 8 and 32 characters", Toast.LENGTH_SHORT).show();
                 }
                 else if(password.length() < 8)
                 {
                     Toast.makeText(RegisterActivity.this, "Your password too weak. Please choose other password which longer than 8 character", Toast.LENGTH_SHORT).show();
                 }
                 else if (username.equals("admin") && password.equals("admin"))
                 {
                     Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                 }
                 else if(!cpassword.equals(password))
                 {
                     Toast.makeText(RegisterActivity.this, "Confirm Password doesn't match", Toast.LENGTH_SHORT).show();
                 }
                 else {
                     Boolean checkusername = mydb.checkUserName(username);
                     if (checkusername == false)
                     {
                         Boolean insert = mydb.addUserAccout(username, password);
                         if (insert = true)
                         {
                             Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                             onBackPressed();
                         }
                         else {
                             Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                         }
                     }else {
                         Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
         });
    }

    public void backlogin(View view) {
        onBackPressed();
    }
}