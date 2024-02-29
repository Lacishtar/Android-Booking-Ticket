package com.tdtu.starrail.sqlHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.tdtu.starrail.activity.CategoryListActivity;
import com.tdtu.starrail.classes.Movie;
import com.tdtu.starrail.classes.Category;
import com.tdtu.starrail.classes.HistoryOrder;
import com.tdtu.starrail.classes.OrderDetail;
import com.tdtu.starrail.classes.Seat;
import com.tdtu.starrail.classes.Users;

import java.util.ArrayList;
import java.util.List;

public class Mydatabase {
    SQLiteDatabase db;
    DatabaseHelper DBhelper;

    public Mydatabase(Context context) {
        DBhelper = new DatabaseHelper(context);
        db = DBhelper.getWritableDatabase();
    }

    //Thêm tài khoản (Đăng Ký)
    public boolean addUserAccout(String username, String password) {
        ContentValues values = new ContentValues();

        values.put(DBhelper.COLUMN_USER_USERNAME, username);
        values.put(DBhelper.COLUMN_USER_PASSWORD, password);
        long result = db.insert(DBhelper.TABLE_USERS, null, values);
        if (result == -1)
            return false;
        else
            return true;
    }

    //Kiểm tra tài khoản (Đăng Nhập)
    public boolean checkUserAccout(String username, String password) {
        String query = "SELECT * FROM " + DBhelper.TABLE_USERS + " WHERE " +
                DBhelper.COLUMN_USER_USERNAME + "=? AND " + DBhelper.COLUMN_USER_PASSWORD + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //Kiểm tra tài khoản đã tồn tại hay chưa
    public boolean checkUserName(String username) {
        String query = "SELECT * FROM " + DBhelper.TABLE_USERS + " WHERE " +
                DBhelper.COLUMN_USER_USERNAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    //Xóa người dùng
    public boolean deleteuser(long userId) {
        int result = db.delete(DBhelper.TABLE_USERS, DBhelper.COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        if (result > 0) {
            // xóa thành công
            return true;
        } else {
            // xóa không thành công
            return false;
        }
    }

    //Xóa sách
    public boolean deleteMovie(int movieId) {
        int result = db.delete(DBhelper.TABLE_MOVIES, DBhelper.COLUMN_MOVIE_ID + " = ?",
                new String[]{String.valueOf(movieId)});
        return result > 0;
    }

    //Sửa sách
    public boolean updateMovie(int movieid, int categoryId, String movieName, int moviePrice, String movieDirector, String description, String trailer,
                               String image1, String image2, String image3, boolean ts1, boolean ts2, boolean ts3, boolean ts4, boolean ts5) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MOVIE_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_MOVIE_NAME, movieName);
        values.put(DatabaseHelper.COLUMN_MOVIE_DIRECTOR, movieDirector);
        values.put(DatabaseHelper.COLUMN_MOVIE_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_MOVIE_TRAILER, trailer);
        values.put(DatabaseHelper.COLUMN_MOVIE_PRICE, moviePrice);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_1, image1);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_2, image2);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_3, image3);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION1, ts1);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION2, ts2);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION3, ts3);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION4, ts4);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION5, ts5);

        int result = db.update(DBhelper.TABLE_MOVIES, values, DBhelper.COLUMN_MOVIE_ID + " = ?", new String[]{String.valueOf(movieid)});

        return result != -1;
    }

    public boolean addSeat(Seat seat, int movieId, String selectedDate, String selectedTime) {
        ContentValues values = new ContentValues();
        values.put(DBhelper.COLUMN_SEAT_SEAT_NUMBER, seat.getSeatNumber());
        values.put(DBhelper.COLUMN_SEAT_ISSLECTED, seat.isSelected() ? 1 : 0);
        values.put(DBhelper.COLUMN_SEAT_MOVIE_ID, movieId);
        values.put(DBhelper.COLUMN_SEAT_DATE, selectedDate);
        values.put(DBhelper.COLUMN_SEAT_TIME, selectedTime);

        long result = db.insert(DatabaseHelper.TABLE_SEATS, null, values);
        return result != -1;
    }

    public boolean isSeatBooked(String seatNumber, int movieId, String selectedDate, String selectedTime) {
        String query = "SELECT * FROM " + DBhelper.TABLE_SEATS + " WHERE " +
                DBhelper.COLUMN_SEAT_SEAT_NUMBER + "=? AND " +
                DBhelper.COLUMN_SEAT_MOVIE_ID + "=? AND " +
                DBhelper.COLUMN_SEAT_DATE + "=? AND " +
                DBhelper.COLUMN_SEAT_TIME + "=? AND " +
                DBhelper.COLUMN_SEAT_ISSLECTED + "=?";

        Cursor cursor = db.rawQuery(query, new String[]{seatNumber, String.valueOf(movieId), selectedDate, selectedTime, "1"});
        boolean isBooked = cursor.getCount() > 0;
        cursor.close();
        return isBooked;
    }

    public List<Seat> getSeatsData(int movieId, String selectedDate, String selectedTime) {
        List<Seat> seatList = new ArrayList<>();

        String selection = DatabaseHelper.COLUMN_SEAT_MOVIE_ID + "=? AND "
                + DatabaseHelper.COLUMN_SEAT_DATE + "=? AND "
                + DatabaseHelper.COLUMN_SEAT_TIME + "=?";
        String[] selectionArgs = {String.valueOf(movieId), selectedDate, selectedTime};

        Cursor cursor = db.query(DBhelper.TABLE_SEATS, null, selection, selectionArgs, null, null, null);

        int columnIndexSeatNumber = cursor.getColumnIndex(DatabaseHelper.COLUMN_SEAT_SEAT_NUMBER);
        int columnIndexSeatSelected = cursor.getColumnIndex(DatabaseHelper.COLUMN_SEAT_ISSLECTED);

        if (cursor.moveToFirst()) {
            do {
                if (columnIndexSeatNumber != -1 && columnIndexSeatSelected != -1) {
                    String seatNumber = cursor.getString(columnIndexSeatNumber);
                    boolean isSelected = cursor.getInt(columnIndexSeatSelected) == 1;

                    Seat seat = new Seat();
                    seat.setSeatNumber(seatNumber);
                    seat.setSelected(isSelected);

                    seatList.add(seat);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return seatList;
    }

    public List<Movie> getCategory4Movies(int categoryId) {
        List<Movie> movieList = new ArrayList<>();
        String query = "SELECT * FROM " + DBhelper.TABLE_MOVIES + " WHERE " + DBhelper.COLUMN_MOVIE_CATEGORY_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(categoryId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int movieCategoryId = cursor.getInt(1);
                String name = cursor.getString(2);
                String director = cursor.getString(3);
                String description = cursor.getString(4);
                String trailer = cursor.getString(5);
                int price = cursor.getInt(6);
                String image1 = cursor.getString(7);
                String image2 = cursor.getString(8);
                String image3 = cursor.getString(9);
                int ts1 = cursor.getInt(10);
                int ts2 = cursor.getInt(11);
                int ts3 = cursor.getInt(12);
                int ts4 = cursor.getInt(13);
                int ts5 = cursor.getInt(14);
                Movie movie = new Movie(id, movieCategoryId, name, price, director, description, trailer, image1, image2, image3, ts1, ts2, ts3, ts4, ts5);
                movieList.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movieList;
    }

    public ArrayList<Movie> getAllMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();

        Cursor cursor = db.query(DBhelper.TABLE_MOVIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                int categoryId = cursor.getInt(1);
                String name = cursor.getString(2);
                String director = cursor.getString(3);
                String description = cursor.getString(4);
                String trailer = cursor.getString(5);
                int price = cursor.getInt(6);
                String image1 = cursor.getString(7);
                String image2 = cursor.getString(8);
                String image3 = cursor.getString(9);
                int ts1 = cursor.getInt(10);
                int ts2 = cursor.getInt(11);
                int ts3 = cursor.getInt(12);
                int ts4 = cursor.getInt(13);
                int ts5 = cursor.getInt(14);
                Movie movie = new Movie(id, categoryId, name, price, director, description, trailer, image1, image2, image3, ts1, ts2, ts3, ts4, ts5);
                movies.add(movie);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return movies;
    }

    public boolean addMovie(int categoryId, String movieName, int moviePrice, String movieDirector, String description, String trailer,
                            String image1, String image2, String image3, Boolean ts1, Boolean ts2, Boolean ts3,
                            Boolean ts4, Boolean ts5) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MOVIE_CATEGORY_ID, categoryId);
        values.put(DatabaseHelper.COLUMN_MOVIE_NAME, movieName);
        values.put(DatabaseHelper.COLUMN_MOVIE_DIRECTOR, movieDirector);
        values.put(DatabaseHelper.COLUMN_MOVIE_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_MOVIE_TRAILER, trailer);
        values.put(DatabaseHelper.COLUMN_MOVIE_PRICE, moviePrice);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_1, image1);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_2, image2);
        values.put(DatabaseHelper.COLUMN_MOVIE_IMAGE_3, image3);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION1, ts1);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION2, ts2);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION3, ts3);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION4, ts4);
        values.put(DatabaseHelper.COLUMN_TIME_SLOT_OPTION5, ts5);
        long result = db.insert(DatabaseHelper.TABLE_MOVIES, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Users getInforAccount(String musername) {
        Users user = null;
        Cursor cursor = db.query(DBhelper.TABLE_USERS, new String[]{DBhelper.COLUMN_USER_ID, DBhelper.COLUMN_USER_USERNAME,
                        DBhelper.COLUMN_USER_PASSWORD, DBhelper.COLUMN_USER_NAME, DBhelper.COLUMN_USER_EMAIL, DBhelper.COLUMN_USER_PHONE},
                DBhelper.COLUMN_USER_USERNAME + "=?", new String[]{musername}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            user = new Users(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5)
            );
        }
        cursor.close();
        return user;
    }

    public boolean updateUser(String username, String name, String email, String phone) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_NAME, name);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_USER_PHONE, phone);

        int rowsAffected = db.update(DatabaseHelper.TABLE_USERS, values,
                DatabaseHelper.COLUMN_USER_USERNAME + " = ?", new String[]{username});

        return rowsAffected > 0;
    }

    public Movie getMovieById(int movieId) {

        String[] columns = {DBhelper.COLUMN_MOVIE_ID, DBhelper.COLUMN_MOVIE_CATEGORY_ID, DBhelper.COLUMN_MOVIE_NAME,
                DBhelper.COLUMN_MOVIE_DIRECTOR, DBhelper.COLUMN_MOVIE_DESCRIPTION, DBhelper.COLUMN_MOVIE_TRAILER,
                DBhelper.COLUMN_MOVIE_PRICE, DBhelper.COLUMN_MOVIE_IMAGE_1, DBhelper.COLUMN_MOVIE_IMAGE_2,
                DBhelper.COLUMN_MOVIE_IMAGE_3, DBhelper.COLUMN_TIME_SLOT_OPTION1, DBhelper.COLUMN_TIME_SLOT_OPTION2,
                DBhelper.COLUMN_TIME_SLOT_OPTION3, DBhelper.COLUMN_TIME_SLOT_OPTION4, DBhelper.COLUMN_TIME_SLOT_OPTION5};
        String selection = DBhelper.COLUMN_MOVIE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(movieId)};

        Cursor cursor = db.query(DBhelper.TABLE_MOVIES, columns, selection, selectionArgs, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            for (String columnName : cursor.getColumnNames()) {
                Log.d("Column Name", columnName);
            }

            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            String name = cursor.getString(2);
            String author = cursor.getString(3);
            String description = cursor.getString(4);
            String trailer = cursor.getString(5);
            int price = cursor.getInt(6);
            String image1 = cursor.getString(7);
            String image2 = cursor.getString(8);
            String image3 = cursor.getString(9);
            int ts1 = cursor.getInt(10);
            int ts2 = cursor.getInt(11);
            int ts3 = cursor.getInt(12);
            int ts4 = cursor.getInt(13);
            int ts5 = cursor.getInt(14);
            Movie movie = new Movie(id, categoryId, name, price, author, description, trailer, image1, image2, image3, ts1, ts2, ts3, ts4, ts5);

            cursor.close();
            return movie;
        }
        return null;
    }

    private boolean getStringAsBoolean(String intValue) {
        return intValue.equals("true");
    }

    public int getUserIdByUsername(String username) {
        int userId = -1;
        String[] columns = {DatabaseHelper.COLUMN_USER_ID};
        String selection = DatabaseHelper.COLUMN_USER_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        return userId;
    }

    public boolean addToCart(String username, int movieId, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CART_USER_ID, getUserIdByUsername(username));
        values.put(DatabaseHelper.COLUMN_CART_MOVIE_ID, movieId);
        values.put(DatabaseHelper.COLUMN_CART_QUANTITY, quantity);

        long result = db.insert(DatabaseHelper.TABLE_CART, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<HistoryOrder> getAllOrders(int userid) {
        List<HistoryOrder> ordersList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBhelper.TABLE_HISTORY_ORDER + " WHERE " +
                DBhelper.COLUMN_ORDER_USER_ID + "=?", new String[]{String.valueOf(userid)});

        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(0);
                int userId = cursor.getInt(1);
                int totalPrice = cursor.getInt(2);
                String orderDate = cursor.getString(3);
                String receiverName = cursor.getString(4);
                String receiverPhone = cursor.getString(5);
                String paymentMethod = cursor.getString(6);
                HistoryOrder order = new HistoryOrder(orderId, userId, totalPrice, orderDate, receiverName, receiverPhone, paymentMethod);
                ordersList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return ordersList;
    }

    public long insertHistoryOrder(int userId, int totalPrice, String orderDate, String username,
                                   String receiverPhone, String paymentMethod) {
        ContentValues values = new ContentValues();
        values.put(DBhelper.COLUMN_ORDER_USER_ID, userId);
        values.put(DBhelper.COLUMN_ORDER_TOTAL_PRICE, totalPrice);
        values.put(DBhelper.COLUMN_ORDER_DATE, orderDate);
        values.put(DBhelper.COLUMN_HISTORY_RECEIVER_NAME, username);
        values.put(DBhelper.COLUMN_HISTORY_RECEIVER_PHONE, receiverPhone);
        values.put(DBhelper.COLUMN_HISTORY_PAYMENT_METHOD, paymentMethod);

        long id = db.insert(DBhelper.TABLE_HISTORY_ORDER, null, values);
        return id;
    }

    public void insertOrderDetail(long orderId, int movieId, int quantity) {
        ContentValues values = new ContentValues();
        values.put(DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID, orderId);
        values.put(DBhelper.COLUMN_ORDER_DETAIL_MOVIE_ID, movieId);
        values.put(DBhelper.COLUMN_ORDER_DETAIL_QUANTITY, quantity);
        db.insert(DBhelper.TABLE_ORDER_DETAIL, null, values);
    }

    public HistoryOrder getOrderById(int id) {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBhelper.TABLE_HISTORY_ORDER
                + " WHERE " + DBhelper.COLUMN_ORDER_ID + "=?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int Orderid = cursor.getInt(0);
            int UserId = cursor.getInt(1);
            int TotalPrice = cursor.getInt(2);
            String OrderDate = cursor.getString(3);
            String ReceiverName = cursor.getString(4);
            String ReceiverPhone = cursor.getString(5);
            String PaymentMethod = cursor.getString(6);
            HistoryOrder order = new HistoryOrder(Orderid, UserId, TotalPrice, OrderDate,
                    ReceiverName, ReceiverPhone, PaymentMethod);
            cursor.close();
            return order;
        }
        return null;
    }

    public List<OrderDetail> getOrderDetailByOrderId(int orderId) {
        List<OrderDetail> orderDetailList = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + DBhelper.TABLE_ORDER_DETAIL
                + " WHERE " + DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID + " = " + orderId;

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                OrderDetail orderDetail = new OrderDetail(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getInt(3)
                );
                orderDetailList.add(orderDetail);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return orderDetailList;
    }

    public boolean deleteOrder(int orderId) {
        db.delete(DBhelper.TABLE_ORDER_DETAIL, DBhelper.COLUMN_ORDER_DETAIL_ORDER_ID
                + " = ?", new String[]{String.valueOf(orderId)});

        int result = db.delete(DBhelper.TABLE_HISTORY_ORDER, DBhelper.COLUMN_ORDER_ID
                + " = ?", new String[]{String.valueOf(orderId)});
        if (result == -1) {
            return false;
        }
        return true;
    }

    public ArrayList<Users> getAllUser() {
        ArrayList<Users> usersList = new ArrayList<>();

        Cursor cursor = db.query(DatabaseHelper.TABLE_USERS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.setUser_id(cursor.getInt(0));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setName(cursor.getString(3));
                user.setEmail(cursor.getString(4));
                user.setPhone(cursor.getString(5));
                usersList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return usersList;
    }

    public boolean addCategory(String categoryName) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, categoryName);

        long result = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteCategory(long categoryId) {
        int result = db.delete(DBhelper.TABLE_CATEGORIES, DBhelper.COLUMN_CATEGORY_ID + "=?", new String[]{String.valueOf(categoryId)});
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<Category> getAllCategories() {
        ArrayList<Category> categories = new ArrayList<Category>();

        Cursor cursor = db.query(DBhelper.TABLE_CATEGORIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int categoryId = cursor.getInt(0);
                String categoryName = cursor.getString(1);

                Category category = new Category(categoryId, categoryName);
                categories.add(category);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public ArrayList<String> getAllCategoryNames() {
        ArrayList<String> categories = new ArrayList<>();

        Cursor cursor = db.query(DBhelper.TABLE_CATEGORIES, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(1);
                categories.add(categoryName);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }
}

