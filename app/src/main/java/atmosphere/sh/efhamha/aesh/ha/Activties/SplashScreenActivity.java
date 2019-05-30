package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Timer;
import java.util.TimerTask;

import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.R;

public class SplashScreenActivity extends AppCompatActivity
{
    private FirebaseUser user;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        user = FirebaseAuth.getInstance().getCurrentUser();

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                if(user != null && user.getEmail().equals("admin@admin.com"))
                {
                    startActivity(new Intent(SplashScreenActivity.this, AdminActivity.class));
                    finish();
                } else
                    {
                        if (user.isEmailVerified())
                        {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);
                            // kill current activity
                            finish();
                        } else
                            {
                                Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                                startActivity(i);
                                // kill current activity
                                finish();
                            }
                    }
            }
        };
        // Show splash screen for 3 seconds
        new Timer().schedule(task, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}