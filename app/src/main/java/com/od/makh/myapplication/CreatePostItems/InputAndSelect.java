package com.od.makh.myapplication.CreatePostItems;

import android.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.od.makh.myapplication.Activities.CreatePostActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.R;

import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;

public class InputAndSelect extends CreatePostRootView
{
    private String hint;

    private Button btnSelect;

    private EditText edtInput;
    private TextView txtTitle;
    private TextView txtUnit;

    private AlertDialog alertDialog;

    public InputAndSelect(String title, String key, String hint)
    {
        super(title, key, CreatePostRootView.INPUT_AND_SELECT);
        this.hint = hint;
        initialize();
    }

    private void initialize()
    {
        findViews();
        setTypeFaces();
        setupViews();
        setupListeners();
    }

    private void findViews()
    {
        edtInput = (EditText) super.view.findViewById(R.id.edtInput);
        txtTitle = (TextView) super.view.findViewById(R.id.txtTitle);
        txtUnit = (TextView) super.view.findViewById(R.id.txtUnit);
        btnSelect = (Button) super.view.findViewById(R.id.btnSelect);
    }

    private void setTypeFaces()
    {
        edtInput.setTypeface(DivarUtils.face);
        txtTitle.setTypeface(DivarUtils.faceLight);
        txtUnit.setTypeface(DivarUtils.faceLight);
        btnSelect.setTypeface(DivarUtils.faceLight);
    }

    private void setupViews()
    {
        edtInput.setHint(this.hint);
        txtTitle.setText(this.title);
        btnSelect.setText(Constant.FIXED);
    }

    private void setupListeners()
    {
        btnSelect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectDialog();
            }
        });
    }

    private void selectDialog()
    {
        final TextView txtTitle, txtFixed, txtFree, txtAgreement, txtChange;
        View view = DivarUtils.mLayoutInflater.inflate(R.layout.dialog_select_price_type, null);

        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtFixed = (TextView) view.findViewById(R.id.txtFixed);
        txtFree = (TextView) view.findViewById(R.id.txtFree);
        txtAgreement = (TextView) view.findViewById(R.id.txtAgreement);
        txtChange = (TextView) view.findViewById(R.id.txtChange);

        txtTitle.setTypeface(DivarUtils.faceLight);
        txtFixed.setTypeface(DivarUtils.faceLight);
        txtFree.setTypeface(DivarUtils.faceLight);
        txtAgreement.setTypeface(DivarUtils.faceLight);
        txtChange.setTypeface(DivarUtils.faceLight);

        txtFixed.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                edtInput.setVisibility(View.VISIBLE);
                txtUnit.setVisibility(View.VISIBLE);
                btnSelect.setText(txtFixed.getText().toString());
                alertDialog.dismiss();
            }
        });

        setTextViewListeners(txtFree);
        setTextViewListeners(txtAgreement);
        setTextViewListeners(txtChange);

        AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostRootView.context);
        builder.setView(view);
        builder.setCancelable(true);

        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    private void setTextViewListeners(final TextView textView)
    {
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                edtInput.setVisibility(View.GONE);
                txtUnit.setVisibility(View.GONE);
                btnSelect.setText(textView.getText().toString());
                alertDialog.dismiss();
            }
        });
    }

    public HashMap getData()
    {
        HashMap hashMap = new HashMap();
        String buttonText = btnSelect.getText().toString();
        String value = "";
        if (buttonText.equals(Constant.FIXED))
        {
            value = edtInput.getText().toString().trim();
        }
        else
        {
            value = buttonText;
        }
        hashMap.put(super.key, value);
        return hashMap;
    }

    public void restoreData(String data)
    {
        if (data.equalsIgnoreCase("مجانی") || data.equalsIgnoreCase("توافقی")
                || data.equalsIgnoreCase("جهت معاوضه"))
        {
            btnSelect.setText(data);
            edtInput.setVisibility(View.GONE);
            txtUnit.setVisibility(View.GONE);
        }
        else
        {
            edtInput.setText(data);
            btnSelect.setText(Constant.FIXED);
            edtInput.setVisibility(View.VISIBLE);
            txtUnit.setVisibility(View.VISIBLE);
        }
    }
}
