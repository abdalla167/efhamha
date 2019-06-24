package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
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
    FloatingActionButton share;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user;

   public static String catename ="";




    @SuppressLint("RestrictedApi")
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
        user = FirebaseAuth.getInstance().getCurrentUser();
        share=findViewById(R.id.share_btn1);

        share.setVisibility(VISIBLE);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(user != null){


                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=atmosphere.sh.efhamha.aesh.ha");
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);


                }else {

                    Toast.makeText(MainActivity.this, "لو سمحت سجل دخولك الاول", Toast.LENGTH_SHORT).show();


                }



            }
        });

        drawer();

        fragmentManager = getSupportFragmentManager();

        Fragment articlesFragment = new ArticlesFragment();
        loadFragment(articlesFragment);
        getSupportActionBar().setTitle("أجدد الموضوعات");
        catename="";

        addBadgeView();

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.articles:
                        share.setVisibility(VISIBLE);
                        Fragment articlesFragment1 = new ArticlesFragment();
                        loadFragment(articlesFragment1);
                        getSupportActionBar().setTitle("أجدد الموضوعات");
                        catename="";

                        return true;
                    case R.id.notification:
                        share.setVisibility(GONE);
                        Fragment notificationsFragment = new NotificationsFragment();
                        loadFragment(notificationsFragment);
                        getSupportActionBar().setTitle("الاشعارات");
                        return true;
                    case R.id.info:
                        share.setVisibility(GONE);
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
                                Fragment ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("كبسولات السعاده");
                                catename="كبسولات السعاده";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d2 :
                                 ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("أنت حر");
                                catename="أنت حر";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d3 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("هن");
                                catename="هن";

                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d4 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("أنت و مزاجك");
                                menuItem.setChecked(true);
                                catename="أنت و مزاجك";
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d5 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("اسمع غيرك");
                                catename="اسمع غيرك";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d6 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("اختلاف مش خلاف");
                                catename="اختلاف مش خلاف";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d7 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("رؤى");
                                catename="رؤى";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d8 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("ملف العدد");
                                catename="ملف العدد";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d9 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("حوار العدد");
                                catename="حوار العدد";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.d10 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("زقزوقة");
                                catename="زقزوقة";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;


                            case R.id.d11 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("اجدد الموضوعات");
                                catename="";
                                menuItem.setChecked(true);
                                mDrawerLayout.closeDrawers();
                                return true;

                            case R.id.d12 :
                                ArticlesFragment = new ArticlesFragment();
                                loadFragment(ArticlesFragment);
                                getSupportActionBar().setTitle("على الأصل دور");
                                catename="على الأصل دور";
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
            Toast.makeText(this, "دوس تاني عشان تخرج", Toast.LENGTH_SHORT).show();
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
