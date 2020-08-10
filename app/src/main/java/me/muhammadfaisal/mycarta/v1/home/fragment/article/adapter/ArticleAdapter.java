package me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v2.model.api.Article;
import me.muhammadfaisal.mycarta.v1.home.fragment.article.view.DetailArticleActivity;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    ArrayList<Article> articles;
    Context context;
    Activity activity;

    public ArticleAdapter(ArrayList<Article> articles, Context context, Activity activity) {
        this.articles = articles;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ArticleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ArticleAdapter.ViewHolder holder, final int position) {
        final Article article = articles.get(position);

        holder.textTitle.setText(article.title);

        Glide.with(context)
                .load(article.getImage())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d("FailParsingImage", e.getMessage() + " " + model);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.imageFeature);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailArticleActivity.class);
                i.putExtra("image", article.getImage());
                i.putExtra("content", article.getSubtitle());
                i.putExtra("title", article.getTitle());
                i.putExtra("position", position);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(activity), holder.imageFeature, "image_article");
                ActivityCompat.startActivity(context, i, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textTitle;
        ImageView imageFeature;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textTitle = itemView.findViewById(R.id.textTitle);
            imageFeature = itemView.findViewById(R.id.imageFeature);
        }
    }
}
