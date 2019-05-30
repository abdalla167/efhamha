package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.victor.loading.rotate.RotateLoading;

import atmosphere.sh.efhamha.aesh.ha.R;

public class VideoActivity extends AppCompatActivity
{
    VideoView videoView;
    RotateLoading rotateLoading;
    String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.video_view);
        rotateLoading = findViewById(R.id.rotateloading);

        URL = getIntent().getStringExtra("url");

        rotateLoading.start();

        Uri uri = Uri.parse(URL);

        MediaController mediaController = new MediaController(VideoActivity.this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
        {
            @Override
            public void onPrepared(MediaPlayer mp)
            {
                rotateLoading.stop();
                videoView.requestFocus();
                videoView.start();
            }
        });
    }
}
