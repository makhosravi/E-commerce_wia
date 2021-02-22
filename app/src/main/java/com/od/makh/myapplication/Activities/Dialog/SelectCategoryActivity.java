package com.od.makh.myapplication.Activities.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.od.makh.myapplication.Activities.CreatePostActivity;
import com.od.makh.myapplication.Adapter.CategoriesAdapter;
import com.od.makh.myapplication.Application.G;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Category;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

import wiadevelopers.com.library.DivarUtils;

public class SelectCategoryActivity extends AppCompatActivity
{

    TextView txtTitle;
    AutoCompleteTextView edtSearch;
    ListView lstCategories;
    ArrayList<String> stackIds = new ArrayList<>();
    ArrayList<Category> categories = new ArrayList<>();
    CategoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        initilaize();
    }

    private void initilaize()
    {
        findViews();
        setTypefaces();
        setupActivity();
        setListeners();
    }

    private void findViews()
    {
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        edtSearch = (AutoCompleteTextView) findViewById(R.id.edtSearch);
        lstCategories = (ListView) findViewById(R.id.lstCategories);
    }

    private void setTypefaces()
    {
        txtTitle.setTypeface(DivarUtils.face);
        edtSearch.setTypeface(DivarUtils.face);
    }

    private void setupActivity()
    {
        adapter = new CategoriesAdapter(SelectCategoryActivity.this, categories);
        lstCategories.setAdapter(adapter);
        assignArrayList("", false);

        ArrayList<String> itemCategories = new ArrayList<>();
        for (int i = 0; i < G.CATEGORIES.size(); i++)
        {
            if (G.CATEGORIES.get(i).isLastNode())
            {
                itemCategories.add(G.CATEGORIES.get(i).getTitle());
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,
                itemCategories);
        edtSearch.setAdapter(adapter);
        edtSearch.setThreshold(1);
    }

    private void assignArrayList(String id, boolean isBackward)
    {
        categories.clear();
        for (int i = 0; i < G.CATEGORIES.size(); i++)
        {
            if (G.CATEGORIES.get(i).getId().length() == id.length() + 1)
            {
                if (G.CATEGORIES.get(i).getId().startsWith(id))
                {
                    categories.add(G.CATEGORIES.get(i));
                }
            }
        }
        adapter.notifyDataSetChanged();
        if (!isBackward)
        {
            stackIds.add(id);
        }
    }

    @Override
    public void onBackPressed() {
        if (stackIds.size() > 1)
        {
            stackIds.remove(stackIds.size() - 1);
            String id = stackIds.get(stackIds.size() - 1);
            lstCategories.setAnimation(DivarUtils.animSlideLeft());
            assignArrayList(id, true);
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void setListeners()
    {
        lstCategories.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                if (!categories.get(position).isLastNode())
                {
                    assignArrayList(categories.get(position).getId(), false);
                    lstCategories.setAnimation(DivarUtils.animSlideRight());
                }
                else
                {
                    String mId = categories.get(position).getId();
                    String mTitle = categories.get(position).getTitle();
                    Intent intent = new Intent();
                    intent.putExtra(Constant.KEY_ID, mId);
                    intent.putExtra(Constant.KEY_TITLE, mTitle);
                    setResult(Constant.RESULT_OK,intent);
                    finish();
                }
            }
        });

        edtSearch.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {
                String text = edtSearch.getText().toString();

                int counter = 0;

                for (int i = 0; i < G.CATEGORIES.size(); i++)
                {
                    if (G.CATEGORIES.get(i).getTitle().equals(text))
                    {
                        if (position == counter)
                        {
                            String mId = G.CATEGORIES.get(i).getId();
                            String mTitle = G.CATEGORIES.get(i).getTitle();
                            Intent intent = new Intent();
                            intent.putExtra(Constant.KEY_ID, mId);
                            intent.putExtra(Constant.KEY_TITLE, mTitle);
                            setResult(Constant.RESULT_OK,intent);
                            finish();
                        }
                        else
                        {
                            counter++;
                        }
                    }
                }
            }
        });
    }
}
