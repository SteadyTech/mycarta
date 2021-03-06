package me.muhammadfaisal.mycarta.v1.home.fragment.article;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter.ArticleAdapter;
import me.muhammadfaisal.mycarta.v1.home.fragment.article.adapter.ArticleInterface;
import me.muhammadfaisal.mycarta.v2.model.api.Article;
import me.muhammadfaisal.mycarta.v2.retrofit.WPArticle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerArticle;
    private LinearLayoutManager manager;
    private ArrayList<Article> articles;
    private ArticleAdapter adapter;

    private String baseURL = "";

    public static List<WPArticle> wpArticles;
    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        initWidgets(v);

        getRetrofit();


        adapter = new ArticleAdapter(articles, getActivity(), getActivity());
        recyclerArticle.setAdapter(adapter);

        return v;
    }

    private void getRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArticleInterface articleInterface = retrofit.create(ArticleInterface.class);
        Call<List<WPArticle>> call = articleInterface.getPostInfo();

        call.enqueue(new Callback<List<WPArticle>>() {
            @Override
            public void onResponse(Call<List<WPArticle>> call, Response<List<WPArticle>> response) {
                Log.e("ArticleList", "response = " + response.message());

                if (response.body() != null) {

                    wpArticles = response.body();
                    progressBar.setVisibility(View.GONE);

                    for (int i = 0; i < response.body().size(); i++){
                        String content =  response.body().get(i).getContent().getRendered();

                        articles.add(new Article(
                                Article.IMAGE_TYPE,
                                response.body().get(i).getTitle().getRendered(),
                                content,
                                response.body().get(i).getEmbedded().getMediaDetails().get(0).getSouceURL()
                        ));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<WPArticle>> call, Throwable t) {

            }
        });
    }

    private void initWidgets(View v) {
        recyclerArticle = v.findViewById(R.id.recyclerArticle);
        progressBar = v.findViewById(R.id.progressBar);

        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerArticle.setLayoutManager(manager);

        articles = new ArrayList<>();
    }

    public static List<WPArticle> getWpArticles(){
        return wpArticles;
    }

}
