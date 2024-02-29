package com.tdtu.starrail.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.admin.AdminActivity;
import com.tdtu.starrail.classes.Loginstatus;


import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText medt_username,medt_password;
    Button btn_login, btn_reg;

    public static Mydatabase mydb;
    Spinner spinner;
    public static final String[] languages = {"Select Language", "English", "Vietnamese", "Japanese"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Anhxa();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLang = adapterView.getItemAtPosition(i).toString();
                if (selectedLang.equals("English")) {
                    setLocal(LoginActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                } else if (selectedLang.equals("Vietnamese")) {
                    setLocal(LoginActivity.this, "vi");
                    finish();
                    startActivity(getIntent());
                }else if (selectedLang.equals("Japanese")) {
                    setLocal(LoginActivity.this, "ja");
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                String username = medt_username.getText().toString();
                String pass = medt_password.getText().toString();
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pass))
                {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("admin") && pass.equals("admin"))
                {
                    Intent i = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(i);
                }
                else
                {
                    Boolean checkuseracount = mydb.checkUserAccout(username,pass);
                    if (checkuseracount == true)
                    {
                        Loginstatus.getInstance().setLoggedIn(true);
                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        Bundle b = new Bundle();
                        b.putString("username", username);
                        i.putExtras(b);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login failed ", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    public void setLocal(Activity activity, String langcode)
    {
        Locale locale = new Locale(langcode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    private void Anhxa() {
        btn_login = findViewById(R.id.btn_login);
        btn_reg = findViewById(R.id.btn_reg);
        medt_username = findViewById(R.id.edt_uesrname);
        medt_password = findViewById(R.id.edt_password);
        mydb = new Mydatabase(this);
        spinner = findViewById(R.id.spinner_lang);
    }
}