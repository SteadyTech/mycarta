package me.muhammadfaisal.mycarta.home.fragment.money.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import me.muhammadfaisal.mycarta.R;

public class DetailArticleActivity extends AppCompatActivity {

    ImageView imageFeature;
    TextView textSubtitle, textTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_article);

        initView();
    }

    private void initView() {

        String title = getIntent().getStringExtra("title");
        String content = getIntent().getStringExtra("content");
        String image = getIntent().getStringExtra("image");
        int position = getIntent().getIntExtra("position", 0);

        textSubtitle = findViewById(R.id.textContent);
        imageFeature = findViewById(R.id.imageViewCollapsing);
        textTitle = findViewById(R.id.textTitle);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("Detail Article | " + position);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textSubtitle.setText(content);
        textTitle.setText(title);
        Glide.with(this)
                .load(image)
                .into(imageFeature);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
