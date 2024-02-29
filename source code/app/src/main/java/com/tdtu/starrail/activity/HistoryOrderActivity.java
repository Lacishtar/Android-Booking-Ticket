package com.tdtu.starrail.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.adapter.AdapterHistoryOrder;
import com.tdtu.starrail.classes.HistoryOrder;

import java.util.List;

public class HistoryOrderActivity extends AppCompatActivity {

    private ListView lvmoviehistoryorder;
    private Mydatabase mydb;
    private List<HistoryOrder> orderList;
    private AdapterHistoryOrder adapter;
    String musername;
    int userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_order);
        Anhxa();

        Bundle b = getIntent().getExtras();
        musername = b.getString("username");
        Toolbar toolbar = findViewById(R.id.toolbarhistoryorder);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userID = mydb.getUserIdByUsername(musername);
        orderList = mydb.getAllOrders(userID);
        adapter = new AdapterHistoryOrder(this, R.layout.layout_item_historyorder, orderList);
        lvmoviehistoryorder.setAdapter(adapter);
    }

    private void Anhxa() {
        lvmoviehistoryorder = findViewById(R.id.lvhistoryorder);
        mydb = new Mydatabase(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.clear();
        orderList = mydb.getAllOrders(userID);
        adapter.notifyDataSetChanged();
        adapter.addAll(orderList);
    }
}
