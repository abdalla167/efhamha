package atmosphere.sh.efhamha.aesh.ha.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import atmosphere.sh.efhamha.aesh.ha.Activties.MainActivity;
import atmosphere.sh.efhamha.aesh.ha.Models.UserModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment
{
    View view;

    Button signout_btn;

    ImageButton edit_btn;

    TextView email,username;
    CircleImageView circleImageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.profile_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        signout_btn = view.findViewById(R.id.signout_btn);
        email = view.findViewById(R.id.email_txt);
        username = view.findViewById(R.id.username_txt);
        edit_btn = view.findViewById(R.id.edit_profile_btn);

        signout_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "Edit Profile Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
