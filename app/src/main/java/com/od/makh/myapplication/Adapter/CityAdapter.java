package com.od.makh.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.od.makh.myapplication.R;

import java.util.ArrayList;

import wiadevelopers.com.library.DivarUtils;

public class CityAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<String> cities = new ArrayList<>();
    private final ArrayList<String> tmpCities;
    private LayoutInflater inflater = null;

    public CityAdapter(Context context, ArrayList<String> cities) {
        this.context = context;
        this.cities = cities;
        this.tmpCities=cities;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cities.size();
    }

    @Override
    public Object getItem(int i) {
        return cities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<String> myResult = new ArrayList<>();
                for (int i = 0; i < tmpCities.size(); i++) {

                    if (charSequence.length() > 0){
                        if (tmpCities.get(i).contains(charSequence.toString())){
                            myResult.add(tmpCities.get(i));
                        }
                    }
                    else{
                        myResult.add(tmpCities.get(i));
                    }

                }
                results.count = myResult.size();
                results.values = myResult;
                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                cities = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public static class ViewHolder{
        public TextView txtText;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        ViewHolder holder;

        if (view == null){
            vi = inflater.inflate(R.layout.item_city, null);

            holder = new ViewHolder();
            holder.txtText = (TextView) vi.findViewById(R.id.itemCityTxtText);
            vi.setTag(holder);
        }
        else
            holder = (ViewHolder) vi.getTag();
        holder.txtText.setTypeface(DivarUtils.faceLight);
        if (cities.size() != 0)
            holder.txtText.setText(cities.get(i));
        else
            holder.txtText.setText("آیتمی یافت نشد.");
        return vi;
    }

}
