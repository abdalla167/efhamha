package atmosphere.sh.efhamha.aesh.ha;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;




public class Showfile extends AppCompatActivity {

    WebView pdfView;

    String title;
    String filelink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showfile);




        pdfView = findViewById(R.id.pdfView);
        title = getIntent().getStringExtra("title");
        filelink = getIntent().getStringExtra("pdflink");

        Toast.makeText(this, ""+filelink, Toast.LENGTH_SHORT).show();

        pdfView.setWebViewClient(new WebViewClient());
        pdfView.loadUrl(filelink);

        WebSettings webSettings = pdfView.getSettings();
        webSettings.setJavaScriptEnabled(true);



    }





}

