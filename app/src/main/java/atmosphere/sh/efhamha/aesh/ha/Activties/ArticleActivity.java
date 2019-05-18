package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import atmosphere.sh.efhamha.aesh.ha.Adapter.CommentAdatpterrecycle;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.UsercommentModel;
import atmosphere.sh.efhamha.aesh.ha.R;

public class ArticleActivity extends AppCompatActivity
{
    TextView title;
    TextView content, source;
    ImageView imageArchi;
    EditText commenttext;

    //for all view
    ArticleModel aricle_obj;

    // for comments
    //for comments
    private RecyclerView recyclerView;
    private CommentAdatpterrecycle adapter;
    private ArrayList<UsercommentModel> commentmodellist;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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
        imageArchi = findViewById(R.id.article_image_full);
        imageArchi = findViewById(R.id.article_image_full);

        commenttext = findViewById(R.id.comment_text);

        //define recycleview for comments
        recyclerView = findViewById(R.id.list_comments);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        //recyclerView.setNestedScrollingEnabled(false);
        // get article which saved
        Intent intent = getIntent();
        aricle_obj = intent.getParcelableExtra("article object");
    }

    private void add_data_to_layout()
    {
        title.setText(aricle_obj.getTitle());
        source.setText(aricle_obj.getSource());
        content.setText(aricle_obj.getContent());
    }

    private void fakedataforcomment()
    {
        commentmodellist = new ArrayList<>();
        HashMap<Integer, ArrayList<String>> hashMap = new HashMap<>();
        hashMap = aricle_obj.getUser_comments();

        for (int i = 0; i < hashMap.size(); i++)
        {
            for (int j = 0; j < hashMap.get(1).size(); j++)
            {
                UsercommentModel usercommentModel = new UsercommentModel(null, "محمد عادل" + i, aricle_obj.getUser_comments().get(1).get(j));
                commentmodellist.add(usercommentModel);
            }
        }
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new CommentAdatpterrecycle(commentmodellist);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void send_comment(View view)
    {
        if (commenttext.getText() != null)
        {
            UsercommentModel usercommentModel = new UsercommentModel(null, "محمد عادل", commenttext.getText().toString());

            commentmodellist.add(usercommentModel);
            adapter = new CommentAdatpterrecycle(commentmodellist);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            commenttext.setText("");
        }
    }
}
