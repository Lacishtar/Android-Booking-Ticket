package com.tdtu.starrail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tdtu.starrail.R;

public class InfoUserActivity extends AppCompatActivity {

    TextView tv_username;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_user);

        tv_username = findViewById(R.id.username);
        Bundle b = getIntent().getExtras();
        username = b.getString("username");
        tv_username.setText(username);

        Toolbar toolbar = findViewById(R.id.toolbarinforuser);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void logout(View view) {
        Intent intent = new Intent(InfoUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void inforaccount(View view) {
        Intent intent = new Intent(InfoUserActivity.this, InfoAccountActivity.class);
        Bundle b = new Bundle();
        b.putString("username", username);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void historyorder(View view) {
        Intent intent = new Intent(InfoUserActivity.this, HistoryOrderActivity.class);
        Bundle b = new Bundle();
        b.putString("username", username);
        intent.putExtras(b);
        startActivity(intent);
    }
}