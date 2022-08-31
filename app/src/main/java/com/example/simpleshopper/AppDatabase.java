package com.example.simpleshopper;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ShoppingList.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ShoppingListDao shoppingListDao();
}