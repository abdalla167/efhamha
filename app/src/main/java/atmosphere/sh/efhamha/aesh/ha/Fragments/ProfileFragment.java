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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

import atmosphere.sh.efhamha.aesh.ha.Activties.MainActivity;
import atmosphere.sh.efhamha.aesh.ha.Activties.SignInActivity;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ProfileFragment extends Fragment
{
    private View view;
    private Button signout_btn;
    private ImageButton edit_btn;
    private TextView emailTV,usernameTV;
    private ImageView circleImageView;

    //Firebase
    private FirebaseAuth mAuth;

    //Firebase Database
    FirebaseDatabase database;
    DatabaseReference ref;

    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.profile_fragment, container, false);
        signout_btn = view.findViewById(R.id.signout_btn);
        emailTV = view.findViewById(R.id.email_txt);
        usernameTV = view.findViewById(R.id.username_txt);
        edit_btn = view.findViewById(R.id.edit_profile_btn);
        circleImageView = view.findViewById(R.id.profile_image);

        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        ref.keepSynced(true);

        loadUserData();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);


        signout_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signOut();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getContext(), "انت دوست تعديل", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Get User Data
    public void loadUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<? extends UserInfo> infos = user.getProviderData();
        String userId = user.getUid();

        int count = 0;
        String providerId;
        for (UserInfo ui : infos) {
            providerId = ui.getProviderId();
            if (count == 1) {
                if (providerId.equals(GoogleAuthProvider.PROVIDER_ID) || providerId.equals(FacebookAuthProvider.PROVIDER_ID)) {
                    usernameTV.setText(user.getDisplayName());
                    emailTV.setText(user.getEmail());
                    Picasso.get()
                            .load(user.getPhotoUrl())
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(circleImageView);
                } else {

                    ref.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String userName = dataSnapshot.child("userName").getValue(String.class);
                            String email = dataSnapshot.child("email").getValue(String.class);
                            String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);
                            usernameTV.setText(userName);
                            emailTV.setText(email);
                            Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.drawable.user)
                                    .error(R.drawable.user)
                                    .into(circleImageView);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
            count++;
        }
    }
    // Sign Out Method
    public void signOut()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<? extends UserInfo> infos = user.getProviderData();

        int count = 0;
        String providerId;
        for (UserInfo ui : infos) {
            providerId = ui.getProviderId();
            if (count > 0) {
                if (providerId.equals(GoogleAuthProvider.PROVIDER_ID)) {
                    GoogleSignInClient mGoogleSignInClient;
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(getString(R.string.default_web_client_id))
                            .requestEmail()
                            .build();
                    mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
                    mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                            new OnCompleteListener<Void>() { //signout google
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getInstance().signOut(); //signout firebase
                                        updateUI();
                                    } else
                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if (providerId.equals(FacebookAuthProvider.PROVIDER_ID)) {
                    LoginManager.getInstance().logOut();
                    mAuth.getInstance().signOut(); //signout firebase
                    updateUI();
                } else {
                    mAuth.getInstance().signOut();
                    updateUI();
                }
            }
            count++;
        }
    }
    public void updateUI()
    {
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
