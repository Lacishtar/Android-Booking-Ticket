package com.tdtu.starrail.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Users;


public class InfoAccountActivity extends AppCompatActivity {

    EditText edt_hoten, edt_sdt, edt_email;
    AppCompatButton btn_updatein4;
    String username;
    Mydatabase mydb;
    Users user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account);

        Anhxa();

        Toolbar toolbar = findViewById(R.id.toolbarinforaccount);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        user = mydb.getInforAccount(username);
        edt_hoten.setText(user.getName());
        edt_email.setText(user.getEmail());
        if (user.getPhone() == null){
            edt_sdt.setText("");
        }else {
            edt_sdt.setText(user.getPhone());
        }

        btn_updatein4 = findViewById(R.id.btn_updateinfo);
        btn_updatein4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_hoten.getText().toString();
                String email = edt_email.getText().toString();
                String phone = edt_sdt.getText().toString();

                if (1 <= phone.length() && phone.length() <= 9){
                    Toast.makeText(InfoAccountActivity.this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                }else {
                    boolean success = mydb.updateUser(username, name, email, phone);
                    if (success) {
                        Toast.makeText(InfoAccountActivity.this, "Thông tin đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(InfoAccountActivity.this, "Lỗi khi cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private void Anhxa() {
        edt_hoten = findViewById(R.id.edt_hoten);
        edt_sdt = findViewById(R.id.edt_sdt);
        edt_email = findViewById(R.id.edt_email);
        btn_updatein4 = findViewById(R.id.btn_updateinfo);
        mydb = new Mydatabase(this);
        Bundle b = getIntent().getExtras();
        username = b.getString("username");
    }

}