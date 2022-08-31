package com.example.simpleshopper;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ShoppingList {
    @PrimaryKey(autoGenerate = true)
    public int listId;

    @ColumnInfo(name = "list_name")
    public String listName;

    @ColumnInfo(name = "list_amount")
    public Integer listAmount;

    @ColumnInfo(name = "list_status")
    public Integer listStatus;


}
