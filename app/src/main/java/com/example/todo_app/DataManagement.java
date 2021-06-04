package com.example.todo_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataManagement extends SQLiteOpenHelper {

    public static final String ITEM_TABLE = "ITEM_TABLE";
    public static final String ITEM_NAME = "ITEM_NAME";
    public static final String ITEM_DATE = "ITEM_DATE";
    public static final String ITEM_TEXT = "ITEM_TEXT";

    public DataManagement(Context context) {
        super(context, ITEM_TABLE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ITEM_TABLE + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ITEM_NAME + " TEXT, " + ITEM_DATE + " TEXT, " + ITEM_TEXT + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public List<Item> getAllItem() {
        List<Item> list = new ArrayList<>();
        String query = "SELECT * FROM " + ITEM_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String date = cursor.getString(2);
                String text = cursor.getString(3);

                Item temp = new Item(id, name, date, text);
                list.add(temp);
            } while (cursor.moveToNext());

        cursor.close();
        db.close();

        return list;
    }

    public void addOne(Item item){
        if (item.getTextNote().equals(""))
            item.setTextNote( item.getTitle() +" details");

        for (Item object : getAllItem())
            if (object.getId() == item.getId() || object.getTitle().contains(item.getTitle()))
                return;

        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(ITEM_NAME, item.getTitle());
        cv.put(ITEM_DATE, item.getDate());
        cv.put(ITEM_TEXT, item.getTextNote());

        writableDatabase.insert(ITEM_TABLE, null, cv);
    }

    public void updatePresident(Item object) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("ID", object.getId());
        cv.put(ITEM_NAME, object.getTitle());
        cv.put(ITEM_DATE, object.getDate());
        cv.put(ITEM_TEXT, object.getTextNote());

        writableDatabase.update(ITEM_TABLE, cv, "ID = " + object.getId(), null);
    }

    public void deleteOne(Item object) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        writableDatabase.delete(ITEM_TABLE, "ID = " + object.getId(), null);
    }

    public int getLastID(){
        if (getAllItem().size() == 0)
            return 0;

        List<Item> temp = getAllItem();
        return temp.get(temp.size() - 1).getId();
    }
}
