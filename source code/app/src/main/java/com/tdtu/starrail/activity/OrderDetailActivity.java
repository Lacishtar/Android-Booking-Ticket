package com.tdtu.starrail.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterOrderDetail;
import com.tdtu.starrail.classes.HistoryOrder;
import com.tdtu.starrail.classes.OrderDetail;


import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    TextView tvMaDonHang, tvUsername, tvDate, tvReceiverPhone, tvPaymentMethod, tvTotalPrice;
    RecyclerView rcvOrderDetail;
    Button btnDeleteOrder;
    Mydatabase mydb;
    int orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        Anhxa();

        Bundle b = getIntent().getExtras();
        orderId = b.getInt("orderID");

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        int numberOfSeats = sharedPreferences.getInt("number_of_seat", 0); // Provide a default value
        String selectedDate = sharedPreferences.getString("selectedDate", "");
        String selectedTime = sharedPreferences.getString("selectedTime", "");

        HistoryOrder order = mydb.getOrderById(orderId);

        tvMaDonHang.setText(String.valueOf(orderId));
        tvUsername.setText(order.getReceiverName());
        tvDate.setText(order.getOrderDate());
        tvReceiverPhone.setText(order.getReceiverPhone());

        tvPaymentMethod.setText(order.getPaymentMethod());
        tvTotalPrice.setText(String.format("%,d", order.getTotalPrice())+ " VNĐ");

        List<OrderDetail> orderDetailList = mydb.getOrderDetailByOrderId(orderId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvOrderDetail.setLayoutManager(layoutManager);
        AdapterOrderDetail adapter = new AdapterOrderDetail(this,orderDetailList, mydb, selectedDate, selectedTime, numberOfSeats);
        rcvOrderDetail.setAdapter(adapter);

        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setTitle("Xác nhận hủy đơn hàng");
                builder.setMessage("Bạn có chắc chắn muốn hủy đơn hàng?");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mydb.deleteOrder(orderId)) {
                            Toast.makeText(getApplicationContext(), "Hủy đơn hàng thành công", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            onBackPressed();
                        } else {
                            Toast.makeText(getApplicationContext(), "Hủy đơn hàng thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void Anhxa() {
        tvMaDonHang = findViewById(R.id.tv_madonhang);
        tvUsername = findViewById(R.id.tv_username);
        tvDate = findViewById(R.id.tv_date);
        tvReceiverPhone = findViewById(R.id.tv_receiverphone);
        tvPaymentMethod = findViewById(R.id.tv_paymentmethod);
        tvTotalPrice = findViewById(R.id.tv_totalprice);
        rcvOrderDetail = findViewById(R.id.rcvorderdetail);
        btnDeleteOrder = findViewById(R.id.btn_deleteorder);
        mydb = new Mydatabase(this);
    }

    public void back(View view) {
        onBackPressed();
    }
}
