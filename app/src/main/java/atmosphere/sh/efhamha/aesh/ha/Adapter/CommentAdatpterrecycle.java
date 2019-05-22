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

import atmosphere.sh.efhamha.aesh.ha.Models.UsercommentModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class CommentAdatpterrecycle extends RecyclerView.Adapter<CommentAdatpterrecycle.ViewHolde> {

    private ArrayList<UsercommentModel> usercommentModels;
    private Context context;

    private OnItemClickListener mListener;

    public interface OnItemClickListener {

        void  open_content(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ViewHolde extends RecyclerView.ViewHolder {


        TextView username, commentcontent;
        ImageView userimage;

        public ViewHolde(@NonNull View itemView, final OnItemClickListener Listener) {
            super(itemView);

            userimage = itemView.findViewById(R.id.user_image);
            username = itemView.findViewById(R.id.username_comment);
            commentcontent = itemView.findViewById(R.id.content_comment);
        }
    }


    public CommentAdatpterrecycle(Context applicationContext,ArrayList<UsercommentModel> modellist) {
        usercommentModels = modellist;
        context=applicationContext;
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_item, viewGroup, false);
        ViewHolde evh = new ViewHolde(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolde viewHolde, int i) {
        UsercommentModel currentItem = usercommentModels.get(i);

        viewHolde.username.setText(currentItem.getUsername());
        viewHolde.commentcontent.setText(currentItem.getContentcomment());


        //Add image of user in library Glide
        if (currentItem.getImage_url() != null) {

            Glide.with(context).load(currentItem.getImage_url()).into(viewHolde.userimage);

        } else {
            // make sure Glide doesn't load anything into this view until told otherwise

            // remove the placeholder (optional); read comments below
           // viewHolde.userimage.setImageDrawable(null);
        }

    }

    @Override
    public int getItemCount() {
        return usercommentModels.size();
    }
}
