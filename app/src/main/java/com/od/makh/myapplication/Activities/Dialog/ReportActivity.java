package com.od.makh.myapplication.Activities.Dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.od.makh.myapplication.R;

import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.Toasty.Toasty;

public class ReportActivity extends AppCompatActivity
{

    RadioButton rdBtn1, rdBtn2, rdBtn3, rdBtn4, rdBtn5, rdBtn6, rdBtn7;
    TextView txtTitle;
    TextView txtSend, txtCancel;
    EditText edtExplain;

    View layoutExplain, layoutOptions;

    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_report);

        int width = (int) ((DivarUtils.widthPX) * ((double) 98 / 100));
        int height = (int) ((DivarUtils.heightPX) * ((double) 70 / 100));
        getWindow().setLayout(width,height);

        initialize();
    }

    private void initialize()
    {
        findViews();
        setupActivity();
        setupListeners();
    }

    private void findViews()
    {
        rdBtn1 = findViewById(R.id.reportRdbtn1);
        rdBtn2 = findViewById(R.id.reportRdbtn2);
        rdBtn3 = findViewById(R.id.reportRdbtn3);
        rdBtn4 = findViewById(R.id.reportRdbtn4);
        rdBtn5 = findViewById(R.id.reportRdbtn5);
        rdBtn6 = findViewById(R.id.reportRdbtn6);
        rdBtn7 = findViewById(R.id.reportRdbtn7);

        edtExplain = findViewById(R.id.edtExplain);

        txtTitle = findViewById(R.id.txtTitle);
        txtSend = findViewById(R.id.txtSend);
        txtCancel = findViewById(R.id.txtCancel);

        layoutExplain = findViewById(R.id.layoutExplain);
        layoutOptions = findViewById(R.id.layoutOptions);
    }

    private void setupActivity()
    {
        Typeface typeface = DivarUtils.face;
        rdBtn1.setTypeface(typeface);
        rdBtn2.setTypeface(typeface);
        rdBtn3.setTypeface(typeface);
        rdBtn4.setTypeface(typeface);
        rdBtn5.setTypeface(typeface);
        rdBtn6.setTypeface(typeface);
        rdBtn7.setTypeface(typeface);

        txtTitle.setTypeface(DivarUtils.faceLight);
        txtSend.setTypeface(DivarUtils.face);
        txtCancel.setTypeface(DivarUtils.faceLight);

        edtExplain.setTypeface(DivarUtils.face);

        txtSend.setVisibility(View.GONE);
        layoutExplain.setVisibility(View.GONE);
    }

    private void setupListeners()
    {
        txtCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        radioButtonClickListener(rdBtn1);
        radioButtonClickListener(rdBtn2);
        radioButtonClickListener(rdBtn3);
        radioButtonClickListener(rdBtn4);
        radioButtonClickListener(rdBtn5);
        radioButtonClickListener(rdBtn6);

        rdBtn7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation animIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_in_right);
                Animation animOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_out_right);
                int interval = 500;
                animOut.setDuration(interval);
                animIn.setDuration(interval);
                layoutExplain.setVisibility(View.VISIBLE);

                animOut.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        layoutOptions.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {

                    }
                });

                layoutOptions.startAnimation(animOut);
                layoutExplain.startAnimation(animIn);
                txtSend.setVisibility(View.VISIBLE);
            }
        });

        txtSend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (rdBtn7.isChecked())
                {
                    data = edtExplain.getText().toString().trim();
                    if (data.equalsIgnoreCase(""))
                        Toasty.error(getApplicationContext(),"توضیحی در مورد علت گزارش آگهی بنویسید").show();
                        //Toast.makeText(getApplicationContext(),"توضیحی در مورد علت گزارش آگهی بنویسید",Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(),data,Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void radioButtonClickListener (final RadioButton radioButton)
    {
        radioButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                data = radioButton.getText().toString().trim();
                txtSend.setVisibility(View.VISIBLE);
            }
        });
    }
}
