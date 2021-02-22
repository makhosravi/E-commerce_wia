package com.od.makh.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.od.makh.myapplication.Model.Category;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

import wiadevelopers.com.library.DivarUtils;

public class CategoriesAdapter extends BaseAdapter
{
    private ArrayList<Category> categories = new ArrayList<>();
    private LayoutInflater inflater = null;

    public CategoriesAdapter(Context context, ArrayList<Category> categories)
    {
        this.categories = categories;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return categories.size();
    }

    @Override
    public Object getItem(int position)
    {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public static class ViewHolder
    {
        public TextView txtCategory;
        public ImageView imgArrow;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        View vi = view;
        ViewHolder holder;
        if (view == null)
        {
            vi = inflater.inflate(R.layout.item_category,null);
            holder = new ViewHolder();
            holder.txtCategory = (TextView) vi.findViewById(R.id.txtCategory);
            holder.imgArrow = (ImageView) vi.findViewById(R.id.imgArrow);
            vi.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) vi.getTag();
        }

        if (categories.size() != 0)
        {
            holder.txtCategory.setTypeface(DivarUtils.face);
            holder.txtCategory.setText(categories.get(position).getTitle());

            if (categories.get(position).isLastNode())
            {
                holder.imgArrow.setVisibility(View.GONE);
            }
            else
            {
                holder.imgArrow.setVisibility(View.VISIBLE);
            }
        }
        return vi;
    }
}
