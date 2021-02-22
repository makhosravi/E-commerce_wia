package com.od.makh.myapplication.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.od.makh.myapplication.Activities.DetailPostActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Post;
import com.od.makh.myapplication.R;
import com.od.makh.myapplication.Utils.AppSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import wiadevelopers.com.library.Components.CustomProgressDialog;
import wiadevelopers.com.library.DivarUtils;

public class PostsAdapterType2 extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private ArrayList<Post> itemList;
    private Context mContext;

    private int type = -1;

    public static final int BOOK_MARK = 654;
    public static final int LAST_SEEN = 789;
    public static final int MY_ADVERTISE = 946;

    private Realm realm;

    public PostsAdapterType2(Context mContext, ArrayList<Post> itemList, int type)
    {
        this.mContext = mContext;
        this.itemList = itemList;
        this.type = type;
        this.realm = Realm.getDefaultInstance();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_post_type2,parent,false);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof postViewHolder)
        {
            postViewHolder mPostViewHolder = (postViewHolder) holder;
            final Post post = itemList.get(position);

            mPostViewHolder.txtTitle.setText(post.getAdTitle());
            mPostViewHolder.txtTime.setText(post.getAdDate());
            String url = post.getImage().getImage1();
            Glide.with(mContext).load(url).placeholder(R.drawable.ic_post_no_image).error(R.drawable.ic_post_no_image)
                    .into(mPostViewHolder.imgPic);

            mPostViewHolder.imgPerson.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    getUserPhone(position);
                }
            });

            mPostViewHolder.imgShare.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Toast.makeText(mContext,"اشتراک گذاشته شد :)",Toast.LENGTH_LONG).show();
                }
            });

            mPostViewHolder.imgClear.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (type == BOOK_MARK)
                        markedPost(position);
                    else if (type == LAST_SEEN)
                    {
                        realm.executeTransaction(new Realm.Transaction()
                        {
                            @Override
                            public void execute(Realm realm)
                            {
                                String id = post.getId();
                                RealmResults<Post> results = realm.where(Post.class).equalTo(Constant.KEY_ID, id).findAll();
                                if (results.size() == 1)
                                {
                                    results.get(0).deleteFromRealm();
                                    itemList.remove(position);
                                    notifyDataSetChanged();
                                }
                            }
                        });
                    }
                    else if (type == MY_ADVERTISE)
                        deletePost(position);
                }
            });

            mPostViewHolder.crdRoot.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent nextPage = new Intent(mContext, DetailPostActivity.class);
                    nextPage.putExtra(Constant.KEY_IMAGE1,post.getImage().getImage1());
                    nextPage.putExtra(Constant.KEY_IMAGE2,post.getImage().getImage2());
                    nextPage.putExtra(Constant.KEY_IMAGE3,post.getImage().getImage3());
                    nextPage.putExtra(Constant.KEY_IMAGE4,post.getImage().getImage4());
                    nextPage.putExtra(Constant.KEY_POST_ID,post.getId());
                    mContext.startActivity(nextPage);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return itemList == null ? 0 : itemList.size();
    }

    static class postViewHolder extends RecyclerView.ViewHolder
    {

        public TextView txtTitle;
        public TextView txtTime;
        public AppCompatImageView imgPic;
        public CardView crdRoot;

        public AppCompatImageView imgPerson;
        public AppCompatImageView imgShare;
        public AppCompatImageView imgClear;

        public postViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtTime = itemView.findViewById(R.id.txtTime);
            imgPic = itemView.findViewById(R.id.imgPic);
            imgClear =  itemView.findViewById(R.id.imgClear);
            imgShare = itemView.findViewById(R.id.imgShare);
            imgPerson = itemView.findViewById(R.id.imgPerson);
            crdRoot = itemView.findViewById(R.id.crdRoot);

            txtTitle.setTypeface(DivarUtils.faceLight);
            txtTime.setTypeface(DivarUtils.face);
        }
    }

    private void getUserPhone(int index)
    {
        final String requestUrl = Constant.BASE_URL + "userPhone.php";

        final CustomProgressDialog progressDialog = new CustomProgressDialog(mContext);
        progressDialog.setMessage("لطفا صبر کنید");
        progressDialog.setTextSize(16);
        progressDialog.setTypeface(DivarUtils.face);
        progressDialog.show();

        JSONObject jsonData = new JSONObject();
        try
        {
            jsonData.put(Constant.USER_ID, itemList.get(index).getUserId());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    final String phoneNumber = "0" + response.getString(Constant.phoneNumber);
                    android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(mContext).create();
                    alertDialog.setTitle("شماره تماس");
                    alertDialog.setMessage(phoneNumber);
                    alertDialog.setButton(android.app.AlertDialog.BUTTON_NEUTRAL, "تماس",
                            new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                                    mContext.startActivity(intent);
                                }
                            });
                    alertDialog.show();
                    progressDialog.dismiss();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(mContext, "لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonData, listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

    private void markedPost(final int listPosition)
    {
        final String requestUrl = Constant.BASE_URL + "mark.php";

        JSONObject jsonData = new JSONObject();
        try
        {
            String phone = DivarUtils.readDataFromStorage(Constant.USER_PHONE, null);
            jsonData.put(Constant.KEY_POST_ID, itemList.get(listPosition).getId());
            jsonData.put(Constant.USER_PHONE, phone);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    String result = response.getString(Constant.STATUS);
                    if (result.equals(Constant.UN_MARKED))
                    {
                        itemList.remove(listPosition);
                        notifyDataSetChanged();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(mContext, "لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonData, listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }

    private void deletePost(final int listPosition)
    {
        final String requestUrl = Constant.BASE_URL + "deleteAd.php";

        JSONObject jsonData = new JSONObject();
        try
        {
            jsonData.put(Constant.KEY_POST_ID, itemList.get(listPosition).getId());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Response.Listener<JSONObject> listener = new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {
                try
                {
                    response.getString(Constant.STATE);
                    itemList.remove(listPosition);
                    notifyDataSetChanged();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(mContext,"خطا در اتصال ، مجددا تلاش کنید",Toast.LENGTH_SHORT).show();
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, requestUrl, jsonData, listener, errorListener)
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        AppSingleton.getInstance(mContext).addToRequestQueue(jsonObjectRequest);
    }
}
