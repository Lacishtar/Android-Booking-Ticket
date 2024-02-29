package com.tdtu.starrail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterCategoryList;
import com.tdtu.starrail.classes.Movie;

import java.util.ArrayList;
import java.util.List;

public class CategoryListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    Toolbar toolbar;
    Mydatabase mydb;
    String musername;
    int mcategory;
    String mcategoryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrgory_list);
        mydb = new Mydatabase(this);

        toolbar = findViewById(R.id.toolbarcategorylist);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle p = getIntent().getExtras();
        if (p != null) {
            musername = p.getString("username");
            mcategory = p.getInt("categoryID");
            mcategoryname = p.getString("categoryname");
        }
        toolbar.setTitle(mcategoryname);
        recyclerView = findViewById(R.id.rcvcategorylist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Movie> categoryMovies = new ArrayList<>();
        categoryMovies = mydb.getCategory4Movies(mcategory);
        AdapterCategoryList adapter = new AdapterCategoryList((Context) this, (ArrayList<Movie>) categoryMovies);
        AdapterCategoryList.OnItemClickListener listener = new AdapterCategoryList.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(CategoryListActivity.this, DetailMovieActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("movieid", movie.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
    }
}
