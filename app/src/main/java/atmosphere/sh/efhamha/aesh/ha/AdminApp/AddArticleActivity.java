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

import java.util.Calendar;
import java.util.Random;

import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.Models.NotificationModel;
import atmosphere.sh.efhamha.aesh.ha.R;

import static atmosphere.sh.efhamha.aesh.ha.R.drawable.ic_add_a_photo_black_24dp;

public class AddArticleActivity extends AppCompatActivity
{
    private static final int img_id = 1;
    private Uri uri_image;
    private String image_url;
    private ImageView im;
    private TextView arc_title,arc_source,arc_content;

    private StorageReference mstorageref = FirebaseStorage.getInstance().getReference("ArticlesImages");
    private DatabaseReference mdatarefre = FirebaseDatabase.getInstance().getReference();

    private ProgressDialog prog;

    String day_txt,month_txt,year_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        im = findViewById(R.id.add_article_image);
        arc_title=findViewById(R.id.add_article_title);
        arc_source=findViewById(R.id.add_article_by);
        arc_content=findViewById(R.id.add_article_content);

        prog = new ProgressDialog(this);

        prog.setMessage("برجاء الانتظار ...");
        prog.setCanceledOnTouchOutside(false);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        switch (month)
        {
            case Calendar.JANUARY:
                month_txt = "يناير";
                break;
            case Calendar.FEBRUARY:
                month_txt = "فبراير";
                break;
            case Calendar.MARCH:
                month_txt = "مارس";
                break;
            case Calendar.APRIL:
                month_txt = "أبريل";
                break;
            case Calendar.MAY:
                month_txt = "مايو";
                break;
            case Calendar.JUNE:
                month_txt = "يونيو";
                break;
            case Calendar.JULY:
                month_txt = "يوليو";
                break;
            case Calendar.AUGUST:
                month_txt = "أغسطس";
                break;
            case Calendar.SEPTEMBER:
                month_txt = "سبتمبر";
                break;
            case Calendar.OCTOBER:
                month_txt = "أكتوبر";
                break;
            case Calendar.NOVEMBER:
                month_txt = "نوفمبر";
                break;
            case Calendar.DECEMBER:
                month_txt = "ديسمبر";
                break;
        }

        day_txt = Integer.toString(day);
        year_txt = Integer.toString(year);
    }

    //get the extention of image
    private String getfileextention(Uri ur)
    {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(ur));
    }

    public void chooseimageformgallery(View view)
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //   startActivityForResult(intent, img_id);
        startActivityForResult(Intent.createChooser(intent, "select"), img_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            uri_image = data.getData();
            im.setImageURI(uri_image);
            Glide.with(this).load(uri_image).into(im);
            im.setImageURI(uri_image);
            im.setBackground(null);
        }
    }

    public void upload_article(View view)
    {
        if (uri_image == null || arc_title.getText().toString().equals("") || arc_source.getText().toString().equals("")|| arc_content.getText() .toString().equals(""))
        {
            Toast.makeText(this, "من فضلك ادخل معلومات المقال", Toast.LENGTH_LONG).show();
        } else {
            final String title =arc_title.getText().toString();
            final String source =arc_source.getText().toString();
            final String content =arc_content.getText().toString();

            prog.show();

            final StorageReference filereference = mstorageref.child(System.currentTimeMillis() + "." + getfileextention(uri_image));
            filereference.putFile(uri_image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot)
                {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            //get id of article before add
                            String ID = mdatarefre.push().getKey();

                            // get link of image and add data in child
                            image_url = uri.toString();
                            ArticleModel obj = new ArticleModel(image_url,title,content,source,ID,null,null,null,null);
                            mdatarefre.child("Articles").child(ID).setValue(obj);
                        }
                    });

                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task)
                {
                    Toast.makeText(AddArticleActivity.this, "تم اضافة المقال بنجاح", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(intent);
                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    prog.hide();
                    Toast.makeText(AddArticleActivity.this, "Error Connection", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

