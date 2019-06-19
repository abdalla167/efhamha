package atmosphere.sh.efhamha.aesh.ha.AdminApp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.MainActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.VideoActivity;
import atmosphere.sh.efhamha.aesh.ha.Fragments.ArticlesFragment;
import atmosphere.sh.efhamha.aesh.ha.Helpers.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.Helpers.ViewPagerAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class AdminFragment extends Fragment {
    View view;

    RecyclerView recyclerView;
    FloatingActionButton signout;

    RotateLoading rotateLoading;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerAdapter<ArticleModel, articlesViewHolder> firebaseRecyclerAdapter;

    FirebaseUser user;

    //Firebase Database
    DatabaseReference messageReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.admin_fragment, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerview);
        SpeedDialView speedDialView = view.findViewById(R.id.adminAction);
        speedDialView.inflate(R.menu.menu_admin_actions);

        signout = view.findViewById(R.id.signout_fab);

        rotateLoading = view.findViewById(R.id.rotateloading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        //Message
        messageReference = FirebaseDatabase.getInstance().getReference().child("messages");

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        displayArticles();

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        speedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem speedDialActionItem) {
                switch (speedDialActionItem.getId()) {
                    case R.id.add_article:
                        Intent intent = new Intent(getContext(), AddArticleActivity.class);
                        startActivity(intent);
                        return false; // true to keep the Speed Dial open

                    case R.id.add_message:
                        saveMessage();
                        return false;

                    default:
                        return false;
                }
            }
        });
    }

    private void saveMessage() {

        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.message, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText messageET = (EditText) promptsView.findViewById(R.id.messageET);
        final EditText titleET = (EditText) promptsView.findViewById(R.id.titleET);
        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("تم", null)
                .setNegativeButton("الغاء", null);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;

                if ((InputValidator.messageValidation(getContext(), messageET, titleET))) {
                    String message = messageET.getText().toString();
                    String title = titleET.getText().toString();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("from", user.getUid());
                    hashMap.put("title", title);
                    hashMap.put("content", message);
                    messageReference.child(user.getUid()).push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_SHORT).show();
                        }
                    });
                    wantToCloseDialog = true;
                }
                if (wantToCloseDialog) {
                    alertDialog.dismiss();
                }
            }
        });

    }

    private void displayArticles() {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Articles")
                .limitToLast(50);

        FirebaseRecyclerOptions<ArticleModel> options =
                new FirebaseRecyclerOptions.Builder<ArticleModel>()
                        .setQuery(query, ArticleModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArticleModel, articlesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull articlesViewHolder holder, int position, @NonNull final ArticleModel model) {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.BindPlaces(model, getContext());

                holder.setlikesstatus(key, getContext(), user);
                holder.setcommentstatus(key, getContext(), user);
                holder.setviewsstatus(getContext(), key, user);

                holder.article_mrl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (user != null) {
                            Intent intent = new Intent(getContext(), ArticleActivity.class);
                            intent.putExtra("admin", 1);
                            intent.putExtra("ar", model);
                            intent.putExtra("ar2", key);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @NonNull
            @Override
            public articlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.article_item, parent, false);
                return new articlesViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        layoutManager.findLastCompletelyVisibleItemPosition();

                /*
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1)))
                {
                    recyclerView.scrollToPosition(positionStart);
                }
                */
            }
        });

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class articlesViewHolder extends RecyclerView.ViewHolder {
        TextView title, time;
        TextView content, source, numlikes, numviews, numcomments, caption, page_numper;
        ImageView imageArchi, likeimage, comment_img, view_img;
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
            caption = itemView.findViewById(R.id.showcaption_item);
            page_numper = itemView.findViewById(R.id.page_num);
//            article_slider =(SliderLayout)itemView.findViewById(R.id.article_image_slider);

            viewPager = itemView.findViewById(R.id.article_image_slider);


            databaseReference = FirebaseDatabase.getInstance().getReference();
        }

        void BindPlaces(final ArticleModel articleModel, final Context context) {

            if (articleModel.getType() == 1) {


                if (articleModel.getImage_url() != null) {

                    viewPager.setVisibility(View.VISIBLE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context, articleModel.getImage_url());
                    viewPager.setAdapter(adapter);
                    imageArchi.setVisibility(View.GONE);
                    page_numper.setText(1 + " / " + articleModel.getImage_url().size());
                }
            } else if (articleModel.getType() == 2) {
                caption.setVisibility(View.GONE);
                page_numper.setVisibility(View.GONE);
                viewPager.setVisibility(View.GONE);
                imageArchi.setVisibility(View.VISIBLE);
                imageArchi.setImageResource(R.drawable.play_button);
                imageArchi.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageArchi.setBackgroundColor(ContextCompat.getColor(context, R.color.darker_grey));
            }


            imageArchi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("url", articleModel.getVideoURL());
                    context.startActivity(intent);
                }
            });

            String sourc = source.getText().toString() + " ";
            source.setText(sourc + articleModel.getSource());
            content.setText(articleModel.getContent());

            if (articleModel.getCaption() != null) {
                caption.setText(articleModel.getCaption().get(0));
            }
            viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {


                }

                @Override
                public void onPageSelected(int i) {


                    if (i < articleModel.getCaption().size()) {
                        Toast.makeText(context, articleModel.getCaption().get(0), Toast.LENGTH_SHORT).show();
                        caption.setText(articleModel.getCaption().get(i));
                    }

                    else
                        caption.setVisibility(View.VISIBLE);
                    int num = i + 1;
                    page_numper.setText(num + " / " + articleModel.getImage_url().size());
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });


            title.setText(articleModel.getTitle());
            String time_txt = " " + articleModel.getArticle_day() + "  " + articleModel.getArticle_time() + " " + articleModel.getArticle_month() + " " + articleModel.getArticle_year();
            time.setText(time_txt);
            // caption.setText(articleModel.getCaption());
            source.setText("   " + articleModel.getSource() + "  ");
            content.setText(articleModel.getContent());
        }

        void setlikesstatus(final String articlekey, final Context context, final FirebaseUser user) {
            databaseReference.child("Likes").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (user != null) {
                        if (dataSnapshot.child(articlekey).hasChild(user.getUid())) {
                            countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();
                            numlikes.setText(countlieks + "");
                            numlikes.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            likeimage.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else {
                            countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();

                            if (countlieks > 0) {
                                numlikes.setText(countlieks + "");
                                numlikes.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                                likeimage.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                            } else {
                                numlikes.setText(countlieks + "");
                                numlikes.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                                likeimage.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                            }
                        }
                    } else {
                        countlieks = (int) dataSnapshot.child(articlekey).getChildrenCount();
                        numlikes.setText(countlieks + "");
                        numlikes.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                        likeimage.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void setcommentstatus(final String articlekey, final Context context, final FirebaseUser user) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (user != null) {
                        int count = (int) dataSnapshot.child("Comments").child(articlekey).getChildrenCount();

                        if (count > 0) {
                            numcomments.setText(count + "");
                            numcomments.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            comment_img.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else {
                            numcomments.setText("0");
                            numcomments.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            comment_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                    } else {
                        int count = (int) dataSnapshot.child("Comments").child(articlekey).getChildrenCount();

                        numcomments.setText(count + "");
                        numcomments.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                        comment_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        void setviewsstatus(final Context context, final String articlekey, final FirebaseUser user) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (user != null) {
                        int count = (int) dataSnapshot.child("Views").child(articlekey).getChildrenCount();

                        if (count > 0) {
                            numviews.setText(count + "");
                            numviews.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                            view_img.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
                        } else {
                            numviews.setText("0");
                            numviews.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                            view_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                        }
                    } else {
                        int count = (int) dataSnapshot.child("Views").child(articlekey).getChildrenCount();

                        numviews.setText(count + "");
                        numviews.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                        view_img.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }


    }


    @Override
    public void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (firebaseRecyclerAdapter != null) {
            firebaseRecyclerAdapter.stopListening();
        }
    }
}
