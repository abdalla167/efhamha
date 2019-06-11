package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;


import java.util.ArrayList;
import java.util.HashMap;

import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.VideoActivity;
import atmosphere.sh.efhamha.aesh.ha.Helpers.ViewPagerAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticlesFragment extends Fragment
{
    View view;
    RecyclerView recyclerView;
    RotateLoading rotateLoading;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager layoutManager;
    EditText search_auothar;
    FirebaseRecyclerAdapter<ArticleModel, articlesViewHolder> firebaseRecyclerAdapter;

    FirebaseUser user;


    ArrayList<ArticleModel>articleModels=new ArrayList<>();

    Boolean likechecker = false;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.article_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);




        displayArticles("");



        search_auothar = view.findViewById(R.id.search_txt);
        search_auothar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                firebaseRecyclerAdapter=null;
                               displayArticles(s.toString());



            }
        });



    }



    private void displayArticles(final String writer)
    {
        Query query;



        if (writer.equals("")||writer.equals(null)) {
            query = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Articles");
                   // .limitToLast(50);

        }
        else
        {
            query=FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Articles")
                    .child("Source")
                    .startAt(writer).endAt(writer + "\uf8ff");
                    //.limitToLast(50);
            Toast.makeText(getActivity(), "33333333333", Toast.LENGTH_SHORT).show();
        }

        FirebaseRecyclerOptions<ArticleModel> options =
                new FirebaseRecyclerOptions.Builder<ArticleModel>()
                        .setQuery(query, ArticleModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArticleModel, articlesViewHolder>(options)
        {




            @Override
            protected void onBindViewHolder(@NonNull articlesViewHolder holder, int position, @NonNull final ArticleModel model)
            {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                if (model.getSource().toLowerCase().contains(writer.toLowerCase())) {
                  holder.BindPlaces(model, getContext());
                }
                else if (writer.equals(""))
                {
                    holder.BindPlaces(model, getContext());

                }
                holder.setlikesstatus(key,getContext(),user);
                holder.setcommentstatus(key,getContext(),user);
                holder.setviewsstatus(getContext(),key,user);

                holder.article_mrl.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (user != null)
                        {
                            Intent intent = new Intent(getContext(), ArticleActivity.class);
                            intent.putExtra("ar", model);
                            intent.putExtra("ar2", key);
                            startActivity(intent);

                            databaseReference.child("Views").child(key).child(user.getUid()).setValue(user.getUid());
                        } else
                        {
                            Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                holder.imagecomment.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (user != null)
                        {
                            Intent intent = new Intent(getContext(), ArticleActivity.class);
                            intent.putExtra("ar", model);
                            intent.putExtra("ar2", key);
                            startActivity(intent);

                            databaseReference.child("Views").child(key).child(user.getUid()).setValue(user.getUid());
                        } else
                            {
                                Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                            }
                    }
                });

                holder.imagelike.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (user != null)
                        {
                            likechecker = true;

                            databaseReference.child("Likes").addValueEventListener(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    if (likechecker.equals(true))
                                    {
                                        if (dataSnapshot.child(key).hasChild(user.getUid()))
                                        {
                                            databaseReference.child("Likes").child(key).child(user.getUid()).removeValue();
                                            likechecker = false;
                                        } else
                                        {
                                            databaseReference.child("Likes").child(key).child(user.getUid()).setValue(true);
                                            likechecker = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                {
                                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else
                            {
                                Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                            }
                    }
                });
            }

            @NonNull
            @Override
            public articlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);
                return new articlesViewHolder(view);
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

    public static class articlesViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        TextView content, source, numlikes, numviews, numcomments;
        ImageView imageArchi , likeimage, comment_img, view_img;
        MaterialRippleLayout imagelike, imagecomment, article_mrl;
        SliderLayout article_slider;

        ViewPager viewPager;

        int countlieks;
        DatabaseReference databaseReference;

        articlesViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            time = itemView.findViewById(R.id.article_time);
            content = itemView.findViewById(R.id.article_content);
            source = itemView.findViewById(R.id.article_by);
            numlikes = itemView.findViewById(R.id.numlikes);
            numviews = itemView.findViewById(R.id.numviews);
            numcomments = itemView.findViewById(R.id.numcomments);
            imageArchi = itemView.findViewById(R.id.article_image);
            imagelike = itemView.findViewById(R.id.like_btn);
            imagecomment = itemView.findViewById(R.id.comment_btn);
            article_mrl = itemView.findViewById(R.id.article_mrl);
            likeimage = itemView.findViewById(R.id.like);
            comment_img = itemView.findViewById(R.id.comment);
            view_img = itemView.findViewById(R.id.view);
//            article_slider =(SliderLayout)itemView.findViewById(R.id.article_image_slider);

             viewPager =itemView .findViewById(R.id.article_image_slider);




            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        void BindPlaces(final ArticleModel articleModel, final Context context) {
            if (articleModel.getType() == 1) {


                if (articleModel.getImage_url()!=null) {
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context, articleModel.getImage_url());
                    viewPager.setAdapter(adapter);
                }




                else if (articleModel.getType() == 2)

                {
                    imageArchi.setVisibility(View.VISIBLE);
                }

                    imageArchi.setImageResource(R.drawable.ic_youtube);
                    imageArchi.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageArchi.setBackgroundColor(ContextCompat.getColor(context, R.color.darker_grey));

                    imageArchi.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            Intent intent = new Intent(context, VideoActivity.class);
                            intent.putExtra("url", articleModel.getImage_url());
                            context.startActivity(intent);
                        }
                    });

                }


               title.setText(articleModel.getTitle());
                String time_txt = " " + articleModel.getArticle_day()+"  "+articleModel.getArticle_time() + " " + articleModel.getArticle_month() +  " " + articleModel.getArticle_year() ;
                time.setText(time_txt);
                source.setText("   "+articleModel.getSource()+"  ");
                content.setText(articleModel.getContent());
            }


        void setlikesstatus(final String articlekey, final Context context, final FirebaseUser user)
        {
            databaseReference.child("Likes").addValueEventListener(new ValueEventListener()
            {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (user != null)
                    {
                        if (dataSnapshot.child(articlekey).hasChild(user.getUid()))
                        {
                            countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();
                            numlikes.setText(countlieks + "");
                            numlikes.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            likeimage.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else
                        {
                            countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();
                            numlikes.setText(countlieks + "");
                            numlikes.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            likeimage.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                    } else
                        {
                            countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();
                            numlikes.setText(countlieks + "");
                            numlikes.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            likeimage.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void setcommentstatus(final String articlekey, final Context context, final FirebaseUser user)
        {
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (user != null)
                    {
                        int count = (int) dataSnapshot.child("Comments").child(articlekey).getChildrenCount();

                        if (count > 0)
                        {
                            numcomments.setText(count + "");
                            numcomments.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            comment_img.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else
                        {
                            numcomments.setText("0");
                            numcomments.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            comment_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                    } else
                        {
                            int count = (int) dataSnapshot.child("Comments").child(articlekey).getChildrenCount();

                            numcomments.setText(count + "");
                            numcomments.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            comment_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void setviewsstatus(final Context context, final String articlekey, final FirebaseUser user)
        {
            databaseReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    if (user != null)
                    {
                        int count = (int) dataSnapshot.child("Views").child(articlekey).getChildrenCount();

                        if (count > 0)
                        {
                            numviews.setText(count + "");
                            numviews.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            view_img.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else
                        {
                            numviews.setText("0");
                            numviews.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            view_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                    } else
                        {
                            int count = (int) dataSnapshot.child("Views").child(articlekey).getChildrenCount();

                            numviews.setText(count + "");
                            numviews.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            view_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        if (firebaseRecyclerAdapter != null)
            firebaseRecyclerAdapter.startListening();

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
}
