package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;


import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Adapter.ArchicleAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticlesFragment extends Fragment {
    View view;

    private RecyclerView recyclerView;
    private ArchicleAdapter adapter;
    private ArrayList<ArticleModel> articleModels;

    // define some lists for get likes comments shares views


    ArrayList<Integer> like_list = new ArrayList<>();
    ArrayList<Integer> share_list = new ArrayList<>();
    ArrayList<Integer> view_list = new ArrayList<>();


    private HashMap<Integer,ArrayList<String>> usercomments;
    ArrayList<String>comments=new ArrayList<>();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.article_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        articleModels = new ArrayList<>();

        addfakedata();

        articleModels.add(new ArticleModel(null, getResources().getString(R.string.title), getResources().getString(R.string.content), "محمد عادل", 1, like_list, share_list, view_list, usercomments));


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        adapter = new ArchicleAdapter(articleModels);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        // when click on item save object to another activity

        adapter.setOnItemClickListener(new ArchicleAdapter.OnItemClickListener() {
            @Override
            public void open_content(int position) {

                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("article object", articleModels.get(position));
                startActivity(intent);
            }
        });




    }

    private void addfakedata() {
        for (int i = 0; i < 3; i++) {
            like_list.add(i);
            share_list.add(i);
            view_list.add(i);
            comments.add(i+"تعليق");
        }
        usercomments = new HashMap<>();
        usercomments.put(1,comments);

    }


}
