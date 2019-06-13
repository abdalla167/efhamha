package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import atmosphere.sh.efhamha.aesh.ha.Models.CommentModel;
import atmosphere.sh.efhamha.aesh.ha.Models.Comment_Move;
import atmosphere.sh.efhamha.aesh.ha.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditComment extends AppCompatActivity {
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
        setContentView(R.layout.activity_edit_comment);
        editText=findViewById(R.id.edite_comment_text);

        updat=findViewById(R.id.update_comment);
        cancle=findViewById(R.id.cancle_update);
        image =findViewById(R.id.image_comment_edit);


        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReference.keepSynced(true);

        final CommentModel commentModel = (CommentModel) getIntent().getSerializableExtra("comment");
        KEY1 = getIntent().getStringExtra("key1");

        final String key2;
        key2=getIntent().getStringExtra("comment_key2");



        editText.setText(commentModel.getContentcomment().toString());
        Picasso.get()
                .load(commentModel.getImage_url())
                .placeholder(R.drawable.ic_darkgrey)
                .error(R.drawable.ic_darkgrey)
                .into(image);


        editText.addTextChangedListener(new TextWatcher() {
int c=0,c2=0;
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

              c2=s.length();
                if (s.length() > 0)
                { //do your work here }
updat.setBackground(getResources().getDrawable( R.drawable.edit_writ));
                    c=s.length();
                }
                else
                {


                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {



            }

            public void afterTextChanged(Editable s) {
if(c2==c){

  //  updat.setBackground(getResources().getDrawable( R.drawable.shap_edit));
}
            }
        });

        updat.setOnClickListener(new View.  OnClickListener() {
            @Override
            public void onClick(View v) {
//
                old = editText.getText().toString();
                if (old != commentModel.getContentcomment().toString()) {
                    Comment_Move comment_move = new Comment_Move(commentModel.getImage_url(), commentModel.getUsername(), editText.getText().toString(), commentModel.getUserid());
                    databaseReference.child("Comments").child(KEY1).child(key2).setValue(comment_move);
                    Toast.makeText(EditComment.this, "تم النعديل بنجاح", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(EditComment.this, "من فضلك ا1ا كنت تريد عمل تعدبل الرجاء تعدبل التعليك ", Toast.LENGTH_SHORT).show();
                }

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

