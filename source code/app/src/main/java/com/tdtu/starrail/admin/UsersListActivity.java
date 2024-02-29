package com.tdtu.starrail.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterUsersList;
import com.tdtu.starrail.classes.Users;


import java.util.ArrayList;

public class UsersListActivity extends AppCompatActivity {
    private ListView listView;
    private ArrayList<Users> mUserList;
    private AdapterUsersList adapter;
    private Mydatabase mydb;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        toolbar = findViewById(R.id.toolbaruserslist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mydb = new Mydatabase(this);
        listView = findViewById(R.id.lvuserslist);

        mUserList = mydb.getAllUser();
        adapter = new AdapterUsersList(this, mUserList);

        listView.setAdapter(adapter);
    }
}