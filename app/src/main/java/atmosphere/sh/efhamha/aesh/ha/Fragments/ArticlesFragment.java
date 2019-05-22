package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.HashMap;


import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Adapter.ArchicleAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticlesFragment extends Fragment {
    // firebase
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

    View view;

    private RecyclerView recyclerView;
    private ArchicleAdapter adapter;
    private ArrayList<ArticleModel> articleModels;
    private RotateLoading rotateLoading;
    RecyclerView.LayoutManager layoutManager;

    // define some lists for get likes comments shares views

    ArrayList<Integer> like_list = new ArrayList<>();
    ArrayList<Integer> share_list = new ArrayList<>();
    ArrayList<String> view_list = new ArrayList<>();

    private HashMap<Integer, ArrayList<String>> usercomments;
    ArrayList<String> comments = new ArrayList<>();


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
        rotateLoading = view.findViewById(R.id.rotateloading);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        load_all_articles();

        ref.keepSynced(true);

        // when click on item save object to another activity


    }


   /* private void addfakedata()
    {
        for (int i = 0; i < 3; i++)
        {
            like_list.add(i);
            share_list.add(i);
            view_list.add(i);
            comments.add(i + "تعليق");
        }
        usercomments = new HashMap<>();
        usercomments.put(1, comments);
    }
*/


    private void load_all_articles() {
        rotateLoading.start();


        articleModels = new ArrayList<>();

        ref.child("Articles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articleModels.clear();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    articleModels.add(dataSnapshot1.getValue(ArticleModel.class));
                    //  Toast.makeText(getActivity(), ""+articleModels.get(0).getImage_url(), Toast.LENGTH_SHORT).show();

                }
                //        Toast.makeText(getActivity(), "size" + all_books.size(), Toast.LENGTH_SHORT).show();

                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext());
                adapter = new ArchicleAdapter(getActivity(), articleModels);
               // adapter.notifyDataSetChanged();

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ArchicleAdapter.OnItemClickListener() {
                    @Override
                    public void open_content(int position) {

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            if (articleModels.get(position).getUser_view() != null)
                                view_list.addAll(articleModels.get(position).getUser_view());

                            if (!view_list.contains(user.getUid())) {
                                view_list.add(user.getUid());

                                ref.child("Articles").child(articleModels.get(position).getArch_id()).child("user_view").setValue(view_list);
                            }
                                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                                intent.putExtra("article object", articleModels.get(position));
                                startActivity(intent);

                        } else
                            Toast.makeText(getContext(), "Please sign in first", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void like_article(int position) {
                        Toast.makeText(getContext(), "Like Clicked : " + position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void comment_article(int position) {
                        Toast.makeText(getContext(), "Comment Clicked : " + position, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void share_article(int position) {
                        Toast.makeText(getContext(), "Share Clicked : " + position, Toast.LENGTH_SHORT).show();
                    }
                });

                rotateLoading.stop();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                rotateLoading.stop();
            }
        });


    }
}
