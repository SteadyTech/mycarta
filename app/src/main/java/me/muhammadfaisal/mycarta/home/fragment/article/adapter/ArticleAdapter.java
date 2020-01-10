package me.muhammadfaisal.mycarta.home.fragment.article.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Objects;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.fragment.article.ArticleFragment;
import me.muhammadfaisal.mycarta.home.fragment.article.model.Article;
import me.muhammadfaisal.mycarta.home.fragment.article.view.DetailArticleActivity;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {

    ArrayList<Article> articles;
    Context context;
    ArticleFragment articleFragment;

    public ArticleAdapter(ArrayList<Article> articles, Context context, ArticleFragment articleFragment) {
        this.articles = articles;
        this.context = context;
        this.articleFragment = articleFragment;
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
                .load(article.Image)
                .into(holder.imageFeature);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, DetailArticleActivity.class);
                i.putExtra("image", article.Image);
                i.putExtra("content", article.subtitle);
                i.putExtra("title", article.title);
                i.putExtra("position", position);
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(Objects.requireNonNull(articleFragment.getActivity()), holder.imageFeature, "image_article");
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
