package com.tdtu.starrail.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Seat;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InforOrder extends AppCompatActivity {

    Toolbar toolbar;
    private EditText edtHoTen, edtSDT;
    private RadioGroup rgPaymentMethod;
    Mydatabase mydb;
    String musername, selectedDate, selectedTime;
    int mtotalprice, mno_seat, mmovieid;

    String publish_key = "pk_test_51OPyfNBBcnNFb9rWTd1ony0fbxxYojms8osMaYFXG0rByTzcDNdtUpyHIGwo8RuWbnpA0tbf7tFAzTKqkvkukMF800YwnyUfO2";
    String secret_key = "sk_test_51OPyfNBBcnNFb9rW7kHYBrEYmLrHseJUoatwVUMzn0Qvo6fhIRMXRW9lwRGxhn6pOPOVA4sWCD3bVBVSwYaWB7zm00FOfh5i8z";
    PaymentSheet paymentSheet;
    String customerID;
    String EphericalKey;
    String ClientSecret;
    ArrayList<Seat> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor_order);
        Anhxa();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Bundle p = getIntent().getExtras();
        if (p != null) {
            mmovieid = p.getInt("movieid");
            musername = p.getString("username");
            mtotalprice = p.getInt("totalPrice");
            mno_seat = p.getInt("number_of_seat");
            selectedDate = p.getString("selectedDate");
            selectedTime = p.getString("selectedTime");
            selectedSeats = p.getParcelableArrayList("selectedSeats");
        }

        PaymentConfiguration.init(this, publish_key);
        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number_of_seat", mno_seat);
        editor.putString("selectedDate", selectedDate);
        editor.putString("selectedTime", selectedTime);
        editor.apply();
        Intent intent = new Intent(InforOrder.this, OrderDetailActivity.class);
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            for (Seat seat : selectedSeats) {
                mydb.addSeat(seat, mmovieid, selectedDate, selectedTime);
            }
            Intent intent = new Intent(InforOrder.this, HomeActivity.class);
            Bundle b = new Bundle();
            b.putString("username", musername);
            intent.putExtras(b);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void getEphericalKey(String customerID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            getCilentSecret(customerID, EphericalKey);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StripeError", error.toString());
                // Handle error (e.g., show a message to the user)
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secret_key);
                header.put("Stripe-Version", "2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params; // Return the params instead of calling super.getParams()
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(InforOrder.this);
        requestQueue.add(stringRequest);
    }

    private void getCilentSecret(String customerID, String ephericalKey) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            ClientSecret = object.getString("client_secret");
                            PaymentFlow();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secret_key);
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", String.valueOf(mtotalprice));
                params.put("currency", "vnd");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(InforOrder.this);
        requestQueue.add(stringRequest);
    }

    private void PaymentFlow() {
        if (customerID != null && EphericalKey != null && ClientSecret != null) {
            paymentSheet.presentWithPaymentIntent(
                    ClientSecret, new PaymentSheet.Configuration("STAR RAIL THEATRE",
                            new PaymentSheet.CustomerConfiguration(
                                    customerID,
                                    EphericalKey
                            ))
            );
        } else {
            Toast.makeText(this, "Sorry something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        edtHoTen = findViewById(R.id.edt_hoten);
        edtSDT = findViewById(R.id.edt_sdt);
        rgPaymentMethod = findViewById(R.id.rg_ppthanhtoan);
        mydb = new Mydatabase(this);
    }

    public void btn_xacnhan(View view) {
        String hoTen = edtHoTen.getText().toString();
        String sdt = edtSDT.getText().toString();
        int paymentMethodId = rgPaymentMethod.getCheckedRadioButtonId();
        String paymentMethod = "";

        if (paymentMethodId == R.id.rb_cod) {
            paymentMethod = "COD";
        } else {
            paymentMethod = "Visa";
        }

        if (hoTen.isEmpty() && sdt.isEmpty() && paymentMethod.equals("")) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else {
            int userId = mydb.getUserIdByUsername(musername);
            int totalPrice = mtotalprice;
            String currentDate = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault()).format(new Date());
            long orderId = mydb.insertHistoryOrder(userId, totalPrice, currentDate, hoTen, sdt, paymentMethod);
            mydb.insertOrderDetail(orderId, mmovieid, mno_seat);

            if (paymentMethod.equals("Visa")) {
                startStripe();
            } else {
                for (Seat seat : selectedSeats) {
                    mydb.addSeat(seat, mmovieid, selectedDate, selectedTime);
                }
                Intent intent = new Intent(InforOrder.this, HomeActivity.class);
                Bundle b = new Bundle();
                b.putString("username", musername);
                intent.putExtras(b);
                Toast.makeText(this, "Order successful", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }
    }

    private void startStripe() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            getEphericalKey(customerID);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("StripeError", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + secret_key);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(InforOrder.this);
        requestQueue.add(stringRequest);
    }
}
