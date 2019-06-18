package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import atmosphere.sh.efhamha.aesh.ha.Models.Artical_Move;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditArtical extends AppCompatActivity {


    EditText editText;
    Button updat,cancle;
    String old;
    FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    String KEY1;
    CircleImageView image;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_artical);


        editText=findViewById(R.id.edite_artical_text);

        updat=findViewById(R.id.update_comment_art);
        cancle=findViewById(R.id.cancle_update_art);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        final ArticleModel articleModel = (ArticleModel) getIntent().getSerializableExtra("modelAreticl");
        KEY1 = getIntent().getStringExtra("key_art");

        editText.setText(articleModel.getContent().toString());


        //Toast.makeText(EditArtical.this, ""+editText.getText().toString(), Toast.LENGTH_LONG).show();
        updat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Artical_Move artical_move=new Artical_Move(articleModel.getImage_url(),articleModel.getTitle(),editText.getText().toString()
                        ,articleModel.getSource(),articleModel.getArticle_time(),articleModel.getArticle_day(),
                        articleModel.getArticle_month(),articleModel.getArticle_year(),articleModel.getWordfile(),articleModel.getType(),articleModel.getCaption().get(0));

                       databaseReference.child("Articles").child(KEY1).setValue(artical_move);

finish();

            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();



            }
        });

    }
}