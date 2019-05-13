package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import atmosphere.sh.efhamha.aesh.ha.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticlesFragment extends Fragment
{
    View view;

    RecyclerView recyclerView;
    List<ArticleModel> articleModels;
    articlesAdapter articlesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.article_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        articleModels = new ArrayList<>();

        articleModels.add(new ArticleModel(getResources().getString(R.string.title), getResources().getString(R.string.content), R.color.colorAccent));
        articleModels.add(new ArticleModel(getResources().getString(R.string.title), getResources().getString(R.string.content), R.color.colorAccent));
        articleModels.add(new ArticleModel(getResources().getString(R.string.title), getResources().getString(R.string.content), R.color.colorAccent));
        articleModels.add(new ArticleModel(getResources().getString(R.string.title), getResources().getString(R.string.content), R.color.colorAccent));

        articlesAdapter = new articlesAdapter(articleModels);

        recyclerView.setAdapter(articlesAdapter);
    }

    public class articlesAdapter extends RecyclerView.Adapter<articlesAdapter.Viewholder>
    {
        List<ArticleModel> articleModels;

        articlesAdapter(List<ArticleModel> articleModels)
        {
            this.articleModels = articleModels;
        }

        @NonNull
        @Override
        public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
        {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_item, viewGroup, false);
            return new  Viewholder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final Viewholder viewholder, int i)
        {
            String title = articleModels.get(i).getTitle();
            String content = articleModels.get(i).getContent();
            int image = articleModels.get(i).getImageurl();

            viewholder.title.setText(title);
            viewholder.content.setText(content);
            viewholder.image.setImageResource(image);

            viewholder.content.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getContext(), ArticleActivity.class);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()
        {
            return articleModels.size();
        }

        class Viewholder extends RecyclerView.ViewHolder
        {
            View view;

            TextView title,content;
            ImageView image;

            Viewholder(@NonNull View itemView)
            {
                super(itemView);

                view = itemView;

                title = view.findViewById(R.id.article_title);
                content = view.findViewById(R.id.article_content);
                image = view.findViewById(R.id.article_image);
            }
        }
    }
}
