package atmosphere.sh.efhamha.aesh.ha;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class offLine extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
