package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;


import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.Fragments.ArticlesFragment;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.CommentModel;
import atmosphere.sh.efhamha.aesh.ha.Models.UserModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class ArticleActivity extends AppCompatActivity
{
    TextView title,time;
    TextView content, source,comments_count;
    ImageView imageArchi;
    MaterialRippleLayout edit_article_mrl;
    EditText commenttext;
    FloatingActionButton add_comment_btn;
    RotateLoading rotateLoading;
    LinearLayout bottom;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<CommentModel, commentsViewHolder> firebaseRecyclerAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String KEY,name,imageurl;

    int admin;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        init();
        addData();
        displayComments(KEY);
    }

    public void init ()
    {
        title = findViewById(R.id.article_title_full);
        time = findViewById(R.id.article_time_full);
        content = findViewById(R.id.article_content_full);
        source = findViewById(R.id.article_by_full);
        imageArchi = findViewById(R.id.article_image_full);
        edit_article_mrl = findViewById(R.id.edit_article_mrl);
        comments_count = findViewById(R.id.comments_count);
        commenttext = findViewById(R.id.comment_text);
        add_comment_btn = findViewById(R.id.add_comment_btn);
        rotateLoading = findViewById(R.id.rotateloading);
        bottom = findViewById(R.id.bottom);

        rotateLoading.start();

        recyclerView = findViewById(R.id.recyclerview);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        add_comment_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String content = commenttext.getText().toString();

                if (TextUtils.isEmpty(content))
                {
                    Toast.makeText(getApplicationContext(), "من فضلك اضف تعليقا", Toast.LENGTH_SHORT).show();
                    return;
                }

                addComment(name,imageurl,content,KEY);
            }
        });
    }

    private void addComment(String name, String imageurl, String content,String key)
    {
        CommentModel commentModel = new CommentModel(imageurl,name,content,getUid());
        String comment_key = databaseReference.child("Comments").push().getKey();
        databaseReference.child("Comments").child(key).child(comment_key).setValue(commentModel);

        Toast.makeText(getApplicationContext(), "تم اضافة تعليق بنجاح", Toast.LENGTH_SHORT).show();
        commenttext.setText("");
    }

    public void addData ()
    {
        admin = getIntent().getIntExtra("admin", 0);
        final ArticleModel articleModel = (ArticleModel) getIntent().getSerializableExtra("ar");
        KEY = getIntent().getStringExtra("ar2");

        if (admin == 1)
        {
            edit_article_mrl.setVisibility(View.VISIBLE);
            bottom.setVisibility(GONE);
        } else
        {
            edit_article_mrl.setVisibility(View.GONE);
            returnData(getUid());
        }

        title.setText(articleModel.getTitle());
        String time_txt = articleModel.getArticle_time() + "\n" + articleModel.getArticle_day() + " " + articleModel.getArticle_month() + " " + articleModel.getArticle_year();
        time.setText(time_txt);
        source.setText(articleModel.getSource());
        content.setText(articleModel.getContent());

        if (articleModel.getType() == 1)
        {
            /*
            Picasso.get()
                    .load(articleModel.getImage_url())
                    .placeholder(R.drawable.ic_darkgrey)
                    .error(R.drawable.ic_darkgrey)
                    .into(imageArchi);
                    */
        } else
            {
                imageArchi.setImageResource(R.drawable.ic_youtube);

                imageArchi.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent intent = new Intent(getApplicationContext(), VideoActivity.class);
                        intent.putExtra("url", articleModel.getImage_url());
                        startActivity(intent);
                    }
                });
            }

        edit_article_mrl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                PopupMenu popup = new PopupMenu(ArticleActivity.this, edit_article_mrl);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.edit_comment, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                {
                    public boolean onMenuItemClick(MenuItem item)
                    {
                        switch (item.getItemId())
                        {
                            case R.id.delete:
                                /*databaseReference.child("Comments").child(KEY).child(key).removeValue();
                                Toast.makeText(getApplicationContext(), "تم حذف التعليق", Toast.LENGTH_SHORT).show();*/
                                return true;
                            default:
                                return true;
                        }
                    }});
                popup.show(); //showing popup menu
            }
        });
    }

    private void displayComments(String key)
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Comments")
                .child(key)
                .limitToLast(50);

        FirebaseRecyclerOptions<CommentModel> options =
                new FirebaseRecyclerOptions.Builder<CommentModel>()
                        .setQuery(query, CommentModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentModel, commentsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final commentsViewHolder holder, int position, @NonNull final CommentModel model)
            {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.materialRippleLayout.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        PopupMenu popup = new PopupMenu(ArticleActivity.this, holder.materialRippleLayout);
                        //Inflating the Popup using xml file
                        popup.getMenuInflater()
                                .inflate(R.menu.edit_comment, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener()
                        {
                            public boolean onMenuItemClick(MenuItem item)
                            {
                                switch (item.getItemId())
                                {
                                    case R.id.delete:
                                        databaseReference.child("Comments").child(KEY).child(key).removeValue();
                                        Toast.makeText(getApplicationContext(), "تم حذف التعليق", Toast.LENGTH_SHORT).show();
                                        return true;
                                    default:
                                        return true;
                                }
                            }});
                        popup.show(); //showing popup menu
                    }
                });

                databaseReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        int count = (int) dataSnapshot.child("Comments").child(KEY).getChildrenCount();

                        comments_count.setText("التعليقات (" + count + ")");
                        rotateLoading.stop();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                holder.BindPlaces(model);
            }

            @NonNull
            @Override
            public commentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.comment_item, parent, false);
                return new commentsViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver()
        {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount)
            {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        layoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1)))
                {
                    recyclerView.scrollToPosition(positionStart);
                }
            }
        });

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class commentsViewHolder extends RecyclerView.ViewHolder
    {
        TextView username,content;
        CircleImageView image;
        MaterialRippleLayout materialRippleLayout;

        commentsViewHolder(View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.username_comment);
            content = itemView.findViewById(R.id.content_comment);
            image = itemView.findViewById(R.id.user_image);
            materialRippleLayout = itemView.findViewById(R.id.edit_mrl);
        }

        void BindPlaces(CommentModel commentModel)
        {
            username.setText(commentModel.getUsername());
            content.setText(commentModel.getContentcomment());

            String id = commentModel.getUserid();

            if (id.equals(getUid()) || getUid().equals("CJ4jidhu82acscpjmd7fUjaT39n2"))
            {
                materialRippleLayout.setVisibility(View.VISIBLE);
            } else
                {
                    materialRippleLayout.setVisibility(View.GONE);
                }

            Picasso.get()
                    .load(commentModel.getImage_url())
                    .placeholder(R.drawable.ic_user1)
                    .error(R.drawable.ic_user1)
                    .into(image);
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();

        if (firebaseRecyclerAdapter != null)
        {
            firebaseRecyclerAdapter.stopListening();
        }
    }

    public static String getUid()
    {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void returnData (String uid)
    {
        databaseReference.child("Users").child(uid).addListenerForSingleValueEvent(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        // Get user value
                        UserModel userModel = dataSnapshot.getValue(UserModel.class);

                        if (userModel != null)
                        {
                            name = userModel.getUserName();
                            imageurl = userModel.getImageUrl();
                        } else
                            {
                                Toast.makeText(getApplicationContext(), "Can't Get User ..", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed()
    {
        if (admin == 1)
        {
            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(intent);
        } else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
