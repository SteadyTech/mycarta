package me.muhammadfaisal.mycarta.home.fragment.article.adapter;

import java.util.List;

import me.muhammadfaisal.mycarta.home.fragment.article.model.Article;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticleInterface {

    @GET("android/wp-json/wp/v2/posts")
    Call<List<Article>> getPostInfo();
}
