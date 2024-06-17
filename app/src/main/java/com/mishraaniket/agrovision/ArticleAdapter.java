package com.mishraaniket.agrovision;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androidnetworking.widget.ANImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    // setting the TAG for debugging purposes
    private static String TAG="ArticleAdapter";

    private ArrayList<NewsArticle> mArrayList;
    private Context mContext;

    public ArticleAdapter(Context context,ArrayList<NewsArticle> list){
        // initializing the constructor
        this.mContext=context;
        this.mArrayList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating the layout with the article view (R.layout.article_item)
        View view=LayoutInflater.from(mContext).inflate(R.layout.article_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // the parameter position is the index of the current article
        // getting the current article from the ArrayList using the position
        NewsArticle currentArticle=mArrayList.get(position);

        // setting the text of textViews
        holder.title.setText(currentArticle.getTitle());
        holder.description.setText(currentArticle.getDescription());

        // subString(0,10) trims the date to make it short
        holder.contributordate.setText(currentArticle.getAuthor()+
                " | "+currentArticle.getPublishedAt().substring(0,10));

        // Loading image from network into
        // Fast Android Networking View ANImageView
        holder.image.setImageUrl(currentArticle.getUrlToImage());

        // setting the content Description on the Image
        holder.image.setContentDescription(currentArticle.getContent());

        // Using Glide for image loading

        Glide.with(mContext)  // Get Glide instance with context (replace with your context)
                .load(currentArticle.getUrlToImage())  // Load image URL from data object
                .placeholder(R.drawable.placeholder)  // Set placeholder image while loading
                .error(R.drawable.placeholder)  // Set error image if loading fails
                .into(holder.image);  // Load image into the ImageView




        // handling click event of the article
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // an intent to the WebActivity that display web pages
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("url_key",currentArticle.getUrl());

                // starting an Activity to display the page of the article
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        // declaring the views
        private TextView title,description,contributordate;
        private ANImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // assigning views to their ids
            title=itemView.findViewById(R.id.title_id);
            description=itemView.findViewById(R.id.description_id);
            image=itemView.findViewById(R.id.image_id);
            contributordate=itemView.findViewById(R.id.contributordate_id);
        }
    }

}
