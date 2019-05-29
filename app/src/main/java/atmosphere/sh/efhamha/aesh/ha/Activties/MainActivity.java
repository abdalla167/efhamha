package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import atmosphere.sh.efhamha.aesh.ha.Fragments.ArticlesFragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D10Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D1Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D2Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D3Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D4Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D5Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D6Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D7Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D8Fragment;
import atmosphere.sh.efhamha.aesh.ha.Fragments.D9Fragment;
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

    DrawerLayout mDrawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        drawer();

        fragmentManager = getSupportFragmentManager();

        Fragment articlesFragment = new ArticlesFragment();
        loadFragment(articlesFragment);
        getSupportActionBar().setTitle("أجدد المقالات");

        addBadgeView();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.articles:
                        Fragment articlesFragment1 = new ArticlesFragment();
                        loadFragment(articlesFragment1);
                        getSupportActionBar().setTitle("أجدد المقالات");
                        return true;
                    case R.id.notification:
                        Fragment notificationsFragment = new NotificationsFragment();
                        loadFragment(notificationsFragment);
                        getSupportActionBar().setTitle("الاشعارات");
                        return true;
                    case R.id.info:
                        Fragment infoFragment = new InfoFragment();
                        loadFragment(infoFragment);
                        getSupportActionBar().setTitle("عن المجلة");
                        return true;
                    case R.id.profile:
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null)
                        {
                            Fragment profileFragment = new ProfileFragment();
                            loadFragment(profileFragment);
                            getSupportActionBar().setTitle("المعلومات الشخصية");
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

    public void drawer()
    {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener()
                {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem)
                    {
                        switch (menuItem.getItemId())
                        {
                            case R.id.d1 :
                                Fragment d1Fragment = new D1Fragment();
                                loadFragment(d1Fragment);
                                getSupportActionBar().setTitle("كبسولات السعاده");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d2 :
                                Fragment d2Fragment = new D2Fragment();
                                loadFragment(d2Fragment);
                                getSupportActionBar().setTitle("أنت حر");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d3 :
                                Fragment d3Fragment = new D3Fragment();
                                loadFragment(d3Fragment);
                                getSupportActionBar().setTitle("هن");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d4 :
                                Fragment d4Fragment = new D4Fragment();
                                loadFragment(d4Fragment);
                                getSupportActionBar().setTitle("أنت و مزاجك");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d5 :
                                Fragment d5Fragment = new D5Fragment();
                                loadFragment(d5Fragment);
                                getSupportActionBar().setTitle("اسمع غيرك");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d6 :
                                Fragment d6Fragment = new D6Fragment();
                                loadFragment(d6Fragment);
                                getSupportActionBar().setTitle("اختلاف مش خلاف");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d7 :
                                Fragment d7Fragment = new D7Fragment();
                                loadFragment(d7Fragment);
                                getSupportActionBar().setTitle("رؤي");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d8 :
                                Fragment d8Fragment = new D8Fragment();
                                loadFragment(d8Fragment);
                                getSupportActionBar().setTitle("ملف العدد");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d9 :
                                Fragment d9Fragment = new D9Fragment();
                                loadFragment(d9Fragment);
                                getSupportActionBar().setTitle("حوار العدد");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d10 :
                                Fragment d10Fragment = new D10Fragment();
                                loadFragment(d10Fragment);
                                getSupportActionBar().setTitle("زقزوقة");
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addBadgeView()
    {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);

        final View notificationBadge = LayoutInflater.from(MainActivity.this).inflate(R.layout.view_notification_badge, menuView, false);

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
                        //imageView.setImageResource(R.drawable.notification_badge);
                        notificationBadge.setVisibility(GONE );
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
