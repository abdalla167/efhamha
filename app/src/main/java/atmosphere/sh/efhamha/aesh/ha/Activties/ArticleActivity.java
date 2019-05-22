package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.HashMap;


import atmosphere.sh.efhamha.aesh.ha.Adapter.CommentAdatpterrecycle;

import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.UserModel;
import atmosphere.sh.efhamha.aesh.ha.Models.UsercommentModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticleActivity extends AppCompatActivity {
    TextView title;
    TextView content, source;
    ImageView imageArchi;
    EditText commenttext;
    MaterialRippleLayout edit_article_mrl;


    //firebase
    private DatabaseReference mdatarefre = FirebaseDatabase.getInstance().getReference();


    //for all view
    ArticleModel aricle_obj;


    ArrayList<String> comm = new ArrayList<>();

    //for comments
    private RecyclerView recyclerView;
    private CommentAdatpterrecycle adapter;
    private ArrayList<UsercommentModel> commentmodellist;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> commentscurrentuser;
    ArrayList<String> comments;


    private HashMap<String, ArrayList<String>> usercomment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        int admin = getIntent().getIntExtra("admin", 0);

        definetools();
        add_data_to_layout();
        get_comments_article();

        // fakedataforcomment();
        //  Toast.makeText(ArticleActivity.this, "dd"+aricle_obj.getUser_comments().get(1).get(0), Toast.LENGTH_SHORT).show();

        if (admin == 1) {
            edit_article_mrl.setVisibility(View.VISIBLE);
        } else {
            edit_article_mrl.setVisibility(View.GONE);
        }

    }

    private void definetools() {
        title = findViewById(R.id.article_title_full);
        content = findViewById(R.id.article_content_full);
        source = findViewById(R.id.article_by_full);
        imageArchi = findViewById(R.id.article_image_full);
        edit_article_mrl = findViewById(R.id.edit_article_mrl);

        commenttext = findViewById(R.id.comment_text);

        //define recycleview for comments
        recyclerView = findViewById(R.id.list_comments);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setNestedScrollingEnabled(false);
        // get article which saved
        Intent intent = getIntent();
        aricle_obj = intent.getParcelableExtra("article object");
    }

    private void add_data_to_layout() {
        Glide.with(this).load(aricle_obj.getImage_url()).into(imageArchi);
        title.setText(aricle_obj.getTitle());
        source.setText(aricle_obj.getSource());
        content.setText(aricle_obj.getContent());
    }


    private void get_comments_article() {
        commentmodellist = new ArrayList<>();
        if (aricle_obj.getUser_comments() != null) {

            for (final String name : aricle_obj.getUser_comments().keySet()) {
                // search  for value
                comments = aricle_obj.getUser_comments().get(name);
                // Toast.makeText(ArticleActivity.this, "" + name, Toast.LENGTH_SHORT).show();


                for (int i = 0; i < comments.size(); i++) {

                    final int finalI = i;
                    mdatarefre.child("Users").child(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            UserModel us = new UserModel();
                            us = dataSnapshot.getValue(UserModel.class);

                            UsercommentModel usercommentModel = new UsercommentModel(us.getImageUrl(), us.getUserName(), comments.get(finalI));
                            commentmodellist.add(usercommentModel);
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(this);
                adapter = new CommentAdatpterrecycle(this, commentmodellist);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }
        }

    }


    public void sned_comment(View view) {

        // first get old user comments
        commentscurrentuser = new ArrayList<>();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (aricle_obj.getUser_comments()!=null)
        {
            if (aricle_obj.getUser_comments().get(user.getUid())!=null)
            {
                commentscurrentuser.addAll(aricle_obj.getUser_comments().get(user.getUid()));
            }


        }
        if (user != null) {
            if (commenttext.getText() != null) {

                commentscurrentuser.add(commenttext.getText().toString());
                mdatarefre.child("Articles").child(aricle_obj.getArch_id()).child("user_comments").child(user.getUid()).setValue(commentscurrentuser);


                if (aricle_obj.getUser_comments() != null) {
                    if (aricle_obj.getUser_comments().get(user.getUid()) != null) {
                        commentscurrentuser.addAll(aricle_obj.getUser_comments().get(user.getUid()));
                    }


                    mdatarefre.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            UserModel us = new UserModel();
                            us = dataSnapshot.getValue(UserModel.class);

                            UsercommentModel usercommentModel = new UsercommentModel(us.getImageUrl(), us.getUserName(), commenttext.getText().toString());
                            commentmodellist.add(usercommentModel);


                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    adapter = new CommentAdatpterrecycle(this, commentmodellist);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    commenttext.setText("");


                }

            }
        }


        // get user comments


    }}
