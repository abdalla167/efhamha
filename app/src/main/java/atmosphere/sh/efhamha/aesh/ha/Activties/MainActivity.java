package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.Fragments.ArticlesFragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.InfoFragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.NotificationsFragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.ProfileFragment;
import atmosphere.sh.efhamha.aesh.ha.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity
{
    BottomNavigationView navigation;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);

        fragmentManager = getSupportFragmentManager();

        Fragment articlesFragment = new ArticlesFragment();
        loadFragment(articlesFragment);

        addBadgeView();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.articles:
                        Fragment articlesFragment = new ArticlesFragment();
                        loadFragment(articlesFragment);
                        return true;
                    case R.id.notification:
                        Fragment notificationsFragment = new NotificationsFragment();
                        loadFragment(notificationsFragment);
                        return true;
                    case R.id.info:
                        Fragment infoFragment = new InfoFragment();
                        loadFragment(infoFragment);
                        return true;
                    case R.id.profile:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null)
                        {
                            Fragment profileFragment = new ProfileFragment();
                            loadFragment(profileFragment);
                        }
                        else
                            {
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                startActivity(intent);
                            }
                        return true;
                }
                return false;
            }
        });
    }

    private void addBadgeView()
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);

        View notificationBadge = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_notification_badge, menuView, false);

        final ImageView imageView = notificationBadge.findViewById(R.id.badge);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                int count = (int) dataSnapshot.child("Notifications").getChildrenCount();

                if (count > 9)
                {
                    imageView.setImageResource(R.drawable.ic_nine);
                    return;
                }

                switch (count)
                {
                    case 0:
                        imageView.setImageResource(R.drawable.notification_badge);
                        break;
                    case 1:
                        imageView.setImageResource(R.drawable.ic_one3);
                        break;
                    case 2:
                        imageView.setImageResource(R.drawable.ic_two1);
                        break;
                    case 3:
                        imageView.setImageResource(R.drawable.ic_three2);
                        break;
                    case 4:
                        imageView.setImageResource(R.drawable.ic_four);
                        break;
                    case 5:
                        imageView.setImageResource(R.drawable.ic_five);
                        break;
                    case 6:
                        imageView.setImageResource(R.drawable.ic_six);
                        break;
                    case 7:
                        imageView.setImageResource(R.drawable.ic_seven);
                        break;
                    case 8:
                        imageView.setImageResource(R.drawable.ic_eight);
                        break;
                    case 9:
                        imageView.setImageResource(R.drawable.ic_nine);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        itemView.addView(notificationBadge);
        notificationBadge.setVisibility(VISIBLE );
    }

    public void loadFragment(Fragment fragment)
    {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);

        getSupportFragmentManager().popBackStack();
        // Commit the transaction
        fragmentTransaction.commit();
    }

    private long exitTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000)
        {
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAffinity();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed()
    {
        doExitApp();
    }
}
