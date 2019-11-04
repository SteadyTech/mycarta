package me.muhammadfaisal.mycarta.home.fragment.article;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.muhammadfaisal.mycarta.R;
import me.muhammadfaisal.mycarta.home.fragment.article.adapter.ArticleAdapter;
import me.muhammadfaisal.mycarta.home.fragment.article.model.Article;

/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleFragment extends Fragment {

    private ProgressBar progressBar;
    private RecyclerView recyclerArticle;
    private LinearLayoutManager manager;
    private ArrayList<Article> articles;
    private ArticleAdapter adapter;

    private String baseURL = "http://www.mycarta.muhammadfaisal.me/";


    public ArticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_article, container, false);

        return v;
    }

}
