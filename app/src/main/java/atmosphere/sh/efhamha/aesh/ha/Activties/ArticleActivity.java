package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import atmosphere.sh.efhamha.aesh.ha.Adapter.CommentAdapter;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.UsercommentModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticleActivity extends AppCompatActivity
{
    TextView title;
    TextView content, source, numlikes, numviews, numcomments, numshare;
    ImageView imageArchi, imagelike, imagecomment, imageshare;
    EditText commenttext;

    //for all view
    ArticleModel aricle_obj;

    // for comments
    CommentAdapter commentAdapter;
    ListView commentlistview;
    ArrayList<UsercommentModel> commentlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        definetools();

        add_data_to_layout();

        fakedataforcomment();


        //  Toast.makeText(ArticleActivity.this, "dd"+aricle_obj.getUser_comments().get(1).get(0), Toast.LENGTH_SHORT).show();


    }


    private void definetools() {
        title = findViewById(R.id.article_title_full);
        content = findViewById(R.id.article_content_full);
        source = findViewById(R.id.article_by_full);
        numlikes = findViewById(R.id.numlikes_full);
        numviews = findViewById(R.id.numviews_full);
        numcomments = findViewById(R.id.numcomments_full);
        numshare = findViewById(R.id.numshare_full);
        imageArchi = findViewById(R.id.article_image_full);
        imagelike = findViewById(R.id.like_btn_full);
        imagecomment = findViewById(R.id.comment_btn_full);
        imageshare = findViewById(R.id.share_btn_full);
        imageArchi = findViewById(R.id.article_image_full);
        commentlistview = findViewById(R.id.list_comments);
        commenttext = findViewById(R.id.comment_text);


        commentlistview.setOnTouchListener(
                new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });


        // get article which saved


        Intent intent = getIntent();
        aricle_obj = intent.getParcelableExtra("article object");

    }

    private void add_data_to_layout() {
        title.setText(aricle_obj.getTitle());
        source.setText(aricle_obj.getSource());
        content.setText(aricle_obj.getContent());
        numlikes.setText(String.valueOf(aricle_obj.getUser_likes().size()));
        numcomments.setText(String.valueOf(aricle_obj.getUser_comments().size()));
        numshare.setText(String.valueOf(aricle_obj.getUser_share().size()));
        numviews.setText(String.valueOf(aricle_obj.getUser_view().size()));
    }

    private void fakedataforcomment() {

        commentlist = new ArrayList<>();
        HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();
        hashMap = aricle_obj.getUser_comments();

        for (int i = 0; i < hashMap.size(); i++) {
            for (int j = 0; j < hashMap.get(1).size(); j++) {
                UsercommentModel usercommentModel = new UsercommentModel(null, "محمد عادل" + i, aricle_obj.getUser_comments().get(1).get(j));
                ;
                commentlist.add(usercommentModel);
            }

        }
        commentAdapter = new CommentAdapter(this, R.layout.comment_item, commentlist);
        commentlistview.setAdapter(commentAdapter);

    }

    public void send_comment(View view) {

        if (commenttext.getText() != null) {
            UsercommentModel usercommentModel = new UsercommentModel(null, "محمد عادل", commenttext.getText().toString());
            ;
            commentlist.add(usercommentModel);
            commentAdapter = new CommentAdapter(this, R.layout.comment_item, commentlist);
            commentlistview.setAdapter(commentAdapter);
            commenttext.setText("");
        }

    }
}
