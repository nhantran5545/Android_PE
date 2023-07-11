package com.example.prm392_pe01_ce160513;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private Cursor cursor;
    private CarClickListener listener;

    public CarAdapter(CarClickListener listener) {
        this.listener = listener;
    }

    public void setCursor(Cursor cursor) {
        if (this.cursor != null) {
            this.cursor.close();
        }
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        if (cursor != null && cursor.moveToPosition(position)) {
            String model = cursor.getString(cursor.getColumnIndex(CarSQLiteHelper.COLUMN_MODEL));
            int price = cursor.getInt(cursor.getColumnIndex(CarSQLiteHelper.COLUMN_PRICE));

            holder.tvModel.setText(model);
            holder.tvPrice.setText(String.valueOf(price));
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public interface CarClickListener {
        void onCarClick(String model, int price);
    }

    class CarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvModel, tvPrice;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (cursor != null && cursor.moveToPosition(position)) {
                String model = cursor.getString(cursor.getColumnIndex(CarSQLiteHelper.COLUMN_MODEL));
                int price = cursor.getInt(cursor.getColumnIndex(CarSQLiteHelper.COLUMN_PRICE));
                listener.onCarClick(model, price);
            }
        }
    }
}

