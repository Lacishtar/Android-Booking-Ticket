package com.tdtu.starrail.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.tdtu.starrail.R;
import com.tdtu.starrail.sqlHelper.Mydatabase;
import com.tdtu.starrail.classes.Users;


import java.util.List;

public class AdapterUsersList extends BaseAdapter {
    private Context mContext;
    private List<Users> mUserList;

    public AdapterUsersList(Context context, List<Users> userList) {
        mContext = context;
        mUserList = userList;
    }

    @Override
    public int getCount() {
        return mUserList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUserList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.layout_listusers, null);
            holder = new ViewHolder();
            holder.txtUsername = (TextView) view.findViewById(R.id.userNameTextView);
            holder.btnDelete = (ImageButton) view.findViewById(R.id.btn_xoauser);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Users user = mUserList.get(position);
        holder.txtUsername.setText(user.getUsername());
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog(position);
            }
        });
        holder.txtUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailDialog(position);
            }
        });
        return view;
    }
    private void showDetailDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Thông tin người dùng");
        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_detailuser, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        TextView tv_hoten = dialogView.findViewById(R.id.tv_detailhoten);
        TextView tv_sdt = dialogView.findViewById(R.id.tv_detailsdt);
        TextView tv_gmail = dialogView.findViewById(R.id.tv_detailgmail);

        final Users user = mUserList.get(position);

        tv_hoten.setText(user.getName());
        tv_sdt.setText(user.getPhone());
        tv_gmail.setText(user.getEmail());

        dialog.show();
    }
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Xóa người dùng");
        builder.setMessage("Có chắc chắn muốn xóa người dùng này?");
        final Users user = mUserList.get(position);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Mydatabase mydb = new Mydatabase(mContext);
                mUserList.remove(user);
                mydb.deleteuser(user.getUser_id());
                notifyDataSetChanged();
                Toast.makeText(mContext, "Xóa người dùng thành công!", Toast.LENGTH_SHORT).show();
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
    static class ViewHolder {
        TextView txtUsername;
        ImageButton btnDelete;
    }
}
