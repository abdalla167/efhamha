package atmosphere.sh.efhamha.aesh.ha.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArchicleAdapter extends RecyclerView.Adapter<ArchicleAdapter.ViewHolde> {

    private ArrayList<ArticleModel> articleModel_list;
    private Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {

         void  open_content(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ViewHolde extends RecyclerView.ViewHolder {

        TextView title;
        TextView content, source, numlikes, numviews, numcomments, numshare;
        ImageView imageArchi, imagelike, imagecomment, imageshare;


        public ViewHolde(@NonNull View itemView, final OnItemClickListener Listener)
        {
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


            content.setOnClickListener(new View.OnClickListener() {
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


        }
    }


    public ArchicleAdapter(ArrayList<ArticleModel> modellist) {
        articleModel_list = modellist;
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
        viewHolde.numlikes.setText(String.valueOf(currentItem.getUser_likes().size()));
        viewHolde.numcomments.setText(String.valueOf(currentItem.getUser_comments().size()));
        viewHolde.numshare.setText(String.valueOf(currentItem.getUser_share().size()));
        viewHolde.numviews.setText(String.valueOf(currentItem.getUser_view().size()));


        //Add image of architichles in library Glide
        if (currentItem.getImage_url() != null) {

            Glide.with(context).load(currentItem.getImage_url()).into(viewHolde.imageArchi);

        } else {
            // make sure Glide doesn't load anything into this view until told otherwise

            // remove the placeholder (optional); read comments below
            viewHolde.imageArchi.setImageDrawable(null);
        }

    }

    @Override
    public int getItemCount() {
        return articleModel_list.size();
    }
}
