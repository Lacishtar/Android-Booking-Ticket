package com.tdtu.starrail.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.tdtu.starrail.R;
import com.tdtu.starrail.classes.Category;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Movie;

import java.util.ArrayList;

public class AdapterMovieList extends BaseAdapter {
    private Context context;
    private ArrayList<Movie> movieList;
    Mydatabase mydb;

    public AdapterMovieList(Context context, ArrayList<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movieList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_item_listmovies, null);

        ImageView imgMovie = view.findViewById(R.id.imageView);
        TextView tvName = view.findViewById(R.id.tv_nameb);
        TextView tvPrice = view.findViewById(R.id.tv_priceb);
        Button btnDelete = view.findViewById(R.id.btn_xoasach);
        Button btnEdit = view.findViewById(R.id.btn_suasach);

        final Movie movie = movieList.get(position);

        tvName.setText(movie.getName());
        tvPrice.setText(String.format("%,d", movie.getPrice()) + " VNĐ");

        Glide.with(context)
                .load("file:///android_asset/" + movie.getImage1())
                .into(imgMovie);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(position);
            }
        });

        return view;
    }

    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete movie");
        builder.setMessage("Bạn có chắc muốn xóa phim?");

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mydatabase mydb = new Mydatabase(context);
                mydb.deleteMovie(movieList.get(position).getId());
                movieList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Xóa phim thành công!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showEditDialog(final int position) {
        mydb = new Mydatabase(context);
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_dialog_edit_movie);
        dialog.setTitle("Edit movie");

        final EditText edtName = dialog.findViewById(R.id.edt_movie_name);
        final EditText edtPrice = dialog.findViewById(R.id.edt_movie_price);
        final EditText edtAuthor = dialog.findViewById(R.id.edt_movie_author);
        final EditText edtDescription = dialog.findViewById(R.id.edt_movie_description);
        final EditText edtTrailer = dialog.findViewById(R.id.edt_trailer_link);
        final EditText imgMovie1 = dialog.findViewById(R.id.imgMovie1);
        final EditText imgMovie2 = dialog.findViewById(R.id.imgMovie2);
        final EditText imgMovie3 = dialog.findViewById(R.id.imgMovie3);
        final CheckBox timeslot1 = dialog.findViewById(R.id.checkBox_9am);
        final CheckBox timeslot2 = dialog.findViewById(R.id.checkBox_12pm);
        final CheckBox timeslot3 = dialog.findViewById(R.id.checkBox_3pm);
        final CheckBox timeslot4 = dialog.findViewById(R.id.checkBox_7pm);
        final CheckBox timeslot5 = dialog.findViewById(R.id.checkBox_9pm);
        final Spinner spinner = dialog.findViewById(R.id.spinner_category);

        ArrayList<String> list = new ArrayList<String>();
        ArrayList<Category> categories = mydb.getAllCategories();
        for (Category category : categories) {
            list.add(category.getCatename());
        }
        ArrayAdapter adapterspinner = new ArrayAdapter(context, android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(adapterspinner);

        final Movie movie = movieList.get(position);
        int movieId = movie.getId();

        Movie updatedMovie = mydb.getMovieById(movieId);

        edtName.setText(updatedMovie.getName());
        edtPrice.setText(String.valueOf(updatedMovie.getPrice()));
        edtAuthor.setText(updatedMovie.getAuthor());
        edtDescription.setText(updatedMovie.getDescription());
        edtTrailer.setText(updatedMovie.getTrailer());
        imgMovie1.setText(updatedMovie.getImage1());
        imgMovie2.setText(updatedMovie.getImage2());
        imgMovie3.setText(updatedMovie.getImage3());
        boolean ts1 = updatedMovie.isTime_slot1();
        boolean ts2 = updatedMovie.isTime_slot2();
        boolean ts3 = updatedMovie.isTime_slot3();
        boolean ts4 = updatedMovie.isTime_slot4();
        boolean ts5 = updatedMovie.isTime_slot5();
        timeslot1.setChecked(ts1);
        timeslot2.setChecked(ts2);
        timeslot3.setChecked(ts3);
        timeslot4.setChecked(ts4);
        timeslot5.setChecked(ts5);

        int categoryId = updatedMovie.getCategoryId();
        int selectedIndex = Math.max(0, Math.min(categoryId - 1, categories.size() - 1));
        spinner.setSelection(selectedIndex);

        Button btnSave = dialog.findViewById(R.id.btnsave);
        Button btnCancel = dialog.findViewById(R.id.btncancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtName.getText().toString().trim();
                String author = edtAuthor.getText().toString().trim();
                String priceStr = edtPrice.getText().toString().trim();
                String description = edtDescription.getText().toString().trim();
                String trailer = edtTrailer.getText().toString().trim();
                String image1 = imgMovie1.getText().toString().trim();
                String image2 = imgMovie2.getText().toString().trim();
                String image3 = imgMovie3.getText().toString().trim();
                boolean ts1 = timeslot1.isChecked();
                boolean ts2 = timeslot2.isChecked();
                boolean ts3 = timeslot3.isChecked();
                boolean ts4 = timeslot4.isChecked();
                boolean ts5 = timeslot5.isChecked();

                int categoryId = (int) spinner.getSelectedItemId() + 1;
                if (name.isEmpty() || author.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    int price = Integer.parseInt(priceStr);
                    boolean updateMovie = mydb.updateMovie(movieId, categoryId, name, price, author, description, trailer, image1, image2, image3, ts1, ts2, ts3, ts4, ts5);
                    if (updateMovie) {
                        movieList.set(position, mydb.getMovieById(movieId));
                        Toast.makeText(context, "Cập nhật phim thành công", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Cập nhật phim thất bại", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
