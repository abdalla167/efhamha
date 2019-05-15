package atmosphere.sh.efhamha.aesh.ha.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import atmosphere.sh.efhamha.aesh.ha.Models.UsercommentModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class CommentAdapter extends ArrayAdapter {

    ArrayList<UsercommentModel> usercomments;
    Context context;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull ArrayList objects) {
        super(context, resource, objects);
        this.usercomments = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.comment_item, parent, false);


        TextView tx1 = v.findViewById(R.id.username);
        TextView tx2 = v.findViewById(R.id.content_comment);
        ImageView im = v.findViewById(R.id.user_image);




        tx1.setText(usercomments.get(position).getUsername());
        tx2.setText(usercomments.get(position).getContentcomment());

        if (usercomments.get(position).getImage_url()!=null)
        {
            Glide.with(context).load(usercomments.get(position).getImage_url()).into(im);
        }

        return v;
    }
}