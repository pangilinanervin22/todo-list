package com.example.todo_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ItemRecyclerAdapter recyclerAdapter;
    private DataManagement dataManagement;
    private List<Item> itemList;

    // activating onclick of each item
    public static boolean isRecyclerItemLock = true;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        dataManagement = new DataManagement(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);


        setRecyclerView();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem lock = menu.findItem(R.id.menu_lockButton);
        if (isRecyclerItemLock)
            lock.setIcon(getDrawable(R.drawable.ic_lock_close));
        else
            lock.setIcon(getDrawable(R.drawable.ic_lock_open));

        return true;
    }

    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        soundOnClick();
        switch (item.getItemId()) {
            case R.id.menu_lockButton:
                isRecyclerItemLock = !isRecyclerItemLock;

                if (isRecyclerItemLock)
                    item.setIcon(getDrawable(R.drawable.ic_lock_close));
                else
                    item.setIcon(getDrawable(R.drawable.ic_lock_open));
                return true;
            case R.id.add_item:
                startActivity(new Intent(this, AddEditItem.class));
                return true;
            case R.id.menu_order_alpha:
                Collections.sort(itemList, Item.alphaCompare);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_order_reverseAlpha:
                Collections.sort(itemList, Item.alphaCompare);
                Collections.reverse(itemList);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_order_date:
                Collections.sort(itemList, Item.dateCompare);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_order_reverseDate:
                Collections.sort(itemList, Item.dateCompare);
                Collections.reverse(itemList);
                recyclerAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setRecyclerView() {
        itemList = dataManagement.getAllItem();
        recyclerAdapter = new ItemRecyclerAdapter(itemList, this);
        recyclerView.setAdapter(recyclerAdapter);
    }

    private void soundOnClick(){
        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.clicksounds);
        mp.start();
    }
}