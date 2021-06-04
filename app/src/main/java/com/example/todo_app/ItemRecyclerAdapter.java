package com.example.todo_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemRecyclerAdapter extends RecyclerView.Adapter<ItemRecyclerAdapter.ViewHolder> {

    List<Item> itemList;
    Context context;

    public ItemRecyclerAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.titleName.setText(item.getTitle());
        holder.dateTime.setText(item.getDate());

        holder.parentLayout.setOnClickListener(v -> {

            // for changing background color
            holder.parentLayout.setBackgroundColor(Color.GRAY);
            new Thread(() -> {
                SystemClock.sleep(100);
                holder.parentLayout.setBackgroundColor(Color.WHITE);
            }).start();

            // deny when item is lock
            if (MainActivity.isRecyclerItemLock) {
                Toast.makeText(context, "lock is enabled", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(context, AddEditItem.class);
            intent.putExtra("ID", item.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView titleName, dateTime;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            parentLayout = itemView.findViewById(R.id.parent_layout);
            titleName = itemView.findViewById(R.id.itemTitle);
            dateTime = itemView.findViewById(R.id.itemDate);
        }

    }
}
