package com.example.pub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlankActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PackingListAdapter adapter;
    private List<String> packingItems;

    private static final String PREFS_NAME = "MyPrefs";
    private static final String PREF_PACKING_ITEMS = "PackingItems";

    private EditText newItemEditText;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newItemEditText = findViewById(R.id.newItemEditText);
        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToList();
            }
        });

        // Restore the list of packing items from SharedPreferences
        packingItems = restorePackingItems();

        // Create the adapter
        adapter = new PackingListAdapter(packingItems);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save the list of packing items to SharedPreferences
        savePackingItems(packingItems);
    }

    private List<String> restorePackingItems() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> itemsSet = prefs.getStringSet(PREF_PACKING_ITEMS, new HashSet<>());
        return new ArrayList<>(itemsSet);
    }

    private void savePackingItems(List<String> items) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> itemsSet = new HashSet<>(items);
        editor.putStringSet(PREF_PACKING_ITEMS, itemsSet);
        editor.apply();
    }

    private void addItemToList() {
        String newItem = newItemEditText.getText().toString().trim();
        if (!newItem.isEmpty()) {
            packingItems.add(newItem);
            adapter.notifyDataSetChanged();
            newItemEditText.setText("");
        }
    }

    public void openNewBlankActivity(View view) {
        // Get the selected items from the adapter
        List<String> selectedItems = adapter.getSelectedItems();

        // Create an intent to launch the NewBlankActivity
        Intent intent = new Intent(this, NewBlankActivity.class);
        intent.putStringArrayListExtra("selectedItems", new ArrayList<>(selectedItems));

        // Start the NewBlankActivity
        startActivity(intent);
    }
}
