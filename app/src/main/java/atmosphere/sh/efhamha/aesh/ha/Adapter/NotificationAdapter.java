package atmosphere.sh.efhamha.aesh.ha.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import atmosphere.sh.efhamha.aesh.ha.Models.NotificationModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ExampleViewHolder> {
    /////
    private ArrayList<NotificationModel> mExampleList;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void intent_to_articel(int pos, LinearLayout linearLayout);
    }

    public NotificationAdapter() {
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    /////
    public static class ExampleViewHolder extends RecyclerView.ViewHolder {

        TextView title,time;
        MaterialRippleLayout mrl;
        CircleImageView image;
        LinearLayout linearLayout;

        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            /////
            title = itemView.findViewById(R.id.article_title);
            time = itemView.findViewById(R.id.article_time);
            image = itemView.findViewById(R.id.article_image);
            mrl = itemView.findViewById(R.id.notification_mrl);
           linearLayout=itemView.findViewById(R.id.linear_notification);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.intent_to_articel(position, linearLayout);
                        }
                    } }
            });

        }
    }
    public NotificationAdapter(Context applicationContext, ArrayList<NotificationModel> exampleList) {
        mExampleList = exampleList;
        context=applicationContext;
    }

    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item,viewGroup,false);

        return new ExampleViewHolder(v,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder exampleViewHolder,final int i) {
        NotificationModel currentitem = mExampleList.get(i);

       exampleViewHolder. title.setText(currentitem.getArticle_title());

        String time_txt = currentitem.getArticle_day() + " " + currentitem.getArticle_month() + " " + currentitem.getArticle_year();

       exampleViewHolder. time.setText(time_txt);

        Picasso.get()
                .load(currentitem.getArticle_image())
                .placeholder(R.drawable.ic_darkgrey)
                .error(R.drawable.ic_darkgrey)
                .into(exampleViewHolder.image);
    }

    @Override
    public int getItemCount() {

        return mExampleList.size();
    }

}
