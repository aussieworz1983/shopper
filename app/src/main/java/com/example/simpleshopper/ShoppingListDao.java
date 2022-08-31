package com.example.simpleshopper;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ShoppingListDao {
    @Query("SELECT * FROM shoppinglist")
    List<ShoppingList> getAll();

    @Query("SELECT * FROM shoppinglist WHERE listId IN (:listIds)")
    List<ShoppingList> loadAllByIds(int[] listIds);

    @Query("SELECT * FROM shoppinglist WHERE list_name LIKE :listname ")
    ShoppingList findByName(String listname);

    @Insert
    void insertAll(ShoppingList... lists);

    @Delete
    void delete(ShoppingList list);


}
