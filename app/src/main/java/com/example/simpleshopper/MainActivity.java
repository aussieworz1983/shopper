package com.example.simpleshopper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    Context context ;
    ArrayList <String> itemList = new ArrayList();
    ScrollView sv;
    LinearLayout ll;
    LinearLayout itemHolder;

    int index = 0;
    int btnIndex = 2000;

    LinearLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        context = this;
        Button addBtn = new Button(this);
        Button saveBtn = new Button(this);
        Button loadBtn = new Button(this);

        EditText itemInput = new EditText(this);
        EditText listNameInput = new EditText(this);
        itemInput.setTextSize(38);
        listNameInput.setTextSize(38);
        rootLayout = (LinearLayout) findViewById(R.id.RootLayout);

        sv = new ScrollView(this);
        sv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll = new LinearLayout(this);
        ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        ll.setOrientation(LinearLayout.VERTICAL);

        addBtn.setText("Add Item");
        saveBtn.setText("Save List");
        loadBtn.setText("Load List");


        for(int i = 0;i < itemList.size();i++){
            AddItem(itemList.get(i));
        }




        rootLayout.addView(sv);
        sv.addView(ll);
        ll.addView(itemInput);
        ll.addView(addBtn);



        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        try {

            ShowToast("data base created");
            ShoppingListDao listDao = db.shoppingListDao();
            List<ShoppingList> lists = listDao.getAll();

            for (ListIterator<ShoppingList> iter = lists.listIterator(); iter.hasNext(); ) {
                ShoppingList element = iter.next();
                String itemVal = element.listName;
                AddItem(itemVal);
                // 1 - can call methods of element
                // 2 - can use iter.remove() to remove the current element from the list
                // 3 - can use iter.add(...) to insert a new element into the list
                //     between element and iter->next()
                // 4 - can use iter.set(...) to replace the current element

                // ...
            }
            Log.d("ids" ,lists.toString());
            Log.d("db created","success");
        }catch (Exception e){
            ShowToast("data base creation failed");
            Log.d("db creation failed","failure");
        }



        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = itemInput.getText().toString();
                itemList.add(value);

                AddItem(value);
                AddToDb(value,db);

                itemInput.setText("");
            }
        });


    }
    public void AddItem(String item){


        itemHolder = new LinearLayout(this);
        itemHolder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        itemHolder.setOrientation(LinearLayout.HORIZONTAL);
        Button delBtn = new Button(this);

        delBtn.setText("Delete");
        TextView itemText = new TextView(this);
        itemText.setText(item);
        itemText.setWidth(800);
        itemText.setTextSize(32);
        itemText.setId(index);
        delBtn.setId(index+btnIndex);
        ll.addView(itemHolder);

        itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView textView = (TextView)findViewById(v.getId());

                if((textView.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG)>0){
                    textView.setPaintFlags( textView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                }else{
                    textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }

            }
        });

        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int btnId = v.getId();
                DeleteItem(btnId);
            }
        });





        itemHolder.addView(itemText);

        itemHolder.addView(delBtn);
        index++;
    }
    public void DeleteItemFromDb(String name){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        try {


            ShoppingListDao listDao = db.shoppingListDao();
           ShoppingList item = listDao.findByName(name);
           listDao.delete(item);
        }catch (Exception e){

        }
    }
    public void DeleteItem(int btnId){

        int theItemId = btnId - 2000;
        TextView theText = (TextView)findViewById(theItemId);

        String name = theText.getText().toString();


        Button theBtn = (Button)findViewById(btnId);
        itemHolder.endViewTransition(theBtn);
        itemHolder.endViewTransition(theText);
        theBtn.setVisibility(View.GONE);
        theText.setVisibility(View.GONE);
        DeleteItemFromDb(name);

        ShowToast("Item Deleted");
    }
    public void ShowToast(String msg){
        Context context = getApplicationContext();


        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }
    public void AddToDb(String val,AppDatabase db){
        try {

                    ShoppingListDao listDao = db.shoppingListDao();
                     ShoppingList list = new ShoppingList();

                     list.listName=val;
                     list.listStatus=0;


                     listDao.insertAll(list);



        }catch (Exception e){
            ShowToast("Save failed");
            Log.d("failed save",e.toString());
        }
    }
}