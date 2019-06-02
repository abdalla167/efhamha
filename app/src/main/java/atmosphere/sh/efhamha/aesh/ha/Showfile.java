package atmosphere.sh.efhamha.aesh.ha;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

public class Showfile extends AppCompatActivity {

    PDFView pdfView;
    int pageNumber = 0;
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

        displayFromurl(filelink);


    }

    private void displayFromurl(String url) {

        pdfView.fromUri(Uri.parse(url))
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)
                .password(null)
                .scrollHandle(null)
                .load();
    }


    }

