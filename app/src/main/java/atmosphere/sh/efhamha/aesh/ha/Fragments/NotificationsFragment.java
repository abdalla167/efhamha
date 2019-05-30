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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.Calendar;

import atmosphere.sh.efhamha.aesh.ha.Activties.ArticleActivity;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

public class NotificationsFragment extends Fragment
{
    View view;
    RecyclerView recyclerView;
    RotateLoading rotateLoading;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<ArticleModel, notificationsViewHolder> firebaseRecyclerAdapter;

    LinearLayout linearLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.notifications_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview_notifaection);
        rotateLoading = view.findViewById(R.id.rotateloading);
        linearLayout = view.findViewById(R.id.no_notifications_lin);

        rotateLoading.start();
        linearLayout.setVisibility(View.GONE);

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

        FirebaseRecyclerOptions<ArticleModel> options =
                new FirebaseRecyclerOptions.Builder<ArticleModel>()
                        .setQuery(query, ArticleModel.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ArticleModel, notificationsViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final notificationsViewHolder holder, int position, @NonNull final ArticleModel model)
            {
                rotateLoading.stop();

                databaseReference.addValueEventListener(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        int count = (int) dataSnapshot.child("Notifications").getChildrenCount();

                        if (count > 0)
                        {
                            linearLayout.setVisibility(View.GONE );
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

                final String key = getRef(position).getKey();

                holder.BindPlaces(model);

                holder.mrl.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null)
                        {
                            databaseReference.child("Notifications").child(key).removeValue();
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
        linearLayout.setVisibility(View.VISIBLE );
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

        void BindPlaces(final ArticleModel notificationModel)
        {
            title.setText(notificationModel.getTitle());
            String time_txt = notificationModel.getArticle_day() + " " + notificationModel.getArticle_month() + " " + notificationModel.getArticle_year();
            time.setText(time_txt);
            Picasso.get()
                    .load(notificationModel.getImage_url())
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
    }
}
