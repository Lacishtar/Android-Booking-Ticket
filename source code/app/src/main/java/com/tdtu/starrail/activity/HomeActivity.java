package com.tdtu.starrail.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterLvNav;
import com.tdtu.starrail.adapter.AdapterRcvCategory;
import com.tdtu.starrail.classes.Movie;
import com.tdtu.starrail.classes.Category;
import com.tdtu.starrail.classes.Loginstatus;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    DrawerLayout drawer_trangchu;
    Toolbar toolbar_trangchu;
    NavigationView nav_trangchu;
    AdapterLvNav adapterLvNav;
    ListView lv_nav;
    private ArrayList<Category> cateList = new ArrayList<>();
    private RecyclerView recyclerViewCategory1;
    private RecyclerView recyclerViewCategory2;
    private RecyclerView recyclerViewCategory3;
    private RecyclerView recyclerViewCategory4;
    private List<ArrayList<Movie>> movieCategories = new ArrayList<>();
    private String musername;
    private Mydatabase mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Anhxa();
        ActionBar();

        setRCV();
        Bundle p = getIntent().getExtras();
        if (p != null) {
            musername = p.getString("username");
        }
        cateList = mydb.getAllCategories();
        adapterLvNav = new AdapterLvNav(this, cateList, musername);
        lv_nav.setAdapter(adapterLvNav);
    }

    private void Anhxa() {
        drawer_trangchu = findViewById(R.id.drawer_trangchu);
        toolbar_trangchu = findViewById(R.id.toolbar_trangchu);
        nav_trangchu = findViewById(R.id.nav_trangchu);
        mydb = new Mydatabase(this);
        lv_nav = findViewById(R.id.lv_nav);
        //Anh xa rcv
        recyclerViewCategory1 = findViewById(R.id.recyclerViewCategory1);
        recyclerViewCategory2 = findViewById(R.id.recyclerViewCategory2);
        recyclerViewCategory3 = findViewById(R.id.recyclerViewCategory3);
        recyclerViewCategory4 = findViewById(R.id.recyclerViewCategory4);
    }

    private void setRCV() {
        //Thiết lập LayoutManager cho rcv
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategory1.setLayoutManager(layoutManager1);
        recyclerViewCategory2.setLayoutManager(layoutManager2);
        recyclerViewCategory3.setLayoutManager(layoutManager3);
        recyclerViewCategory4.setLayoutManager(layoutManager4);
        //
        List<Movie> category1Movies = new ArrayList<>();
        List<Movie> category2Movies = new ArrayList<>();
        List<Movie> category3Movies = new ArrayList<>();
        List<Movie> category4Movies = new ArrayList<>();
        //
        category1Movies = mydb.getCategory4Movies(1); // Lấy sách theo category 1
        category2Movies = mydb.getCategory4Movies(2); // Lấy sách theo category 2
        category3Movies = mydb.getCategory4Movies(3); // Lấy sách theo category 3
        category4Movies = mydb.getCategory4Movies(4); // Lấy sách theo category 4
        //
        movieCategories.add((ArrayList<Movie>) category1Movies);
        movieCategories.add((ArrayList<Movie>) category2Movies);
        movieCategories.add((ArrayList<Movie>) category3Movies);
        movieCategories.add((ArrayList<Movie>) category4Movies);
        //
        AdapterRcvCategory adapter1 = new AdapterRcvCategory((Context) this, (ArrayList<Movie>) category1Movies);
        AdapterRcvCategory adapter2 = new AdapterRcvCategory((Context) this, (ArrayList<Movie>) category2Movies);
        AdapterRcvCategory adapter3 = new AdapterRcvCategory((Context) this, (ArrayList<Movie>) category3Movies);
        AdapterRcvCategory adapter4 = new AdapterRcvCategory((Context) this, (ArrayList<Movie>) category4Movies);
        //
        AdapterRcvCategory.OnItemClickListener listener = new AdapterRcvCategory.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent intent = new Intent(HomeActivity.this, DetailMovieActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                b.putInt("movieid", movie.getId());
                intent.putExtras(b);
                startActivity(intent);
            }
        };
        //
        adapter1.setOnItemClickListener(listener);
        adapter2.setOnItemClickListener(listener);
        adapter3.setOnItemClickListener(listener);
        adapter4.setOnItemClickListener(listener);
        //
        recyclerViewCategory1.setAdapter(adapter1);
        recyclerViewCategory2.setAdapter(adapter2);
        recyclerViewCategory3.setAdapter(adapter3);
        recyclerViewCategory4.setAdapter(adapter4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.search_item) {
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            if (musername != null) {
                Bundle b = new Bundle();
                b.putString("username", musername);
                intent.putExtras(b);
            }
            startActivity(intent);
        } else if (itemId == R.id.accout_item) {
            if (Loginstatus.getInstance().isLoggedIn() == true) {
                Intent i = new Intent(HomeActivity.this, InfoUserActivity.class);
                if (musername != null) {
                    Bundle b = new Bundle();
                    b.putString("username", musername);
                    i.putExtras(b);
                }
                startActivity(i);
            } else {
                Toast.makeText(this, "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar_trangchu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setTitle("");
        toolbar_trangchu.setNavigationIcon(R.drawable.ic_widgets);
        toolbar_trangchu.inflateMenu(R.menu.menu_toolbar);
        toolbar_trangchu.setTitle(null);

        toolbar_trangchu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_trangchu.openDrawer(GravityCompat.START);
            }
        });
    }
}
