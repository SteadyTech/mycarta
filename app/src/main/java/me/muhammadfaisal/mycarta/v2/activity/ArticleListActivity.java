package me.muhammadfaisal.mycarta.v2.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter.ArticleAdapter;
import me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter.ArticleInterface;
import me.muhammadfaisal.mycarta.v2.helper.CartaHelper;
import me.muhammadfaisal.mycarta.v2.helper.Constant;
import me.muhammadfaisal.mycarta.v2.model.api.Article;
import me.muhammadfaisal.mycarta.v2.retrofit.WPArticle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleListActivity extends AppCompatActivity {

    private RecyclerView recyclerArticle;

    private ArrayList<Article> articles;
    private List<WPArticle> wpArticles;

    {
        this.articles = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_article_list);

        this.init();

        this.getArticle();

        this.recyclerView();
    }



    private void init() {
        this.recyclerArticle = findViewById(R.id.recyclerArticle);
    }

    private void getArticle() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.URL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArticleInterface articleInterface = retrofit.create(ArticleInterface.class);
        Call<List<WPArticle>> call = articleInterface.getPostInfo();

        call.enqueue(new Callback<List<WPArticle>>() {
            @Override
            public void onResponse(Call<List<WPArticle>> call, Response<List<WPArticle>> response) {
                Log.d("ArticleListActivity", "OnResponse()");

                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        ArticleListActivity.this.wpArticles = response.body();

                        for (int i = 0; i < response.body().size(); i++){
                            String content = response.body().get(i).getContent().getRendered();

                            ArticleListActivity.this.articles.add(
                                    new Article(
                                            Article.IMAGE_TYPE,
                                            response.body().get(i).getTitle().getRendered(),
                                            content,
                                            response.body().get(i).getEmbedded().getMediaDetails().get(0).getSouceURL()
                                    )
                            );
                        }

                        ArticleListActivity.this.recyclerArticle.setAdapter(new ArticleAdapter(ArticleListActivity.this.articles, ArticleListActivity.this, ArticleListActivity.this));

                    }else{
                        CartaHelper.showCaution(ArticleListActivity.this, response.message(), response.errorBody().toString(), String.valueOf(response.code()));
                    }
                }else{
                    CartaHelper.showCaution(ArticleListActivity.this, response.message(), response.errorBody().toString(), String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<WPArticle>> call, Throwable t) {
                CartaHelper.showCaution(ArticleListActivity.this, String.valueOf(t.hashCode()), t.getMessage(), String.valueOf(t.getCause().toString()));
            }
        });
    }

    private void recyclerView() {
        this.recyclerArticle.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

    }
}
