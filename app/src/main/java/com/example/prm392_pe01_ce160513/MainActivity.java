package com.example.prm392_pe01_ce160513;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements CarAdapter.CarClickListener {

    private EditText etModel, etPrice;
    private Button btnAdd, btnUpdate, btnDelete;
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etModel = findViewById(R.id.etModel);
        etPrice = findViewById(R.id.etPrice);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        carAdapter = new CarAdapter(this);
        recyclerView.setAdapter(carAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCar();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCar();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCar();
            }
        });

        loadCars();
    }

    private void loadCars() {
        Uri uri = CarContentProvider.CONTENT_URI;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        carAdapter.setCursor(cursor);
    }

    private void addCar() {
        String model = etModel.getText().toString().trim();
        String priceString = etPrice.getText().toString().trim();
        if (model.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }
        int price = Integer.parseInt(priceString);

        ContentValues values = new ContentValues();
        values.put(CarSQLiteHelper.COLUMN_MODEL, model);
        values.put(CarSQLiteHelper.COLUMN_PRICE, price);

        Uri uri = getContentResolver().insert(CarContentProvider.CONTENT_URI, values);
        if (uri != null) {
            Toast.makeText(this, "Car added successfully", Toast.LENGTH_SHORT).show();
            etModel.setText("");
            etPrice.setText("");
            loadCars();
        } else {
            Toast.makeText(this, "Failed to add car", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateCar() {
        String model = etModel.getText().toString().trim();
        String priceString = etPrice.getText().toString().trim();
        if (model.isEmpty() || priceString.isEmpty()) {
            Toast.makeText(this, "Please enter valid data", Toast.LENGTH_SHORT).show();
            return;
        }
        int price = Integer.parseInt(priceString);

        ContentValues values = new ContentValues();
        values.put(CarSQLiteHelper.COLUMN_MODEL, model);
        values.put(CarSQLiteHelper.COLUMN_PRICE, price);

        Uri uri = CarContentProvider.CONTENT_URI;
        int rowsUpdated = getContentResolver().update(uri, values, null, null);
        if (rowsUpdated > 0) {
            Toast.makeText(this, "Car updated successfully", Toast.LENGTH_SHORT).show();
            etModel.setText("");
            etPrice.setText("");
            loadCars();
        } else {
            Toast.makeText(this, "Failed to update car", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteCar() {
        String model = etModel.getText().toString().trim();
        if (model.isEmpty()) {
            Toast.makeText(this, "Please enter a car model to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = CarContentProvider.CONTENT_URI;
        int rowsDeleted = getContentResolver().delete(uri, CarSQLiteHelper.COLUMN_MODEL + "=?", new String[]{model});
        if (rowsDeleted > 0) {
            Toast.makeText(this, "Car deleted successfully", Toast.LENGTH_SHORT).show();
            etModel.setText("");
            etPrice.setText("");
            loadCars();
        } else {
            Toast.makeText(this, "Failed to delete car", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCarClick(String model, int price) {
        etModel.setText(model);
        etPrice.setText(String.valueOf(price));
    }
}
