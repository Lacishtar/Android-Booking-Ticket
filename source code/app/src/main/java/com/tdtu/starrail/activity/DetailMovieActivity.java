package com.tdtu.starrail.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.DatabaseHelper;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterVpgDetailMovie;
import com.tdtu.starrail.classes.Movie;
import com.tdtu.starrail.classes.Loginstatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailMovieActivity extends AppCompatActivity {
    Toolbar toolbar;
    private String musername;
    private String trailer_link;
    private int mmovieid;
    private Mydatabase mydb;
    private DatabaseHelper dbhelper;
    private TextView namemovie, pricemovie, authormovie, describemovie;
    private TextView selectedDate;
    private Calendar maxAllowedDate;
    private Spinner spinnerTime;
    private Calendar selectedCalendar;
    ViewPager viewPager;
    Button btn_book, btn_trailer;

    private List<String> timeOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Anhxa();

        dbhelper = new DatabaseHelper(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        timeOptions = new ArrayList<>();

        Bundle p = getIntent().getExtras();
        if (p != null) {
            musername = p.getString("username");
            mmovieid = p.getInt("movieid");
        }
        Movie movie = mydb.getMovieById(mmovieid);
        if (movie != null) {
            namemovie.setText(movie.getName());
            pricemovie.setText(String.format("%,d", movie.getPrice()) + " VNĐ");
            authormovie.setText(movie.getAuthor());
            describemovie.setText(movie.getDescription());
            trailer_link = movie.getTrailer();
            if (trailer_link != null && trailer_link.endsWith("&t") && trailer_link.startsWith("www")) {
                trailer_link = "https://" + trailer_link.substring(0, trailer_link.length() - 2).trim();
            }
            boolean ts1 = movie.isTime_slot1();
            boolean ts2 = movie.isTime_slot2();
            boolean ts3 = movie.isTime_slot3();
            boolean ts4 = movie.isTime_slot4();
            boolean ts5 = movie.isTime_slot5();

            if (movie.isTime_slot1()) {
                timeOptions.add("9:00 AM");
            }
            if (movie.isTime_slot2()) {
                timeOptions.add("12:00 PM");
            }
            if (movie.isTime_slot3()) {
                timeOptions.add("3:00 PM");
            }
            if (movie.isTime_slot4()) {
                timeOptions.add("7:00 PM");
            }
            if (movie.isTime_slot5()) {
                timeOptions.add("9:00 PM");
            }
        }

        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(movie.getImage1());
        imageUrls.add(movie.getImage2());
        imageUrls.add(movie.getImage3());
        AdapterVpgDetailMovie adapter = new AdapterVpgDetailMovie(this, imageUrls);
        viewPager.setAdapter(adapter);

        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, timeOptions);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailMovieActivity.this, SeatSelectionActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("movieid", movie.getId());
                b.putInt("mseatprice", movie.getPrice());
                b.putString("selectedDate", selectedDate.getText().toString());
                b.putString("selectedTime", spinnerTime.getSelectedItem().toString());
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btn_trailer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (trailer_link != null && !trailer_link.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailer_link));
                    intent.putExtra("force_fullscreen", true);
                    startActivity(intent);
                } else {
                    Toast.makeText(DetailMovieActivity.this, "Trailer link is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedTime = timeOptions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        Calendar currentDate = Calendar.getInstance();
        int year = currentDate.get(Calendar.YEAR);
        int month = currentDate.get(Calendar.MONTH);
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        // Initialize maxAllowedDate before using it
        Calendar maxAllowedDate = Calendar.getInstance();
        maxAllowedDate.add(Calendar.DAY_OF_MONTH, 7); // Allow selection up to 6 days including today

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDateCalendar = Calendar.getInstance();
                        selectedDateCalendar.set(year, monthOfYear, dayOfMonth);

                        // Check if selected date is within the allowed range
                        if (selectedDateCalendar.after(currentDate) && selectedDateCalendar.before(maxAllowedDate)) {
                            selectedCalendar = selectedDateCalendar;

                            // Update the selected date TextView
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                            selectedDate.setText(dateFormat.format(selectedCalendar.getTime()));
                            btn_book.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(DetailMovieActivity.this, "Please choose a date within today and the next 7 days", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                year, month, day);

        // Set the date picker's min and max date
        datePickerDialog.getDatePicker().setMinDate(currentDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxAllowedDate.getTimeInMillis());

        datePickerDialog.show();
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbardetailmovie);
        namemovie = findViewById(R.id.tv_namemovie);
        pricemovie = findViewById(R.id.tv_pricemovie);
        authormovie = findViewById(R.id.tv_authormovie);
        describemovie = findViewById(R.id.tv_describemovie);
        viewPager = findViewById(R.id.vpg_imagemovie);
        btn_book = findViewById(R.id.btn_book);
        btn_trailer = findViewById(R.id.btn_trailer);
        mydb = new Mydatabase(this);
        selectedDate = findViewById(R.id.tv_selected_date);
        spinnerTime = findViewById(R.id.spinner_time);
        selectedCalendar = Calendar.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detailmovie, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.accout_item) {
            if (Loginstatus.getInstance().isLoggedIn()) {
                Intent i = new Intent(DetailMovieActivity.this, InfoUserActivity.class);
                if (musername != null) {
                    Bundle b = new Bundle();
                    b.putString("username", musername);
                    i.putExtras(b);
                }
                startActivity(i);
            } else {
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailMovieActivity.this, LoginActivity.class);
                startActivity(i);
            }
        } else if (itemId == R.id.share_item) {
            // Handle share_item
            if (Loginstatus.getInstance().isLoggedIn()) {
                Movie movie = mydb.getMovieById(mmovieid);
                String movieInformation = "Movie Title: " + movie.getName() + "\nPrice: " + movie.getPrice() + " VND"
                        + "\nDescription: " + movie.getDescription() + "\nCome and watch with me at Star Rail Theatre";

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, movieInformation);

                startActivity(Intent.createChooser(shareIntent, "Share Movie Information"));
            } else {
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(DetailMovieActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
