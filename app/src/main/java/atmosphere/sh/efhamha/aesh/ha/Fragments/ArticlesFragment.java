package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;

import com.daimajia.slider.library.SliderLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.victor.loading.rotate.RotateLoading;


import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;


import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.MainActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.VideoActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.Writers;
import atmosphere.sh.efhamha.aesh.ha.Helpers.ViewPagerAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

import static android.view.View.GONE;


public class ArticlesFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    RotateLoading rotateLoading;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    LinearLayoutManager layoutManager;
    EditText search_auothar;
    FirebaseUser user;
    ArrayList<ArticleModel> articleModels;
    Boolean likechecker = false;
    ArticleAdapter articleAdapter;
    TextView showwriter;


    public  static  String writer_name = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.article_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        showwriter=view.findViewById(R.id.show_writers_btn);
        showwriter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Writers.class);
                startActivity(i);
            }
        });
        user = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = view.findViewById(R.id.recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

      /*  layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
*/
        loadarticledata();

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

                filterbywriter(s.toString());


            }
        });


        if (!writer_name.equals(""))
        {
            search_auothar.setVisibility(GONE);
            showwriter.setVisibility(GONE);
        }

        if (!MainActivity.catename.equals(""))
        {
            showwriter.setVisibility(GONE);
        }


    }

    private void filterbywriter(String text) {
        ArrayList<ArticleModel> filteredList = new ArrayList<>();

        for (ArticleModel item : articleModels) {
            if (item.getSource().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        articleAdapter.filterList(filteredList);
    }

    private void filterbycategory(String text) {
        ArrayList<ArticleModel> filteredList = new ArrayList<>();


        for (ArticleModel item : articleModels) {
            if (item.getCategory() != null) {
                if (item.getCategory().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }

        if (filteredList.size() == 0) {
            Toast.makeText(getActivity(), "لا يوجد مقالات من هذا النوع", Toast.LENGTH_SHORT).show();
        }


        articleAdapter.filterList(filteredList);
    }


    private void loadarticledata() {

        articleModels = new ArrayList<>();
        databaseReference.child("Articles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    articleModels.add(dataSnapshot1.getValue(ArticleModel.class));
                }

                if (articleModels != null) {

                    Collections.reverse(articleModels);

                    if (articleAdapter == null) {
                        articleAdapter = new ArticleAdapter(articleModels, getActivity());
                        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                        recyclerView.setLayoutManager(manager);
                        recyclerView.setAdapter(articleAdapter);
                    } else
                        articleAdapter.notifyDataSetChanged();

                    //hana b3ml filter le category
                    if (articleAdapter != null) {

                        filterbycategory(MainActivity.catename);

                        if (!writer_name.equals(""))
                            filterbywriter(writer_name);


                    }



                } else
                    Toast.makeText(getActivity(), "No Data to show", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.AricleViewHolder> {

        private ArrayList<ArticleModel> articleList;
        private ArrayList<ArticleModel> articleListfillter;
        private Context context;

        class AricleViewHolder extends RecyclerView.ViewHolder {
            TextView title, time;
            TextView content, source, numlikes, numviews, numcomments, caption, page_numper;
            ImageView imageArchi, likeimage, comment_img, view_img;
            MaterialRippleLayout imagelike, imagecomment, article_mrl;
            SliderLayout article_slider;

            ViewPager viewPager;

            int countlieks;
            DatabaseReference databaseReference;

            AricleViewHolder(View itemView) {
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
                viewPager = itemView.findViewById(R.id.article_image_slider);
                caption = itemView.findViewById(R.id.showcaption_item);
                databaseReference = FirebaseDatabase.getInstance().getReference();
                page_numper = itemView.findViewById(R.id.page_num);
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
                                numlikes.setText(countlieks + "");
                                numlikes.setTextColor(ContextCompat.getColor(context, R.color.darker_grey));
                                likeimage.setColorFilter(ContextCompat.getColor(context, R.color.darker_grey));
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

        public ArticleAdapter(ArrayList<ArticleModel> articleList1, Context context) {
            this.articleList = articleList1;
            this.articleListfillter = new ArrayList<>(articleList1);
            this.context = context;
        }

        @Override
        public AricleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
            AricleViewHolder evh = new AricleViewHolder(v);
            return evh;
        }

        @Override
        public void onBindViewHolder(final AricleViewHolder holder, int position) {
            final ArticleModel currentItem = articleList.get(position);

            if (currentItem.getType() == 1) {

                if (currentItem.getImage_url() != null) {
                    holder.viewPager.setVisibility(View.VISIBLE);
                    ViewPagerAdapter adapter = new ViewPagerAdapter(context, currentItem.getImage_url());
                    holder.viewPager.setAdapter(adapter);
                    holder.imageArchi.setVisibility(View.GONE);
                    holder.page_numper.setVisibility(View.VISIBLE);
                    holder.page_numper.setText(1 + " / " + currentItem.getImage_url().size());

                }
            } else if (currentItem.getType() == 2) {
                holder.caption.setVisibility(View.GONE);
                holder.viewPager.setVisibility(View.GONE);
                holder.page_numper.setVisibility(View.GONE);
                holder.imageArchi.setVisibility(View.VISIBLE);
                holder.imageArchi.setImageResource(R.drawable.play_button);
                holder.imageArchi.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.imageArchi.setBackgroundColor(ContextCompat.getColor(context, R.color.darker_grey));
            }


            holder.imageArchi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, VideoActivity.class);
                    intent.putExtra("url", currentItem.getVideoURL());
                    context.startActivity(intent);
                }
            });


            holder.title.setText(currentItem.getTitle());
            String time_txt = currentItem.getArticle_time() + " " + currentItem.getArticle_day() + " " + currentItem.getArticle_month() + " " + currentItem.getArticle_year() + " ";
            holder.time.setText(time_txt);


            holder.source.setText("كتب بواسطة: "+currentItem.getSource());

            holder.content.setText(currentItem.getContent());

            if (currentItem.getCaption() != null) {
                holder.caption.setText(currentItem.getCaption().get(0));
            }
            holder.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int i1) {


                }

                @Override
                public void onPageSelected(int i) {


                    if (currentItem.getCaption() == null) {
                        holder.caption.setVisibility(GONE);

                    } else {
                        if (i < currentItem.getCaption().size()) {
                            holder.caption.setText(currentItem.getCaption().get(i));
                        } else
                            holder.caption.setVisibility(View.GONE);
                    }

                    int num = i + 1;
                    holder.page_numper.setText(num + " / " + currentItem.getImage_url().size());
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });


            final String key = currentItem.getAricle_id();
            holder.setlikesstatus(key, context, user);
            holder.setcommentstatus(key, context, user);
            holder.setviewsstatus(context, key, user);

            holder.article_mrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user != null) {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("ar", currentItem);
                        intent.putExtra("ar2", key);
                        startActivity(intent);

                        databaseReference.child("Views").child(key).child(user.getUid()).setValue(user.getUid());
                    } else {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("ar", currentItem);
                        intent.putExtra("ar2", key);
                        startActivity(intent);
                        String ID = databaseReference.push().getKey();
                        databaseReference.child("Views").child(key).child(ID).setValue(ID);

                    }
                }
            });

            holder.imagecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user != null) {
                        Intent intent = new Intent(getContext(), ArticleActivity.class);
                        intent.putExtra("ar", currentItem);
                        intent.putExtra("ar2", key);
                        startActivity(intent);

                        databaseReference.child("Views").child(key).child(user.getUid()).setValue(user.getUid());
                    } else {
                        Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.imagelike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (user != null) {
                        likechecker = true;

                        databaseReference.child("Likes").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (likechecker.equals(true)) {
                                    if (dataSnapshot.child(key).hasChild(user.getUid())) {
                                        databaseReference.child("Likes").child(key).child(user.getUid()).removeValue();
                                        likechecker = false;
                                    } else {
                                        databaseReference.child("Likes").child(key).child(user.getUid()).setValue(true);
                                        likechecker = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getContext(), "لو سمحت سجل دخولك الأول", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return articleList.size();
        }

        void filterList(ArrayList<ArticleModel> filteredList) {
            articleList = filteredList;
            notifyDataSetChanged();
        }

    }
}