package com.tdtu.starrail.sqlHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "moviestore.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    // Table seats
    public static final String TABLE_SEATS = "seats";
    public static final String COLUMN_SEAT_ID = "seat_id";
    public static final String COLUMN_SEAT_MOVIE_ID = "seat_movie_id";
    public static final String COLUMN_SEAT_SEAT_NUMBER = "seat_number";
    public static final String COLUMN_SEAT_ISSLECTED = "seat_isSelected";
    public static final String COLUMN_SEAT_TIME = "seat_time";
    public static final String COLUMN_SEAT_DATE = "seat_date";

    // Table users
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_USERNAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PHONE = "phone";

    // Table categories
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CATEGORY_ID = "categorie_id";
    public static final String COLUMN_CATEGORY_NAME = "name";

    // Table movies
    public static final String TABLE_MOVIES = "movies";
    public static final String COLUMN_MOVIE_ID = "movie_id";
    public static final String COLUMN_MOVIE_CATEGORY_ID = "movie_category_id";
    public static final String COLUMN_MOVIE_NAME = "movie_name";
    public static final String COLUMN_MOVIE_DIRECTOR = "movie_director";
    public static final String COLUMN_MOVIE_DESCRIPTION = "movie_description";
    public static final String COLUMN_MOVIE_TRAILER = "movie_trailer";
    public static final String COLUMN_MOVIE_PRICE = "movie_price";
    public static final String COLUMN_MOVIE_IMAGE_1 = "imagemovie_1";
    public static final String COLUMN_MOVIE_IMAGE_2 = "imagemovie_2";
    public static final String COLUMN_MOVIE_IMAGE_3 = "imagemovie_3";
    public static final String COLUMN_TIME_SLOT_OPTION1 = "timeslot1";
    public static final String COLUMN_TIME_SLOT_OPTION2 = "timeslot2";
    public static final String COLUMN_TIME_SLOT_OPTION3 = "timeslot3";
    public static final String COLUMN_TIME_SLOT_OPTION4 = "timeslot4";
    public static final String COLUMN_TIME_SLOT_OPTION5 = "timeslot5";

    // Table Cart
    public static final String TABLE_CART = "cart";
    public static final String COLUMN_CART_ID = "cart_id";
    public static final String COLUMN_CART_USER_ID = "cart_user_id";
    public static final String COLUMN_CART_MOVIE_ID = "cart_movie_id";
    public static final String COLUMN_CART_QUANTITY = "cart_quantity";

    // Table historyorder
    public static final String TABLE_HISTORY_ORDER = "historyorder";
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_USER_ID = "user_id";
    public static final String COLUMN_ORDER_TOTAL_PRICE = "total_price";
    public static final String COLUMN_ORDER_DATE = "order_date";
    public static final String COLUMN_HISTORY_RECEIVER_NAME = "receiver_name";
    public static final String COLUMN_HISTORY_RECEIVER_PHONE = "receiver_phone";
    public static final String COLUMN_HISTORY_PAYMENT_METHOD = "payment_method";

    // Table order_detail
    public static final String TABLE_ORDER_DETAIL = "order_detail";
    public static final String COLUMN_ORDER_DETAIL_ID = "order_detail_id";
    public static final String COLUMN_ORDER_DETAIL_ORDER_ID = "order_id";
    public static final String COLUMN_ORDER_DETAIL_MOVIE_ID = "movie_id";
    public static final String COLUMN_ORDER_DETAIL_QUANTITY = "quantity";

    // Create table seats
    private static final String CREATE_TABLE_SEATS = "CREATE TABLE " + TABLE_SEATS + " ("
            + COLUMN_SEAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_SEAT_SEAT_NUMBER + " TEXT, "
            + COLUMN_SEAT_ISSLECTED + " INTEGER, "
            + COLUMN_SEAT_MOVIE_ID + " INTEGER, "
            + COLUMN_SEAT_TIME + " TEXT, "
            + COLUMN_SEAT_DATE + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_SEAT_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + ") "
            + ")";

    // Create table users
    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USER_USERNAME + " TEXT ,"
            + COLUMN_USER_PASSWORD + " TEXT ,"
            + COLUMN_USER_NAME + " TEXT ,"
            + COLUMN_USER_EMAIL + " TEXT ,"
            + COLUMN_USER_PHONE + " TEXT "
            + ")";

    // Create table categories
    private static final String CREATE_TABLE_CATEGORIES = "CREATE TABLE " + TABLE_CATEGORIES + "("
            + COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CATEGORY_NAME + " TEXT "
            + ")";

    // Create table movies
    private static final String CREATE_TABLE_MOVIES = "CREATE TABLE " + TABLE_MOVIES + "("
            + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MOVIE_CATEGORY_ID + " INTEGER ,"
            + COLUMN_MOVIE_NAME + " TEXT ,"
            + COLUMN_MOVIE_DIRECTOR + " TEXT ,"
            + COLUMN_MOVIE_DESCRIPTION + " TEXT ,"
            + COLUMN_MOVIE_TRAILER + " TEXT ,"
            + COLUMN_MOVIE_PRICE + " INTEGER ,"
            + COLUMN_MOVIE_IMAGE_1 + " TEXT ,"
            + COLUMN_MOVIE_IMAGE_2 + " TEXT ,"
            + COLUMN_MOVIE_IMAGE_3 + " TEXT ,"
            + COLUMN_TIME_SLOT_OPTION1 + " TEXT, "
            + COLUMN_TIME_SLOT_OPTION2 + " TEXT, "
            + COLUMN_TIME_SLOT_OPTION3 + " TEXT, "
            + COLUMN_TIME_SLOT_OPTION4 + " TEXT, "
            + COLUMN_TIME_SLOT_OPTION5 + " TEXT, "
            + "FOREIGN KEY(" + COLUMN_MOVIE_CATEGORY_ID + ") REFERENCES " + TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + ") "
            + ")";

    // Create table Cart
    private static final String CREATE_TABLE_CART = "CREATE TABLE " + TABLE_CART + "("
            + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CART_USER_ID + " INTEGER , "
            + COLUMN_CART_MOVIE_ID + " INTEGER , "
            + COLUMN_CART_QUANTITY + " INTEGER , "
            + "FOREIGN KEY(" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), "
            + "FOREIGN KEY(" + COLUMN_CART_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "));";

    // Create table historyorder
    private static final String CREATE_TABLE_HISTORY_ORDER = "CREATE TABLE " + TABLE_HISTORY_ORDER + "("
            + COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_USER_ID + " INTEGER,"
            + COLUMN_ORDER_TOTAL_PRICE + " INTEGER,"
            + COLUMN_ORDER_DATE + " TEXT,"
            + COLUMN_HISTORY_RECEIVER_NAME + " TEXT ,"
            + COLUMN_HISTORY_RECEIVER_PHONE + " TEXT ,"
            + COLUMN_HISTORY_PAYMENT_METHOD + " TEXT ,"
            + "FOREIGN KEY(" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "));";

    // Create table order_detail
    private static final String CREATE_TABLE_ORDER_DETAIL = "CREATE TABLE " + TABLE_ORDER_DETAIL + "("
            + COLUMN_ORDER_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ORDER_DETAIL_ORDER_ID + " INTEGER,"
            + COLUMN_ORDER_DETAIL_MOVIE_ID + " INTEGER,"
            + COLUMN_ORDER_DETAIL_QUANTITY + " INTEGER,"
            + "FOREIGN KEY(" + COLUMN_ORDER_DETAIL_ORDER_ID + ") REFERENCES " + TABLE_HISTORY_ORDER + "(" + COLUMN_ORDER_ID + "),"
            + "FOREIGN KEY(" + COLUMN_ORDER_DETAIL_MOVIE_ID + ") REFERENCES " + TABLE_MOVIES + "(" + COLUMN_MOVIE_ID + "));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor rawQuery(String query, String[] selectionArgs) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(query, selectionArgs);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_CATEGORIES);
        db.execSQL(CREATE_TABLE_MOVIES);
        db.execSQL(CREATE_TABLE_SEATS);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_HISTORY_ORDER);
        db.execSQL(CREATE_TABLE_ORDER_DETAIL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if (i < i1) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEATS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY_ORDER);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_DETAIL);
            onCreate(db);
        }
    }
}
