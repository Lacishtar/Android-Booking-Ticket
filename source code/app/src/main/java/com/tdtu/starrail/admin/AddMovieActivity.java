package com.tdtu.starrail.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;

import java.util.ArrayList;

public class AddMovieActivity extends AppCompatActivity {
    EditText etName, etAuthor, etPrice, etDescription, etImage1, etImage2, etImage3, etTrailer;
    CheckBox timeSlot9am, timeSlot12pm, timeSlot3pm, timeSlot7pm, timeSlot9pm;
    Spinner spinner;
    Button btn_add;
    Toolbar toolbar;
    private Mydatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);
        etName = findViewById(R.id.edt_movie_name);
        etAuthor = findViewById(R.id.edt_movie_author);
        etPrice = findViewById(R.id.edt_movie_price);
        etDescription = findViewById(R.id.edt_book_description);
        timeSlot9am = findViewById(R.id.checkBox_9am);
        timeSlot12pm = findViewById(R.id.checkBox_12pm);
        timeSlot3pm = findViewById(R.id.checkBox_3pm);
        timeSlot7pm = findViewById(R.id.checkBox_7pm);
        timeSlot9pm = findViewById(R.id.checkBox_9pm);
        etImage1 = findViewById(R.id.imgMovie1);
        etImage2 = findViewById(R.id.imgMovie2);
        etImage3 = findViewById(R.id.imgMovie3);
        etTrailer = findViewById(R.id.edt_trailer_link);
        spinner = findViewById(R.id.spinner_category);
        btn_add = findViewById(R.id.btn_addmovie);
        mydb = new Mydatabase(this);

        toolbar = findViewById(R.id.toolbaraddbook);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ArrayList<String> dscategory = mydb.getAllCategoryNames();
        ArrayAdapter adapterspinner = new ArrayAdapter(this, android.R.layout.simple_spinner_item, dscategory);
        spinner.setAdapter(adapterspinner);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String author = etAuthor.getText().toString();
                String priceStr = etPrice.getText().toString();
                String description = etDescription.getText().toString();
                String trailer = etTrailer.getText().toString();
                String image1 = etImage1.getText().toString();
                String image2 = etImage2.getText().toString();
                String image3 = etImage3.getText().toString();
                boolean cb9am = timeSlot9am.isChecked();
                boolean cb12pm = timeSlot12pm.isChecked();
                boolean cb3pm = timeSlot3pm.isChecked();
                boolean cb7pm = timeSlot7pm.isChecked();
                boolean cb9pm = timeSlot9pm.isChecked();
                int categoryId = (int) spinner.getSelectedItemId() + 1;

                if (name.isEmpty() || author.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(AddMovieActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int price = Integer.parseInt(priceStr);
                    boolean insertMovie = mydb.addMovie(categoryId, name, price, author, description, trailer, image1, image2, image3, cb9am, cb12pm, cb3pm, cb7pm, cb9pm);
                    if (insertMovie) {
                        Toast.makeText(AddMovieActivity.this, "Add Movie successful", Toast.LENGTH_SHORT).show();
                        etName.setText("");
                        etAuthor.setText("");
                        etPrice.setText("");
                        etDescription.setText("");
                        etImage1.setText("");
                        etImage2.setText("");
                        etImage3.setText("");
                        etTrailer.setText("");
                        timeSlot9am.setChecked(false);
                        timeSlot12pm.setChecked(false);
                        timeSlot3pm.setChecked(false);
                        timeSlot7pm.setChecked(false);
                        timeSlot9pm.setChecked(false);
                    } else {
                        Toast.makeText(AddMovieActivity.this, "Add Movie failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public String isCheck(boolean a) {
        return a ? "true" : "false";
    }
}
