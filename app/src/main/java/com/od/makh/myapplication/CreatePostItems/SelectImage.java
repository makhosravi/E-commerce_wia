package com.od.makh.myapplication.CreatePostItems;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.od.makh.myapplication.Activities.CreatePostActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import wiadevelopers.com.library.DivarUtils;
import wiadevelopers.com.library.ImageController.ImageController;
import wiadevelopers.com.library.ImageController.onItemDeleteListener;

public class SelectImage extends CreatePostRootView
{

    private Button btnSelect;
    private TextView txtTitle;
    private TextView txtHint;
    private LinearLayout lnrInsert;

    private AlertDialog alertDialog;

    private Activity activity;
    private ImageController imageController;

//    private static final String KEY_NO_IMAGE = "noImageSelected";
    private static final String KEY_NO_IMAGE = "NO_IMAGE";

    public SelectImage(String title)
    {
        super(title, Constant.IS_IMAGE, CreatePostRootView.SELECT_IMAGE);
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
        txtTitle = (TextView) super.view.findViewById(R.id.txtTitle);
        txtHint = (TextView) super.view.findViewById(R.id.txtHint);
        btnSelect = (Button) super.view.findViewById(R.id.btnSelect);
        lnrInsert = (LinearLayout) super.view.findViewById(R.id.lnrInsert);
    }

    private void setTypeFaces()
    {
        txtTitle.setTypeface(DivarUtils.faceLight);
        txtHint.setTypeface(DivarUtils.face);
        btnSelect.setTypeface(DivarUtils.faceLight);
    }

    private void setupViews()
    {
        txtTitle.setText(title);
        activity = (Activity) (CreatePostRootView.context);
        imageController = new ImageController(activity, "عکس اصلی", 3, 600);
    }

    private void setupListeners()
    {
        btnSelect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
//                selectDialog();
                ((CreatePostActivity)activity).requestAppPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        Constant.REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE);
            }
        });

        imageController.setOnItemDeleteListener(new onItemDeleteListener()
        {
            @Override
            public void onDelete(int i)
            {
                setupLayouts();
            }
        });
    }

    public void onPermissionsGranted(int requestCode)
    {
        if (requestCode == Constant.REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE)
        {
            selectDialog();
        }
    }

    public void onPermissionsDeny(int requestCode)
    {
        if (requestCode == Constant.REQUEST_PERMISSIONS_READ_EXTERNAL_STORAGE)
        {
            Toast.makeText(activity, "جواز خواندن از حافظه داده نشد", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectDialog()
    {
        final TextView txtTitle, txtCamera, txtGallery;
        View view = DivarUtils.mLayoutInflater.inflate(R.layout.dialog_select_camera_or_gallery, null);
        txtTitle = (TextView) view.findViewById(R.id.txtTitle);
        txtCamera = (TextView) view.findViewById(R.id.txtCamera);
        txtGallery = (TextView) view.findViewById(R.id.txtGallery);

        Typeface typeface = DivarUtils.face;

        txtTitle.setTypeface(typeface);
        txtCamera.setTypeface(typeface);
        txtGallery.setTypeface(typeface);

        txtCamera.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DivarUtils.capturePhoto(activity, Constant.REQUEST_CAPTURE_PHOTO);
                alertDialog.dismiss();
            }
        });

        txtGallery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DivarUtils.getImageFromGallery(activity, "عکستان را انتخاب کنید", Constant.REQUEST_CHOOSE_IMAGE_GALLERY);
                alertDialog.dismiss();
            }
        });

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreatePostRootView.context);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {

        String message = "اپلیکیشن برش عکس خود را انتخاب کنید";

        if (requestCode == Constant.REQUEST_CAPTURE_PHOTO)
        {
            DivarUtils.ImageCropFunction(activity, DivarUtils.getCapturedPhotoUri(), message,
                    Constant.REQUEST_CROP_IMAGE);
        }
        else if (requestCode == Constant.REQUEST_CHOOSE_IMAGE_GALLERY)
        {
            DivarUtils.ImageCropFunction(activity, data, message,Constant.REQUEST_CROP_IMAGE);
        }
        else if (requestCode == Constant.REQUEST_CROP_IMAGE)
        {
            imageController.addBitmap(DivarUtils.getBitmap(data));
            setupLayouts();
        }
    }

    private void setupLayouts()
    {
        lnrInsert.removeAllViews();
        for (int i = 0; i < imageController.getImagesCount(); i++)
        {
            View view = imageController.getView(i);
            lnrInsert.addView(view);
        }

        if (imageController.isFull())
        {
            btnSelect.setVisibility(View.GONE);
        }
        else
        {
            btnSelect.setVisibility(View.VISIBLE);
        }
    }

    public HashMap getData()
    {
        HashMap hashMap = new HashMap();
        JSONObject json = imageController.getData();
        if (json.length() != 0)
        {
            hashMap.put(super.key, json);
        }
        else
        {
            hashMap.put(super.key, KEY_NO_IMAGE);
        }
        return hashMap;
    }

    public void restoreData(String data)
    {
        if (!data.equals(KEY_NO_IMAGE))
        {
            try
            {
                JSONObject jsonObject = new JSONObject(data);
                for (int i = 0; i < jsonObject.length(); i++)
                {
                    String encodedImage = jsonObject.getString("img" +  (i + 1));
                    imageController.addBitmap(DivarUtils.getBitmap(encodedImage));
                }

                setupLayouts();
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
