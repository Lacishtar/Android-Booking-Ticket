package com.tdtu.starrail.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Seat;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private Button btnConfirm;
    private Mydatabase mydatabase;
    private String musername, selectedDate, selectedTime;
    private int mmovieid, mseatprice;

    private List<Seat> selectedSeats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actitvity_seat_select);

        Bundle p = getIntent().getExtras();
        if (p != null) {
            musername = p.getString("username");
            mmovieid = p.getInt("movieid");
            mseatprice = p.getInt(("mseatprice"));
            selectedDate = p.getString("selectedDate");
            selectedTime = p.getString("selectedTime");
        }
        mydatabase = new Mydatabase(this);

        gridLayout = findViewById(R.id.gridLayout);
        btnConfirm = findViewById(R.id.btnConfirm);

        createSeatButtons(selectedDate, selectedTime);
        int number_of_selectedSeat = calculateSelectedSeatsCount();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeSelectedSeats(selectedDate, selectedTime);
                int no_seat = calculateTotalSelectedSeat() - number_of_selectedSeat;
                int totalPrice = mseatprice * no_seat;
                displayPaymentOptions(mmovieid, totalPrice, no_seat);
            }
        });
    }

    private void displayPaymentOptions(int mmovieid, int totalPrice, int no_seat) {
        Intent intent = new Intent(SeatSelectionActivity.this, InforOrder.class);
        Bundle b = new Bundle();
        b.putInt("movieid", mmovieid);
        b.putString("username", musername);
        b.putInt("totalPrice", totalPrice);
        b.putInt("number_of_seat", no_seat);
        b.putString("selectedDate", selectedDate);
        b.putString("selectedTime", selectedTime);
        b.putParcelableArrayList("selectedSeats", new ArrayList<>(selectedSeats));
        intent.putExtras(b);
        startActivity(intent);
    }

    private int calculateTotalSelectedSeat() {
        int seat = 0;

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View seatView = gridLayout.getChildAt(i);

            if (seatView instanceof Button) {
                Button seatButton = (Button) seatView;
                if (seatButton.isSelected()) {
                    seat += 1;
                }
            }
        }
        return seat;
    }

    private void storeSelectedSeats(String selectedDate, String selectedTime) {
        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View seatView = gridLayout.getChildAt(i);

            if (seatView instanceof Button) {
                Button seatButton = (Button) seatView;
                Seat seat = new Seat();
                seat.setSeatNumber(seatButton.getText().toString());
                seat.setSelected(seatButton.isSelected());

                if (seatButton.isSelected()) {
                    selectedSeats.add(seat);
                }
            }
        }
    }

    private void createSeatButtons(String selectedDate, String selectedTime) {
        List<Seat> seatList = mydatabase.getSeatsData(mmovieid, selectedDate, selectedTime);

        for (int i = 1; i <= 24; i++) {
            Button seatButton = new Button(this);
            seatButton.setLayoutParams(new GridLayout.LayoutParams());
            seatButton.setText("Seat " + i);

            seatButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));

            boolean isBooked = isSeatBooked("Seat " + i, mmovieid, selectedDate, selectedTime);

            if (isBooked) {
                seatButton.setBackgroundColor(0xFF236A64);
                seatButton.setSelected(true);
            } else {
                for (Seat seat : seatList) {
                    if (("Seat " + i).equals(seat.getSeatNumber())) {
                        seatButton.setSelected(seat.isSelected());

                        if (seat.isSelected()) {
                            seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                        } else {
                            seatButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        }
                        break;
                    }
                }
            }

            seatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleSeatSelection((Button) view, mmovieid, selectedDate, selectedTime);
                }
            });

            gridLayout.addView(seatButton);
        }

        updateConfirmButtonVisibility();
    }

    private int calculateSelectedSeatsCount() {
        int selectedSeatsCount = 0;

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View seatView = gridLayout.getChildAt(i);

            if (seatView instanceof Button) {
                Button seatButton = (Button) seatView;
                if (seatButton.isSelected()) {
                    selectedSeatsCount++;
                }
            }
        }

        return selectedSeatsCount;
    }

    private boolean isSeatBooked(String seatNumber, int movieId, String selectedDate, String selectedTime) {
        Mydatabase mydatabase = new Mydatabase(SeatSelectionActivity.this);
        return mydatabase.isSeatBooked(seatNumber, movieId, selectedDate, selectedTime);
    }

    private void toggleSeatSelection(Button seatButton, int movieId, String selectedDate, String selectedTime) {
        boolean isSelected = !seatButton.isSelected();

        if (isSeatBooked(seatButton.getText().toString(), movieId, selectedDate, selectedTime)) {
            Toast.makeText(SeatSelectionActivity.this, "Seat is already booked!", Toast.LENGTH_SHORT).show();
            return;
        }

        seatButton.setSelected(isSelected);
        if (isSelected) {
            seatButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
        } else {
            seatButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

        updateConfirmButtonVisibility();
    }

    private void updateConfirmButtonVisibility() {
        boolean atLeastOneSeatSelected = false;

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            View seatView = gridLayout.getChildAt(i);

            if (seatView instanceof Button) {
                Button seatButton = (Button) seatView;

                // Check if the seat is both selected and not booked
                if (seatButton.isSelected() && !isSeatBooked(seatButton.getText().toString(), mmovieid, selectedDate, selectedTime)) {
                    atLeastOneSeatSelected = true;
                    break;
                }
            }
        }

        btnConfirm.setVisibility(atLeastOneSeatSelected ? View.VISIBLE : View.GONE);
    }
}
