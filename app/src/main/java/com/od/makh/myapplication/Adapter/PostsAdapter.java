package com.od.makh.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.od.makh.myapplication.Activities.DetailPostActivity;
import com.od.makh.myapplication.Constant.Constant;
import com.od.makh.myapplication.Model.Post;
import com.od.makh.myapplication.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import wiadevelopers.com.library.MaterialLoading.MaterialProgressBar;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private ArrayList<Post> postItems;
    private Context context;
    private final int VIEW_POST_ITEM = 0;
    private final int VIEW_LOADING = 1;

    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private int visibleTreshold = 1;
    private int lastVisisbleItem, totalItemCount;
    private Realm realm;

    public PostsAdapter(Context context, ArrayList<Post> postItems, RecyclerView mRecyclerView)
    {
        this.postItems = postItems;
        this.context = context;
        this.realm = Realm.getDefaultInstance();

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisisbleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisisbleItem + visibleTreshold))
                {
                    if(onLoadMoreListener != null)
                    {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener)
    {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public boolean isLoading()
    {
        return isLoading;
    }

    public void setLoading(boolean loading)
    {
        isLoading = loading;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (postItems.get(position) != null)
            return VIEW_POST_ITEM;
        else
            return VIEW_LOADING;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == VIEW_POST_ITEM)
        {
            View view = inflater.inflate(R.layout.item_post,parent,false);
            return new postViewHolder(view);
        }
        else if (viewType == VIEW_LOADING)
        {
            View view = inflater.inflate(R.layout.item_loading,parent,false);
            return new loadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position)
    {
        if (holder instanceof postViewHolder)
        {
            postViewHolder mPostViewHolder = (PostsAdapter.postViewHolder) holder;
            mPostViewHolder.txtTitle.setText(postItems.get(position).getAdTitle());

            String price = postItems.get(position).getPrice();

            if (price.equals("مجانی") || price.equals("توافقی") || price.equals("جهت معاوضه"))
                mPostViewHolder.txtPrice.setText(postItems.get(position).getPrice());
            else
                mPostViewHolder.txtPrice.setText(postItems.get(position).getPrice() + " تومان");

            String url = postItems.get(position).getImage().getImage1();
            Glide.with(context).load(url).placeholder(R.drawable.ic_post_no_image).error(R.drawable.ic_post_no_image)
                    .into(mPostViewHolder.imgPic);

            mPostViewHolder.txtTime.setText(postItems.get(position).getAdDate());

            mPostViewHolder.crdRoot.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    final Post post = postItems.get(position);
                    Intent intent = new Intent(context, DetailPostActivity.class);
                    intent.putExtra(Constant.KEY_POST_ID,post.getId());
                    intent.putExtra(Constant.KEY_IMAGE1,post.getImage().getImage1());
                    intent.putExtra(Constant.KEY_IMAGE2,post.getImage().getImage2());
                    intent.putExtra(Constant.KEY_IMAGE3,post.getImage().getImage3());
                    intent.putExtra(Constant.KEY_IMAGE4,post.getImage().getImage4());
                    ((AppCompatActivity)context).startActivity(intent);

                    realm.executeTransaction(new Realm.Transaction()
                    {
                        @Override
                        public void execute(Realm realm)
                        {
                            RealmResults<Post> result = realm.where(Post.class).equalTo(Constant.KEY_ID,post.getId()).findAll();
                            if (result.size() == 0)
                                realm.copyToRealm(post);
                        }
                    });
                }
            });
        }
        else if (holder instanceof loadingViewHolder)
        {
            loadingViewHolder mLoadingViewHolder = (PostsAdapter.loadingViewHolder) holder;
            mLoadingViewHolder.materialProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount()
    {
        if (postItems.size() == 0)
            return 0;
        return postItems.size();
    }

    public class postViewHolder extends RecyclerView.ViewHolder
    {

        public TextView txtTitle, txtPrice, txtTime;
        public AppCompatImageView imgPic;
        public CardView crdRoot;

        public postViewHolder(@NonNull View itemView)
        {
            super(itemView);
            txtTitle = (TextView) itemView.findViewById(R.id.txtTitle);
            txtPrice = (TextView) itemView.findViewById(R.id.txtPrice);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);

            imgPic = (AppCompatImageView) itemView.findViewById(R.id.imgPic);

            crdRoot = (CardView) itemView.findViewById(R.id.crdRoot);
        }
    }

    public class loadingViewHolder extends RecyclerView.ViewHolder
    {

        public MaterialProgressBar materialProgressBar;

        public loadingViewHolder(@NonNull View itemView)
        {
            super(itemView);
            materialProgressBar = (MaterialProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
}
