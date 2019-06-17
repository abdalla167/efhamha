package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.R;

public class SplashScreenActivity extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    String email;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        user = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {

                if (user != null) {
                    email = user.getEmail();

                    reference.child("Admins").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String adminEmail1 = snapshot.child("email").getValue(String.class);
                                if (adminEmail1.equals(email)) {
                                    startActivity(new Intent(SplashScreenActivity.this, AdminActivity.class));
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (user != null) {
                    if (user.isEmailVerified()) {
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    } else {
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                } else {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }

        };
        new Timer().schedule(task, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}