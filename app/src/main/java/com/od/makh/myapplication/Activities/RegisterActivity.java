package com.od.makh.myapplication.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.od.makh.myapplication.Activities.Dialog.SelectCityActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.MainActivity;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;
import com.od.makh.myapplication.Utils.CheckResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


import wiadevelopers.com.library.Components.CustomProgressDialog;
import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.MaskdEditText.MaskedEditText;

public class RegisterActivity extends AppCompatActivity {
    private RelativeLayout rltLine1, rltLine2;
    private LinearLayout lnCityItemsContainer;
    private MaskedEditText edtPhone;

    private TextView txtTitle, txtCity, txtExplain;
    private ImageView imgSend;
    private boolean isCitySelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize() {
        findViews();
        setupActivity();
    }

    private void findViews() {
        txtTitle = (TextView) findViewById(R.id.txtToolbarTitle);
        txtCity = (TextView) findViewById(R.id.txtCity);
        rltLine1 = (RelativeLayout) findViewById(R.id.rltvLine1);
        rltLine2 = (RelativeLayout) findViewById(R.id.rltvLine2);
        edtPhone = (MaskedEditText) findViewById(R.id.edtPhone);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        txtExplain = (TextView) findViewById(R.id.txtExplain);
        lnCityItemsContainer = (LinearLayout) findViewById(R.id.lnrCityItemsContainer);
    }

    private void setupActivity()
    {
        DivarUtils.statusBarColor(getWindow(), R.color.mainColor);
        setTypefaces();
        setListeners();
    }

    private void setTypefaces()
    {
        txtTitle.setTypeface(DivarUtils.face);
        txtCity.setTypeface(DivarUtils.face);
        txtExplain.setTypeface(DivarUtils.face);
    }

    private void setListeners()
    {
        lnCityItemsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(RegisterActivity.this, SelectCityActivity.class), Constant.REQUEST_SELECT_CITY);
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RelativeLayout layout = (RelativeLayout) findViewById(R.id.layoutParent);
                if (isCitySelected){
                    String phone = edtPhone.getRawText().toString();
                    if (!phone.equals(""))
                    {
                        if (phone.length() == Constant.PHONE_NMBER_LENGTH)
                        {
//                            Toast.makeText(getApplicationContext(),"Send",Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
//                            finish();
                            loginRequest(edtPhone.getRawText().toString());
                        }
                        else {
                            DivarUtils.createSnackBar(layout,"تعداد کاراکتر‌های وارد شده درست نمی‌باشد",DivarUtils.face).show();
                        }
                    }
                    else {
                        DivarUtils.createSnackBar(layout,"لطفا شماره تلفن خود را وارد کنید",DivarUtils.face).show();
                    }
                }
                else {
                    DivarUtils.createSnackBar(layout,"لطفا نام شهر خود را انتخاب کنید",DivarUtils.face).show();
                }
            }
        });

        edtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = edtPhone.getRawText().toString();
                if (!value.equals("")){
                    rltLine2.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.mainColor));
                }
                else {
                    rltLine2.setBackgroundColor(Color.parseColor("#d6d6d6"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SELECT_CITY && resultCode == Constant.RESULT_OK){
            String city = data.getStringExtra("city");
            isCitySelected = true;
            rltLine1.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.mainColor));
            txtCity.setText(city);
        }
    }

    private void loginRequest(final String phone){
        final String url = Constant.BASE_URL + "login.php";
        final CustomProgressDialog progressDialog = new CustomProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("لطفا صبر کنید");
        progressDialog.setIndicatorColor(R.color.mainColor);
        progressDialog.setTextColor(R.color.textColor);
        progressDialog.setTypeface(DivarUtils.face);
        progressDialog.setTextSize(16);
        progressDialog.show();

        JSONObject jsonData = new JSONObject();
        try {
            jsonData.put("phoneNumber",phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                int state = -1;
                try {
                    state = Integer.parseInt(response.getString(Constant.STATE));
                    Toast.makeText(getApplicationContext(), CheckResponse.getMessage(state),Toast.LENGTH_SHORT).show();
                    if (state == CheckResponse.SUCCESS_LOGIN || state == CheckResponse.SUCCESS_REGISTER)
                    {
                        DivarUtils.writeDataInStorage(Constant.USER_PHONE, phone);
                        DivarUtils.writeDataInStorage(Constant.USER_CITY, txtCity.getText().toString().trim());
                        startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),CheckResponse.getMessage(CheckResponse.JSON_EXCEPTION),Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        };

        Response.ErrorListener errorListener  = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error: \n"+error.toString(),Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonData, listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("Content-Type", "application/json; charset=utf-8");
                return hashMap;
            }
        };

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
