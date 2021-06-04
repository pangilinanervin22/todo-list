package com.example.todo_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class AddEditItem extends AppCompatActivity {

    EditText titleItem, textItem;
    TextView dateItem;
    Button deleteItem, saveItem, backToMain;
    int textItemCursor, intentID, itemID;

    String special;
    DataManagement dataManagement;
    Item currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_add_edit_item);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setAllViews();
        onClickMethods();
        textLimit();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setAllViews(){
        titleItem = findViewById(R.id.edit_title_item);
        textItem = findViewById(R.id.edit_text_item);
        deleteItem = findViewById(R.id.button_delete_item);
        saveItem = findViewById(R.id.button_save_item);
        backToMain = findViewById(R.id.button_goto_main);
        dateItem = findViewById(R.id.text_date_item);

        dataManagement = new DataManagement(this);
        String currentTime = String.valueOf(DateFormat.format("MMMM d, yyyy HH:mm aa", new Date().getTime()));
        dateItem.setText(currentTime);
        deleteItem.setVisibility(View.GONE);
        saveItem.setBackground(getDrawable(R.drawable.ic_add_list));
        itemID = dataManagement.getLastID() + 1;

        Intent intent = getIntent();
        intentID = intent.getIntExtra("ID", -1);
        if (intentID >= 0)
            for (Item object : dataManagement.getAllItem())
                if (object.getId() == intentID) {
                    currentItem = object;
                    itemID = currentItem.getId();
                    titleItem.setText(currentItem.getTitle());
                    textItem.setText(currentItem.getTextNote());
                    dateItem.setText(currentItem.getDate());
                    deleteItem.setVisibility(View.VISIBLE);
                    saveItem.setBackground(getDrawable(R.drawable.ic_save));
                }
    }

    public void onClickMethods() {
        Intent goToMain = new Intent(this, MainActivity.class);

        deleteItem.setOnClickListener(v -> {
            Item temp = new Item(itemID, titleItem.getText().toString(), dateItem.getText().toString(),
                    textItem.getText().toString());
            darkenOnClick(v);

            AlertDialog.Builder builder = new AlertDialog.Builder(AddEditItem.this);
            builder.setMessage("Are you sure you want delete this? \n it cannot restore again");
            builder.setCancelable(true);

            builder.setNegativeButton("cancel",
                    (dialog, id) -> dialog.cancel());


            builder.setPositiveButton("delete",
                    (dialog, id) -> {
                        dataManagement.deleteOne(currentItem);
                        Toast.makeText(this, "deleted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(goToMain);
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        saveItem.setOnClickListener(v -> {
            Item temp = new Item(itemID, titleItem.getText().toString(), dateItem.getText().toString(),
                    textItem.getText().toString());
            darkenOnClick(v);

            if (intentID >= 0) {
                dataManagement.updatePresident(temp);
                Toast.makeText(this, "update successfully", Toast.LENGTH_SHORT).show();
            } else {
                if (temp.getTitle().equals(""))
                    return;

                dataManagement.addOne(temp);
                Toast.makeText(this, temp.getTitle() + "is added successfully", Toast.LENGTH_SHORT).show();
            }

            startActivity(goToMain);
        });

        backToMain.setOnClickListener(v -> {
            darkenOnClick(v);
            startActivity(goToMain);
        });


    }

    public void textLimit() {
        textItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textItemCursor = textItem.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                textItem.removeTextChangedListener(this);

                if (textItem.getLineCount() > 25) {
                    textItem.setText(special);
                    textItem.setSelection(textItemCursor);
                } else
                    special = textItem.getText().toString();

                textItem.addTextChangedListener(this);
            }
        });
    }

    private void darkenOnClick(View view) {
        soundOnClick();
        view.setBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
        new Thread(() -> runOnUiThread(() -> {
            SystemClock.sleep(100);
            view.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        })).start();
    }

    private void soundOnClick(){
        MediaPlayer mp = new MediaPlayer();
        mp = MediaPlayer.create(this, R.raw.clicksounds);
        mp.start();
    }


}