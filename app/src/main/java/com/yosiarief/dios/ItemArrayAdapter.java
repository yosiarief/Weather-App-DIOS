package com.yosiarief.dios;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yosiarief.dios.model.forecast.ForecastWeatherModel;
import com.yosiarief.dios.model.forecast.List;

import java.util.ArrayList;

public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.ViewHolder> {

    //All methods in this adapter are required for a bare minimum recyclerview adapter
    private int listItemLayout;
    ForecastWeatherModel forecastWeatherModel;
    private ArrayList<List> itemList;
    Context context;
    // Constructor of the class
    public ItemArrayAdapter(int layoutId, ForecastWeatherModel forecastWeatherModel, Context context) {
        listItemLayout = layoutId;
        this.context = context;
        this.forecastWeatherModel = forecastWeatherModel;
        itemList = (ArrayList<List>) forecastWeatherModel.getList();
    }

    // get the size of the list
    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(listItemLayout, parent, false);
        ViewHolder myViewHolder = new ViewHolder(view);
        return myViewHolder;
    }

    // load data in each row element
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int listPosition) {
        TextView item = holder.item;
        String date = Helper.timestampToDate(itemList.get(listPosition).getDt());
        item.setText(date);
        holder.condition.setText(itemList.get(listPosition).getWeather().get(0).getDescription());
        float tempCel = Helper.kelvinToCelcius(itemList.get(listPosition).getMain().getTemp());
        float feel = Helper.kelvinToCelcius(itemList.get(listPosition).getMain().getFeelsLike());
        holder.temp.setText(String.format("%.0f", tempCel)+"\u2103");
        holder.hum.setText(String.format("%.0f", feel)+"\u2103");
        Glide.with(context).load("http://openweathermap.org/img/wn/"+itemList.get(listPosition).getWeather().get(0).getIcon()+"@2x.png").into(holder.imageView);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passData(listPosition);
            }
        });
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView item, condition, temp, hum;
        public ImageView imageView;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            item = (TextView) itemView.findViewById(R.id.row_item);
            condition = itemView.findViewById(R.id.row_condition);
            temp = itemView.findViewById(R.id.row_temp);
            imageView = itemView.findViewById(R.id.image_list);
            hum = itemView.findViewById(R.id.row_hum);
            linearLayout = itemView.findViewById(R.id.liniear);
        }
//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(view.getContext(), DetailWeatherActivity.class);
//            intent.putExtra("date", )
//            Log.d("onclick", "onClick " + getLayoutPosition() + " " + item.getText());
//        }
    }

    public void passData(int pos){
        Log.d("momo", itemList.get(pos).getMain().getTemp().toString());
        Intent intent = new Intent(context, DetailWeatherActivity.class);
        intent.putExtra("date", itemList.get(pos).getDt());
        intent.putExtra("temp", itemList.get(pos).getMain().getTemp());
        intent.putExtra("icon", itemList.get(pos).getWeather().get(0).getIcon());
        intent.putExtra("condition", itemList.get(pos).getWeather().get(0).getDescription());
        intent.putExtra("humidity", itemList.get(pos).getMain().getHumidity());
        intent.putExtra("pressure", itemList.get(pos).getMain().getPressure());
        intent.putExtra("wind", itemList.get(pos).getWind().getSpeed());
        intent.putExtra("location", forecastWeatherModel.getCity().getName()+" ,"+forecastWeatherModel.getCity().getCountry());
        context.startActivity(intent);
    }
}
