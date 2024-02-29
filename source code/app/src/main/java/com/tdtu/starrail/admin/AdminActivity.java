package com.tdtu.starrail.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tdtu.starrail.R;
import com.tdtu.starrail.activity.LoginActivity;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
    }

    public void dssp(View view) {
        Intent i = new Intent(AdminActivity.this, MoviesListActivity.class);
        startActivity(i);
    }

    public void dsnd(View view) {
        Intent i = new Intent(AdminActivity.this, UsersListActivity.class);
        startActivity(i);
    }

    public void logout(View view) {
        Intent i = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}