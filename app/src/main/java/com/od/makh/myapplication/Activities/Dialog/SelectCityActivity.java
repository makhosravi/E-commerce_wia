package com.od.makh.myapplication.Activities.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.od.makh.myapplication.Adapter.CityAdapter;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

import wiadevelopers.com.library.DivarUtils;

public class SelectCityActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText edtSearch;
    ListView lstCities;
    CityAdapter cityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        initialize();
    }

    private void initialize() {
        findViews();
        setupActivity();
    }

    private void findViews() {
        txtTitle = (TextView) findViewById(R.id.txtToolbarTitle);
        edtSearch = (EditText) findViewById(R.id.edtSearch);
        lstCities = (ListView) findViewById(R.id.lstCities);
    }

    private void setupActivity() {
        setTypeFaces();
        setCityData();
        setListeners();
    }

    private void setTypeFaces() {
        txtTitle.setTypeface(DivarUtils.faceLight);
        edtSearch.setTypeface(DivarUtils.faceLight);
    }

    private void setCityData(){
        final ArrayList<String> cities = new ArrayList<>();
        cities.add("بیرجند");
        cities.add("تهران");
        cities.add("کرج");
        cities.add("مشهد");
        cities.add("اصفهان");
        cities.add("تبریز");
        cities.add("شیراز");
        cities.add("اهواز");
        cities.add("قم");
        cities.add("کرمانشاه");
        cities.add("ارومیه");
        cities.add("زاهدان");
        cities.add("رشد");
        cities.add("کرمان");
        cities.add("همدان");
        cities.add("ardabil");
        cities.add("اراک");
        cities.add("یزد");
        cities.add("اردبیل");
        cities.add("بندرعباس");
        cities.add("birjand");
        cityAdapter = new CityAdapter(SelectCityActivity.this,cities);
        lstCities.setAdapter(cityAdapter);
    }

    private void setListeners(){
        lstCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                TextView textView = (TextView)view;
                String city = ((TextView)view).getText().toString();
//                Toast.makeText(getApplicationContext(),city,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("city",city);
                setResult(Constant.RESULT_OK,intent);
                finish();
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                cityAdapter.getFilter().filter(edtSearch.getText().toString());
            }
        });
    }
}
