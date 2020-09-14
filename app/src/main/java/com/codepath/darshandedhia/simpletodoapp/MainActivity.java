package com.codepath.darshandedhia.simpletodoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;

    Button btnAdd;
    EditText textBox;
    RecyclerView listView;
    ItemsAdapter itemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.addButton);
        textBox = findViewById(R.id.editTextTextPersonName);
        listView = findViewById(R.id.listView);


        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener= new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                saveItems();
                Toast.makeText(getApplicationContext(), "Todo item removed", Toast.LENGTH_SHORT).show();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        listView.setAdapter(itemsAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String todoItem = textBox.getText().toString();
                if (todoItem == null || todoItem.isEmpty() || todoItem.equalsIgnoreCase("")){
                    return;
                }
                items.add(todoItem);
                itemsAdapter.notifyItemInserted(items.size() - 1);
                textBox.setText("");
                saveItems();
                Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todos.txt");
    }

    private void loadItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }

    private void saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving items", e);
        }
    }
}