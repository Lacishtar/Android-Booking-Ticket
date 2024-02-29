package com.tdtu.starrail.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Movie;
import com.tdtu.starrail.classes.OrderDetail;


import java.util.List;

public class AdapterOrderDetail extends RecyclerView.Adapter<AdapterOrderDetail.ViewHolder> {
    private Context context;
    private List<OrderDetail> orderDetailList;
    private Mydatabase mydb;
    private String date;
    private String time;
    private int mno_seat;

    public AdapterOrderDetail(Context context, List<OrderDetail> orderDetailList, Mydatabase mydb, String selectedDate, String selectedTime, Integer mno_seat) {
        this.context = context;
        this.orderDetailList = orderDetailList;
        this.mydb = mydb;
        this.date = selectedDate;
        this.time = selectedTime;
        this.mno_seat = mno_seat;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item_rcvdetailorder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderDetail orderDetail = orderDetailList.get(position);
        Movie book = mydb.getMovieById(orderDetail.getMovieId());

        holder.tvOrderDetailName.setText(book.getName());
        holder.tvOrderDetailQuantity.setText("x" + orderDetail.getQuantity());
        holder.tvOrderDetailDate.setText(date);
        holder.tvOrderDetailTime.setText(time);

        generateQRCode(orderDetail, holder);
    }

    private void generateQRCode(OrderDetail orderDetail, ViewHolder holder) {
        AsyncTask.execute(() -> {
            try {
                String data = "MovieID: " + orderDetail.getMovieId() +
                        "\nMovieName: " + holder.tvOrderDetailName.getText() +
                        "\nTicket Quantity: " + orderDetail.getQuantity() +
                        "\nBookDate: " + holder.tvOrderDetailDate.getText() +
                        "\nBookTime: " + holder.tvOrderDetailTime.getText();

                // Generate QR code bitmap
                Bitmap qrCodeBitmap = generateQRCodeBitmap(data, 500, 500);

                // Update UI on the main thread
                holder.imageView.post(() -> {
                    // Display the QR code in the ImageView
                    holder.imageView.setImageBitmap(qrCodeBitmap);
                });

            } catch (WriterException e) {
                Log.e("AdapterOrderDetail", "Error generating QR code", e);
            }
        });
    }

    private Bitmap generateQRCodeBitmap(String data, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }

        return bitmap;
    }

    @Override
    public int getItemCount() {
        return orderDetailList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvOrderDetailName;
        TextView tvOrderDetailQuantity;
        TextView tvOrderDetailDate, tvOrderDetailTime;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_itemdetailorder);
            tvOrderDetailName = itemView.findViewById(R.id.tv_namemovie);
            tvOrderDetailQuantity = itemView.findViewById(R.id.tv_quantity);
            tvOrderDetailDate = itemView.findViewById(R.id.tv_datemovie);
            tvOrderDetailTime = itemView.findViewById(R.id.tv_timemovie);
        }
    }
}
