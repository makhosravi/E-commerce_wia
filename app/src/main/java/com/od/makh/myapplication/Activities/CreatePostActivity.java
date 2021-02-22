package com.od.makh.myapplication.Activities;

import androidx.annotation.Nullable;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.od.makh.myapplication.Activities.Dialog.SelectCategoryActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.CreatePostItems.ContainItems;
import com.od.makh.myapplication.CreatePostItems.CreatePostRootView;
import com.od.makh.myapplication.CreatePostItems.InputAndSelect;
import com.od.makh.myapplication.CreatePostItems.JustInput;
import com.od.makh.myapplication.CreatePostItems.Optional;
import com.od.makh.myapplication.CreatePostItems.SelectImage;
import com.od.makh.myapplication.MainActivity;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;
import com.od.makh.myapplication.Utils.CheckResponse;
import com.od.makh.myapplication.Utils.RuntimePermissionsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wiadevelopers.com.library.Components.CustomProgressDialog;
import wiadevelopers.com.library.DivarUtils;

public class CreatePostActivity extends RuntimePermissionsActivity
{

    //Toolbar
    ImageView imgBack, imgDiscardPost, imgDraftPost;
    TextView txtToolbarTitle, txtSend;

    // choose category
    TextView txtTitle1, txtSubject1, txtTitle2;
    Button btnChoose1, btnChoose2;

    LinearLayout lnrContainer1, lnrContainer2, lnrInsertPoint;

    ArrayList<CreatePostRootView> rootViews = new ArrayList<>();
    private String selectedId = null, selectedTitle = null;

    /*public static Context context;*/  //not a safe approach

    private int finalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        initialize();
    }

    @Override
    public void onPermissionsGranted(int requestCode)
    {
        for (int i = 0; i < rootViews.size(); i++)
        {
            CreatePostRootView rootView = rootViews.get(i);
            if (rootView instanceof SelectImage)
            {
                SelectImage selectImage = (SelectImage) rootView;
                selectImage.onPermissionsGranted(Constant.REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        }
    }

    @Override
    public void onPermissionsDeny(int requestCode)
    {
        for (int i = 0; i < rootViews.size(); i++)
        {
            CreatePostRootView rootView = rootViews.get(i);
            if (rootView instanceof SelectImage)
            {
                SelectImage selectImage = (SelectImage) rootView;
                selectImage.onPermissionsDeny(Constant.REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        }
    }

    private void initialize()
    {
        /*context = CreatePostActivity.this;*/  // not a safe approach
        CreatePostRootView.setContext(CreatePostActivity.this);
        findViews();
        setupActivity();
        setTypefaces();
        setupListeners();
        restoreData();
    }

    private void findViews()
    {
        imgBack = (ImageView) findViewById(R.id.imgBack);
        imgDiscardPost = (ImageView) findViewById(R.id.imgDiscardPost);
        imgDraftPost = (ImageView) findViewById(R.id.imgDraftPost);
        txtToolbarTitle = (TextView) findViewById(R.id.txtToolbarTitle);
        txtSend = (TextView) findViewById(R.id.txtSend);

        txtTitle1 = (TextView) findViewById(R.id.txtTitle1);
        txtTitle2 = (TextView) findViewById(R.id.txtTitle2);
        txtSubject1 = (TextView) findViewById(R.id.txtSubject1);
        btnChoose1 = (Button) findViewById(R.id.btnChoose1);
        btnChoose2 = (Button) findViewById(R.id.btnChoose2);
        lnrContainer1 = (LinearLayout) findViewById(R.id.lnrContainer1);
        lnrContainer2 = (LinearLayout) findViewById(R.id.lnrContainer2);
        lnrInsertPoint = (LinearLayout) findViewById(R.id.lnrContainer3);
    }

    private void setupActivity()
    {
        lnrContainer1.setVisibility(View.VISIBLE);
        lnrContainer2.setVisibility(View.GONE);

        txtToolbarTitle.setText("درج آگهی");
        txtSend.setText(" " + "ارسال");

        DivarUtils.statusBarColor(getWindow(), R.color.mainColor);

        Drawable img = DivarUtils.createDrawable(R.drawable.ic_check_white_48dp, Constant.TEXTVIEW_DRAWABLE_SIZE);
        txtSend.setCompoundDrawables(null, null, img, null);
    }

    private void setTypefaces()
    {
        txtToolbarTitle.setTypeface(DivarUtils.face);
        txtSend.setTypeface(DivarUtils.face);

        txtTitle1.setTypeface(DivarUtils.faceLight);
        txtTitle2.setTypeface(DivarUtils.faceLight);
        txtSubject1.setTypeface(DivarUtils.faceLight);

        btnChoose1.setTypeface(DivarUtils.faceLight);
        btnChoose2.setTypeface(DivarUtils.faceLight);
    }

    private void setupListeners()
    {
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        imgDiscardPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DivarUtils.writeDataInStorage(Constant.KEY_DRAFT,null);
                finish();
            }
        });

        imgDraftPost.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap hashMap = new HashMap();

                hashMap.put(Constant.KEY_ID, selectedId);
                hashMap.put(Constant.KEY_TITLE, selectedTitle);

                for (int i = 0; i < rootViews.size(); i++)
                {
                    hashMap.putAll(rootViews.get(i).getData());
                }

//                Log.i("myHashmap", hashMap.toString());
                JSONObject jsonObject = new JSONObject(hashMap);
                DivarUtils.writeDataInStorage(Constant.KEY_DRAFT, jsonObject.toString());
                Toast.makeText(getApplicationContext(),"اطلاعات شما به صورت پیش نویس ذخیره شد", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isAllow = true;
                for (int i = 0; i < rootViews.size(); i++)
                {
                    if (rootViews.get(i).isFill())
                    {
                        rootViews.get(i).noColoredView();
                    }
                    else
                    {
                        isAllow = false;
                        rootViews.get(i).coloredView();
                    }
                }

                if (!isAllow)
                {
                    Toast.makeText(getApplicationContext(),"لطفا فیلد‌های رنگی را پر کنید", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    planDialog();
//                    Toast.makeText(getApplicationContext(),"داده‌ها ارسال شد", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChoose1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePostActivity.this, SelectCategoryActivity.class);
                startActivityForResult(intent, Constant.REQUEST_SELECT_CATEGORY);
            }
        });

        btnChoose2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatePostActivity.this, SelectCategoryActivity.class);
                startActivityForResult(intent, Constant.REQUEST_SELECT_CATEGORY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SELECT_CATEGORY && resultCode == Constant.RESULT_OK && data != null)
        {
            String id = data.getStringExtra(Constant.KEY_ID);
            String title = data.getStringExtra(Constant.KEY_TITLE);
            selectedId = id;
            selectedTitle = title;

            btnChoose1.setText(title);
            btnChoose2.setText(title);

            lnrContainer1.setVisibility(View.GONE);
            lnrContainer2.setVisibility(View.VISIBLE);

            setData(id);

//            Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
        }

        else if (requestCode == Constant.REQUEST_CAPTURE_PHOTO ||
                requestCode == Constant.REQUEST_CROP_IMAGE ||
                requestCode == Constant.REQUEST_CHOOSE_IMAGE_GALLERY)
        {
            if (resultCode == Constant.RESULT_OK)
            {
                for (int i = 0; i < rootViews.size(); i++)
                {
                    CreatePostRootView rootView = rootViews.get(i);
                    if (rootView instanceof SelectImage)
                    {
                        SelectImage selectImage = (SelectImage) rootView;
                        selectImage.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    private void setData(String mId)
    {
        int id = Integer.parseInt(mId.toString().trim());

        if (rootViews.size() != 0)
        {
            rootViews.clear();
        }
        /*if (id == 111)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("قیمت کل", Constant.PRICE, "300،000،000 (سیصد میلیون تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "فروشی", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));
            ArrayList<String> items = new ArrayList<>();
            items.add("لطفا انتخاب کنید");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));
            rootViews.add(new SelectImage("عکس"));
            *//*rootViews.add(new JustInput("تلفن", Constant.PHONE, "091xxxxxxxx"
                    , "درج شماره تماس باعث میشود زودتر به نتیجه برسید"));*//*
            rootViews.add(new JustInput("عنوان", Constant.TITLE, "آپارتمان ملاصدرا 130 متر"
                    , "از عنوان هایی استفاده کنید که دربردارنده مهمترین ویژگی های آگهی شما است. در آگهی های املاک، اشاره به محله ی ملک و متراژ آن موجب بیشتر دیده شدن آگهی می شود"));
            rootViews.add(new JustInput("توضیحات", Constant.EXPLAIN, ""
                    , "تمام جزئیات و نکات قابل توجه آگهی خود را به صورت کامل و دقیق ذکر کنید؛ توجه به این مورد به صورت قابل توجهی ابهامات کاربران را برطرف خواهد کرد و شانس موفقیت آگهی شما را افزایش خواهد داد."));
        }*/

        if (id == 111 || id == 112)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("قیمت کل", Constant.PRICE, "300،000،000 (سیصد میلیون تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "فروشی", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));

            ArrayList<String> items = new ArrayList<>();
            items.add("لطفا انتخاب کنید");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));
        }
        else if (id == 113)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("قیمت کل", Constant.PRICE, "300،000،000 (سیصد میلیون تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "فروشی", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));

        }
        else if (id == 121 || id == 122)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("ودیعه", Constant.TRUST, "100،000،000 (صد میلیون تومان)"));
            rootViews.add(new InputAndSelect("اجاره ماهیانه", Constant.RENT, "300،000 (سیصد هزار تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "ارائه", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));

            ArrayList<String> items = new ArrayList<>();
            items.add("لطفا انتخاب کنید");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));
        }

        else if (id == 131 || id == 132 || id == 133)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("قیمت کل", Constant.PRICE, "300،000،000 (سیصد میلیون تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "فروشی", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));

            ArrayList<String> items = new ArrayList<>();
            items.add("لطفا انتخاب کنید");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));

            rootViews.add(new Optional("سند اداری", Constant.OFFICIAL_DOCUMENT, "ندارد", "دارد"));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));
        }
        else if (id == 141 || id == 142 || id == 143)
        {
            rootViews.add(new JustInput("متراژ (متر مربع)", Constant.AREA, "150"));
            rootViews.add(new InputAndSelect("ودیعه", Constant.TRUST, "100،000،000 (صد میلیون تومان)"));
            rootViews.add(new InputAndSelect("اجاره ماهیانه", Constant.RENT, "300،000 (سیصد هزار تومان)"));
            rootViews.add(new Optional("نوع", Constant.ORDER_TYPE, "ارائه", "درخواستی"));
            rootViews.add(new Optional("نوع آگهی دهنده", Constant.ADVERTISER_TYPE, "شخصی", "مشاور املاک"));
            ArrayList<String> items = new ArrayList<>();
            items.add("لطفا انتخاب کنید");
            items.add("بدون اتاق");
            items.add("یک");
            items.add("دو");
            items.add("سه");
            items.add("چهار");
            items.add("پنج یا بیشتر");
            rootViews.add(new ContainItems("تعداد اتاق", Constant.NUMBER_OF_ROOM, items));
            rootViews.add(new Optional("حومه شهر", Constant.IS_COUNTRY_SIDE, "نیست", "هست"));
        }

        rootViews.add(new SelectImage("عکس"));
        //rootViews.add(new JustInput("تلفن", Constant.PHONE, "09111111111", "درج شماره تماس موجب می شود زودتر به نتیجه برسید"));
        rootViews.add(new JustInput("عنوان", Constant.TITLE, "آپارتمان ملاصدرا 130 متر", "از عنوان هایی استفاده کنید که دربردارنده مهمترین ویژگی های آگهی شما است. در آگهی های املاک، اشاره به محله ی ملک و متراژ آن موجب بیشتر دیده شدن آگهی می شود"));
        rootViews.add(new JustInput("توضیحات", Constant.EXPLAIN, "", "تمام جزئیات و نکات قابل توجه آگهی خود را به صورت کامل و دقیق ذکر کنید؛ توجه به این مورد به صورت قابل توجهی ابهامات کاربران را برطرف خواهد کرد و شانس موفقیت آگهی شما را افزایش خواهد داد."));

        /*CreatePostRootView rootView = rootViews.get(0);
        boolean result = rootView instanceof ContainItems;*/

        setupLayouts();
    }

    private void setupLayouts()
    {
        lnrInsertPoint.removeAllViews();
        for (int i = 0; i < rootViews.size(); i++)
        {
            View view = rootViews.get(i).getView();
            lnrInsertPoint.addView(view);
        }
    }

    private void restoreData()
    {
        String data = DivarUtils.readDataFromStorage(Constant.KEY_DRAFT, null);
        if (data != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(data);
                lnrContainer1.setVisibility(View.GONE);
                lnrContainer2.setVisibility(View.VISIBLE);
                btnChoose1.setText(jsonObject.getString(Constant.KEY_TITLE));
                btnChoose2.setText(jsonObject.getString(Constant.KEY_TITLE));

                selectedId = jsonObject.getString(Constant.KEY_ID);
                selectedTitle = jsonObject.getString(Constant.KEY_TITLE);

                setData(jsonObject.getString(Constant.KEY_ID));

                for (int i = 0; i < rootViews.size(); i++)
                {
                    String key = rootViews.get(i).getKey();
                    rootViews.get(i).restoreData(jsonObject.getString(key));
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void planDialog()
    {
        final CheckBox chkIsEmergency;
        final RadioButton rdBtnFree, rdBtnCost1, rdBtnCost2;
        final TextView txtPay, txtCancel;

        View promptsView = LayoutInflater.from(this).inflate(R.layout.dialog_choose_plan, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptsView);

        chkIsEmergency = (CheckBox) promptsView.findViewById(R.id.chkIsEmergency);
        rdBtnFree = (RadioButton) promptsView.findViewById(R.id.rdbtnFree);
        rdBtnCost1 = (RadioButton) promptsView.findViewById(R.id.rdbtnCost1);
        rdBtnCost2 = (RadioButton) promptsView.findViewById(R.id.rdbtnCost2);
        txtPay = (TextView) promptsView.findViewById(R.id.txtPay);
        txtCancel = (TextView) promptsView.findViewById(R.id.txtCancel);

        Typeface typeface = DivarUtils.face;

        chkIsEmergency.setTypeface(typeface);
        rdBtnFree.setTypeface(typeface);
        rdBtnCost1.setTypeface(typeface);
        rdBtnCost2.setTypeface(typeface);
        txtPay.setTypeface(typeface);
        txtCancel.setTypeface(typeface);

        rdBtnFree.setChecked(true);

        chkIsEmergency.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked)
            {
                if (isCheked)
                {
                    finalPrice += 6000;
                }
                else
                {
                    finalPrice -= 6000;
                }
                setText(txtPay);
            }
        });

        rdBtnFree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked)
            {
                setText(txtPay);
            }
        });

        rdBtnCost1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked)
            {
                if (isCheked)
                {
                    finalPrice += 5000;
                }
                else
                {
                    finalPrice -= 5000;
                }
                setText(txtPay);
            }
        });

        rdBtnCost2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isCheked)
            {
                if (isCheked)
                {
                    finalPrice += 10000;
                }
                else
                {
                    finalPrice -= 10000;
                }
                setText(txtPay);
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();

        txtPay.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                HashMap map = new HashMap();

                String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE, null);
                String city = DivarUtils.readDataFromStorage(Constant.USER_CITY, null);

                map.put(Constant.USER_PHONE, phone);
                map.put(Constant.USER_CITY, city);
                map.put(Constant.CATEGORY_ID, selectedId);

                if (chkIsEmergency.isChecked())
                {
                    map.put(Constant.IS_EMERGENCY, Constant.EMERGENCY);
                }
                else
                {
                    map.put(Constant.IS_EMERGENCY, Constant.NOT_EMERGENCY);
                }

                map.put(Constant.COST, finalPrice + "");

                String lifeTime = null;
                if (rdBtnFree.isChecked())
                {
                    lifeTime = "5";
                }
                else if (rdBtnCost1.isChecked())
                {
                    lifeTime = "10";
                }
                else if (rdBtnCost2.isChecked())
                {
                    lifeTime = "15";
                }

                map.put(Constant.LIFE_TIME, lifeTime);

                for (int i = 0; i < rootViews.size(); i++)
                {
                    map.putAll(rootViews.get(i).getData());
                }

                JSONObject jsonObject = new JSONObject(map);

                setAdRequest(jsonObject);
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                alertDialog.dismiss();
            }
        });

        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialogInterface)
            {
                finalPrice = 0;
            }
        });

        alertDialog.show();
    }

    private void setText(TextView txtPay)
    {
        if (finalPrice == 0)
        {
            txtPay.setText("پرداخت");
        }
        else
        {
            txtPay.setText("پرداخت " + finalPrice + " تومان");
        }
    }

    private void setAdRequest(JSONObject jsonData)
    {
        final String url = Constant.BASE_URL + "setAd.php";
        final CustomProgressDialog progressDialog = new CustomProgressDialog(CreatePostActivity.this);
        progressDialog.setMessage("لطفا صبر کنید");
        progressDialog.setIndicatorColor(R.color.mainColor);
        progressDialog.setTextColor(R.color.textColor);
        progressDialog.setTypeface(DivarUtils.face);
        progressDialog.setTextSize(16);
        progressDialog.show();

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                Toast.makeText(getApplicationContext(),"Success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
            }
        };

        Response.ErrorListener errorListener  = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(getApplicationContext(),"Error: \n"+error.toString(),Toast.LENGTH_SHORT).show();
                AppSingleton.getInstance(getApplicationContext()).cancelPendingRequests();
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

        final int socketTimeout = 100000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }
}
