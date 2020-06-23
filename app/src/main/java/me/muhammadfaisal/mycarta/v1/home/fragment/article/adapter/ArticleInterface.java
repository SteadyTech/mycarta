package me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter;

import java.util.List;

import me.muhammadfaisal.mycarta.v1.home.fragment.article.retrofit.WPArticle;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ArticleInterface {

    @GET("wp-json/wp/v2/posts?&_embed=true")
    Call<List<WPArticle>> getPostInfo();

}
