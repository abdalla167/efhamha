package atmosphere.sh.efhamha.aesh.ha.AdminApp;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

import static atmosphere.sh.efhamha.aesh.ha.R.drawable.ic_add_a_photo_black_24dp;

public class AddArticleActivity extends AppCompatActivity {
    private static final int img_id = 1;
    private Uri uri_image;
    private String image_url;
    private ImageView im;
    private TextView arc_title,arc_source,arc_content;

    private StorageReference mstorageref = FirebaseStorage.getInstance().getReference("ArticlesImages");
    private DatabaseReference mdatarefre = FirebaseDatabase.getInstance().getReference();

    private ProgressDialog prog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        im = findViewById(R.id.add_article_image);
        arc_title=findViewById(R.id.add_article_title);
        arc_source=findViewById(R.id.add_article_by);
        arc_content=findViewById(R.id.add_article_content);

        prog = new ProgressDialog(this);

        prog.setTitle("Uploading Data");
        prog.setMessage("Loading");
        prog.setCanceledOnTouchOutside(false);


    }




    //get the extention of image
    private String getfileextention(Uri ur) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(ur));

    }

    public void chooseimageformgallery(View view) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //   startActivityForResult(intent, img_id);
        startActivityForResult(Intent.createChooser(intent, "select"), img_id);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                uri_image = data.getData();
                im.setImageURI(uri_image);
                Glide.with(this).load(uri_image).into(im);
                im.setImageURI(uri_image);
                im.setBackground(null);

    }
    }


    public void upload_article(View view) {

        if (arc_title.getText().toString().equals("") || arc_source.getText().toString().equals("")|| arc_content.getText() .toString().equals("")) {
            Toast.makeText(this, "Please Enter The Article Data", Toast.LENGTH_LONG).show();
        }

        else {
            prog.show();
            if (uri_image != null) {
                final StorageReference filereference = mstorageref.child(System.currentTimeMillis() + "." + getfileextention(uri_image));
                filereference.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                        filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //get id of article before add
                                String ID =mdatarefre.push().getKey();

                                // get link of image and add data in child
                                image_url = uri.toString();
                                ArticleModel obj = new ArticleModel(image_url,arc_title.getText().toString(),arc_content.getText().toString(),arc_source.getText().toString(),ID,null,null,null,null);
                                mdatarefre.child("Articles").child(ID).setValue(obj);


                            }
                        });

                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        //return defualt view to the layout
                        prog.dismiss();
                        Toast.makeText(AddArticleActivity.this, "Data Successfully Uploaded", Toast.LENGTH_SHORT).show();
                        arc_title.setText("");
                        arc_content.setText("");
                        arc_source.setText("");

                        im.setBackgroundColor(getResources().getColor(R.color.backgroundimage));
                        im.setImageURI(null);
                        im.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        prog.hide();
                        Toast.makeText(AddArticleActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();

                    }
                });

            }

        }

    }
    }

