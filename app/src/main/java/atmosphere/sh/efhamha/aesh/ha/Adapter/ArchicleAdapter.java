package atmosphere.sh.efhamha.aesh.ha.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArchicleAdapter extends RecyclerView.Adapter<ArchicleAdapter.ViewHolde> {

    private ArrayList<ArticleModel> articleModel_list;
    private Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void open_content(int position);

        void like_article(int position , ImageView like_btn);

        void comment_article(int position);

        void share_article(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ViewHolde extends RecyclerView.ViewHolder {

        TextView title;
        TextView content, source, numlikes, numviews, numcomments, numshare;
        ImageView imageArchi ,likeimage;
        MaterialRippleLayout imagelike, imagecomment, imageshare, article_mrl;


        public ViewHolde(@NonNull View itemView, final OnItemClickListener Listener) {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            content = itemView.findViewById(R.id.article_content);
            source = itemView.findViewById(R.id.article_by);
            numlikes = itemView.findViewById(R.id.numlikes);
            numviews = itemView.findViewById(R.id.numviews);
            numcomments = itemView.findViewById(R.id.numcomments);
            numshare = itemView.findViewById(R.id.numshare);
            imageArchi = itemView.findViewById(R.id.article_image);
            imagelike = itemView.findViewById(R.id.like_btn);
            imagecomment = itemView.findViewById(R.id.comment_btn);
            imageshare = itemView.findViewById(R.id.share_btn);
            imageArchi = itemView.findViewById(R.id.article_image);
            article_mrl = itemView.findViewById(R.id.article_mrl);
            likeimage=itemView.findViewById(R.id.like);

            article_mrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.open_content(position);
                        }
                    }
                }
            });

            imagelike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.like_article(position,likeimage);
                        }
                    }
                }
            });

            imagecomment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.comment_article(position);
                        }
                    }
                }
            });

            imageshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            Listener.share_article(position);
                        }
                    }
                }
            });
        }
    }


    public ArchicleAdapter(Context applicationContext, ArrayList<ArticleModel> modellist) {
        articleModel_list = modellist;
        context = applicationContext;
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.article_item, viewGroup, false);
        ViewHolde evh = new ViewHolde(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolde viewHolde, int i) {
        ArticleModel currentItem = articleModel_list.get(i);

        viewHolde.title.setText(currentItem.getTitle());
        viewHolde.source.setText(currentItem.getSource());
        viewHolde.content.setText(currentItem.getContent());
        if (currentItem.getUser_likes() == null)
            viewHolde.numlikes.setText(String.valueOf(0));
        else
            viewHolde.numlikes.setText(String.valueOf(currentItem.getUser_likes().size()));

        if (currentItem.getUser_comments() == null)
            viewHolde.numcomments.setText(String.valueOf(0));
        else {
            int c = 0;

            for (final String name : currentItem.getUser_comments().keySet()) {
                // search  for value
                c += currentItem.getUser_comments().get(name).size();
                viewHolde.numcomments.setText(String.valueOf(c));
            }
        }
        if (currentItem.getUser_share() == null)
            viewHolde.numshare.setText(String.valueOf(0));
        else

            viewHolde.numshare.setText(String.valueOf(currentItem.getUser_share().size()));

        if (currentItem.getUser_view() == null)
            viewHolde.numviews.setText(String.valueOf(0));
        else
            viewHolde.numviews.setText(String.valueOf(currentItem.getUser_view().size()));


        //Add image of architichles in library Glide
        if (currentItem.getImage_url() != null) {

            Glide.with(context).load(currentItem.getImage_url()).into(viewHolde.imageArchi);

        } else {
            // make sure Glide doesn't load anything into this view until told otherwise

            // remove the placeholder (optional); read comments below
            viewHolde.imageArchi.setImageDrawable(null);
        }

        // set likes of this user


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        viewHolde.likeimage.setTag(0);
        if (user !=null)
        {
            if (currentItem.getUser_likes()!=null) {
                if (currentItem.getUser_likes().contains(user.getUid())) {
                    {
                        viewHolde.likeimage.setImageResource(R.drawable.liked);
                        viewHolde.likeimage.setTag(R.drawable.liked);
                    }
                }

                else
                    {

                        viewHolde.likeimage.setImageResource(R.drawable.not_like);
                        viewHolde.likeimage.setTag(0);
                }

                currentItem.getUser_likes().clear();

            }
        }

    }

    @Override
    public int getItemCount() {

        return articleModel_list.size();
    }
}
