package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Calendar;

import atmosphere.sh.efhamha.aesh.ha.Adapter.NotificationAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.NotificationModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationsFragment extends Fragment
{

    /*
    RecyclerView recyclerView;
    RotateLoading rotateLoading;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager layoutManager;
   // FirebaseRecyclerAdapter<NotificationModel, notificationsViewHolder> firebaseRecyclerAdapter;
   */

    ////
    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return ArticlesFragment.view_notification;
    }


    /*
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);
        rotateLoading = view.findViewById(R.id.rotateloading);
        rotateLoading.start();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        displayNotifications();
    }

    private void displayNotifications()
    {
        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Notifications")
                .limitToLast(50);

        FirebaseRecyclerOptions<NotificationModel> options =
                new FirebaseRecyclerOptions.Builder<NotificationModel>()
                        .setQuery(query, NotificationModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NotificationModel, notificationsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final notificationsViewHolder holder, int position, @NonNull final NotificationModel model)
            {
                rotateLoading.stop();

                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                holder.mrl.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        databaseReference.child("Notifications").child(key).removeValue();
                    }
                });
            }

            @NonNull
            @Override
            public notificationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
            {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.notification_item, parent, false);
                return new notificationsViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        rotateLoading.stop();
    }

    public static class notificationsViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,time;
        MaterialRippleLayout mrl;
        CircleImageView image;

        notificationsViewHolder(View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            time = itemView.findViewById(R.id.article_time);
            image = itemView.findViewById(R.id.article_image);
            mrl = itemView.findViewById(R.id.notification_mrl);
        }
        void BindPlaces(final NotificationModel notificationModel)
        {
            title.setText(notificationModel.getArticle_title());
            String time_txt = notificationModel.getArticle_day() + " " + notificationModel.getArticle_month() + " " + notificationModel.getArticle_year();
            time.setText(time_txt);
            Picasso.get()
                    .load(notificationModel.getArticle_image())
                    .placeholder(R.drawable.ic_darkgrey)
                    .error(R.drawable.ic_darkgrey)
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
    }*/
}
