package com.example.lpgdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;


public class OrderViewHolder extends RecyclerView.Adapter<OrderViewHolder.MyViewHolder> {

    private ArrayList<Order> dataSet;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

       TextView txtOrderId;
         TextView txtOrderStatus;
      TextView txtOrderQuan;
      TextView txtOrderSize;
       TextView txtOrderPrice;
        TextView txtOrderDate;
        TextView txtBookDate;
     TextView txtOrderTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtOrderId = itemView.findViewById(R.id.order_id);
            txtOrderStatus = itemView.findViewById(R.id.order_status);
            txtOrderSize = itemView.findViewById(R.id.order_size);
            txtOrderQuan = itemView.findViewById(R.id.order_quan);
            txtOrderPrice = itemView.findViewById(R.id.order_price);
            txtOrderDate = itemView.findViewById(R.id.order_date);
            txtBookDate = itemView.findViewById(R.id.book_date);
            txtOrderTime = itemView.findViewById(R.id.order_time);
        }
    }

    public OrderViewHolder(ArrayList<Order> data) {
        this.dataSet = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_layout, parent, false);

        //  view.setOnClickListener(MainActivity.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        TextView txtOrderId = holder.txtOrderId;
        TextView txtOrderStatus = holder.txtOrderStatus;
        TextView txtOrderSize = holder.txtOrderSize;
        TextView txtOrderPrice = holder.txtOrderPrice;
        TextView txtOrderDate = holder.txtOrderDate;
        TextView txtOrderTime = holder.txtOrderTime;
        TextView txtOrderQuan = holder.txtOrderQuan;
        TextView txtBookDate = holder.txtBookDate;


        txtOrderId.setText(dataSet.get(listPosition).getOid());
        txtOrderStatus.setText(dataSet.get(listPosition).getStatus());
        txtOrderSize.setText(dataSet.get(listPosition).getCylinderSize());
        txtOrderPrice.setText(dataSet.get(listPosition).getAmountPayable());
        txtOrderDate.setText(dataSet.get(listPosition).getDate());
        txtOrderTime.setText(dataSet.get(listPosition).getTime());
        txtOrderQuan.setText(dataSet.get(listPosition).getQuan());
        txtBookDate.setText(dataSet.get(listPosition).getBdate());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}

