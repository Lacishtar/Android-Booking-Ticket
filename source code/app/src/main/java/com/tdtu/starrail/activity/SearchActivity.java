package com.tdtu.starrail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterRcvSearch;
import com.tdtu.starrail.classes.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private AdapterRcvSearch adapter;
    private List<Movie> movieList;
    private Mydatabase mydb;
    private SearchView searchView;
    private RecyclerView rcv_searchmovies;
    Toolbar toolbar;
    String musername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Anhxa();

        mydb = new Mydatabase(this);
        movieList = mydb.getAllMovies();
        adapter = new AdapterRcvSearch(movieList, this);
        rcv_searchmovies.setAdapter(adapter);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        searchView.requestFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Movie> filteredList = filter(movieList, newText);
                adapter.setData(filteredList);
                adapter.notifyDataSetChanged();

                return true;
            }
        });
        Bundle p = getIntent().getExtras();
        if (p != null) {
            musername = p.getString("username");
        }
        adapter.setOnItemClickListener(new AdapterRcvSearch.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(SearchActivity.this, DetailMovieActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("movieid", movie.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void Anhxa() {
        searchView = findViewById(R.id.sv_movies);
        rcv_searchmovies = findViewById(R.id.rcv_searchmovie);
        toolbar = findViewById(R.id.toolbarsearch);
        rcv_searchmovies.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<Movie> filter(List<Movie> movies, String query) {
        query = query.toLowerCase().trim();

        final List<Movie> filteredList = new ArrayList<>();
        for (Movie movie : movies) {
            if (movie.getName().toLowerCase().contains(query)) {
                filteredList.add(movie);
            }
        }
        return filteredList;
    }
}
