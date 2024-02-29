package com.tdtu.starrail.admin;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterMovieList;
import com.tdtu.starrail.adapter.AdapterCateList;
import com.tdtu.starrail.classes.Movie;
import com.tdtu.starrail.classes.Category;

import java.util.ArrayList;

public class MoviesListActivity extends AppCompatActivity {

    private ListView lvMovie;
    private AdapterMovieList adapterMovieList;
    private AdapterCateList adapterCateList;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private ArrayList<Category> cateList = new ArrayList<>();
    private Mydatabase mydb;
    private Toolbar toolbar;
    ImageButton btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_list);
        Anhxa();
        ActionBar();
        movieList = mydb.getAllMovies();
        adapterMovieList = new AdapterMovieList(this, movieList);
        lvMovie.setAdapter(adapterMovieList);

        registerForContextMenu(btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MoviesListActivity.this, view);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();

                        if (itemId == R.id.item_addmovie) {
                            Intent intent = new Intent(MoviesListActivity.this, AddMovieActivity.class);
                            startActivity(intent);
                        } else if (itemId == R.id.item_addcate) {
                            showAddCateDialog();
                        }
                        return false;
                    }
                });
                popupMenu.inflate(R.menu.menu_add);
                popupMenu.show();
            }
        });
        btn_add.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                openContextMenu(v);
                return true;
            }
        });
    }

    private void Anhxa() {
        lvMovie = findViewById(R.id.lvmovieslist);
        mydb = new Mydatabase(this);
        toolbar = findViewById(R.id.toolbarmovieslist);
        btn_add = findViewById(R.id.imgbt_add);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.menu_add);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_showlist, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item_showlistmovie) {
            movieList.clear();
            cateList.clear();
            adapterMovieList.notifyDataSetChanged();
            movieList = mydb.getAllMovies();
            adapterMovieList = new AdapterMovieList(this, movieList);
            lvMovie.setAdapter(adapterMovieList);
        } else if (itemId == R.id.item_showlistcate) {
            movieList.clear();
            cateList.clear();
            adapterMovieList.notifyDataSetChanged();
            cateList = mydb.getAllCategories();
            adapterCateList = new AdapterCateList(this, cateList);
            lvMovie.setAdapter(adapterCateList);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        movieList.clear();
        cateList.clear();
        adapterMovieList.notifyDataSetChanged();
        movieList = mydb.getAllMovies();
        adapterMovieList = new AdapterMovieList(this, movieList);
        lvMovie.setAdapter(adapterMovieList);
    }

    private void showAddCateDialog() {
        ConstraintLayout constraintLayout = findViewById(R.id.AddCateConstraintLayout);
        View view = LayoutInflater.from(MoviesListActivity.this).inflate(R.layout.dialog_addcate, constraintLayout);
        EditText edt_namecate = view.findViewById(R.id.edt_addcate);
        Button btn_add = view.findViewById(R.id.btn_add);

        AlertDialog.Builder builder = new AlertDialog.Builder(MoviesListActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catename = edt_namecate.getText().toString().trim();
                if (catename.isEmpty()) {
                    Toast.makeText(MoviesListActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                } else {
                    boolean result = mydb.addCategory(catename);
                    if (result) {
                        Toast.makeText(MoviesListActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        movieList.clear();
                        cateList.clear();
                        adapterMovieList.notifyDataSetChanged();
                        cateList = mydb.getAllCategories();
                        adapterCateList = new AdapterCateList(MoviesListActivity.this, cateList);
                        lvMovie.setAdapter(adapterCateList);
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(MoviesListActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}
