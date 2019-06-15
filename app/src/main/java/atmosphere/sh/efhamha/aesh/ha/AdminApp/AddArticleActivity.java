package atmosphere.sh.efhamha.aesh.ha.AdminApp;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import atmosphere.sh.efhamha.aesh.ha.Activties.EmailAndPasswordActivity;
import atmosphere.sh.efhamha.aesh.ha.Models.ArticleModel;
import atmosphere.sh.efhamha.aesh.ha.R;

import static atmosphere.sh.efhamha.aesh.ha.R.drawable.final_logo;
import static atmosphere.sh.efhamha.aesh.ha.R.drawable.ic_add_a_photo_black_24dp;

public class AddArticleActivity extends AppCompatActivity {
    private static final int word_id = 4;
    private Uri word_uri;
    private String word_url;


    private ArrayList<Uri> uri_image = new ArrayList<>();
    private ArrayList<String> image_url = new ArrayList<>();

    private Uri video_uri;


    private ImageView im, video;
    private TextView arc_title, arc_source, arc_content, url_txt,caption;
    private Spinner spinner;
    String category;
    int type;
    private Button addpdffile;

    private StorageReference mstorageref = FirebaseStorage.getInstance().getReference("ArticlesImages");
    private DatabaseReference mdatarefre = FirebaseDatabase.getInstance().getReference();

    private ProgressDialog prog;

    int c = 0;
    String time_txt, day_txt, month_txt, year_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        im = findViewById(R.id.add_article_image);
        url_txt = findViewById(R.id.url);
        video = findViewById(R.id.add_article_video);
        arc_title = findViewById(R.id.add_article_title);
        arc_source = findViewById(R.id.add_article_by);
        arc_content = findViewById(R.id.add_article_content);
        spinner = findViewById(R.id.categ_spinner);
        addpdffile = findViewById(R.id.addpdf);
caption=findViewById(R.id.add_caption);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.categories, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = String.valueOf(parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        prog = new ProgressDialog(this);

        prog.setMessage("برجاء الانتظار ...");
        prog.setCanceledOnTouchOutside(false);

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int time = calendar.get(Calendar.HOUR_OF_DAY);
        int time2 = calendar.get(Calendar.MINUTE);

        switch (month) {
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
        String t1 = Integer.toString(time);
        String t2 = Integer.toString(time2);

        time_txt = t1 + ":" + t2;
    }

    private String getfileextention(Uri ur) {
        ContentResolver resolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(resolver.getType(ur));
    }

    public void chooseimageformgallery(View view) {
        if (video_uri == null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                    .setAspectRatio(1, 1)
                    .start(AddArticleActivity.this);
        } else {
            Toast.makeText(getApplicationContext(), "انت مختار فيديو", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    type = 1;
                    uri_image.add(result.getUri());
                    url_txt.setText(uri_image.get(uri_image.size() - 1).getLastPathSegment());

                    Toast.makeText(this, "انت اخترت " + uri_image.size() + "صور", Toast.LENGTH_SHORT).show();

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (resultCode == RESULT_OK) {
            if (requestCode == 103) {
                type = 2;
                assert data != null;
                video_uri = data.getData();
                url_txt.setText(video_uri.getLastPathSegment());
            }


            //3shan uri bta3 l word file
            else if (requestCode == word_id) {
                assert data != null;
                word_uri = data.getData();
                addpdffile.setText("تغير ملف");

                Toast.makeText(this, "تم ادراج ملف ", Toast.LENGTH_SHORT).show();

            }
        }


    }

    public void upload_article(View view) {


        if (uri_image == null || arc_title.getText().toString().equals("")||caption.getText().toString().equals("") || arc_source.getText().toString().equals("") || arc_content.getText().toString().equals("") || category.equals("اختار موضوع")) {
            Toast.makeText(this, "من فضلك ادخل معلومات المقال", Toast.LENGTH_LONG).show();
        } else {


            prog.show();
            //3shan arf3 word file

            if (word_uri != null) {

                final StorageReference filereference = mstorageref.child(System.currentTimeMillis() + "." + getfileextention(word_uri));
                filereference.putFile(word_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                //get link of word file
                                word_url = uri.toString();

                            }
                        });

                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

            }


            final String title = arc_title.getText().toString();
            final String source = arc_source.getText().toString();
            final String content = arc_content.getText().toString();
            final String cap=caption.getText().toString();

            int i;
            for (i = 0; i < uri_image.size(); i++) {
                final StorageReference filereference = mstorageref.child(System.currentTimeMillis() + "." + getfileextention(uri_image.get(i)));
                filereference.putFile(uri_image.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                image_url.add(uri.toString());


                                if (uri_image.size() == image_url.size()) {
                                    String ID = mdatarefre.push().getKey();
                                    ArticleModel obj = new ArticleModel(ID, image_url, title, content, source, category, time_txt, day_txt, month_txt, year_txt, word_url, type,cap);
                                    mdatarefre.child("Articles").child(ID).setValue(obj);
                                    mdatarefre.child("Notifications").child(ID).setValue(obj);
                                    mdatarefre.child("Categories").child(category).child(ID).setValue(obj);
                                    Toast.makeText(AddArticleActivity.this, "تم اضافة المقال بنجاح", Toast.LENGTH_SHORT).show();
                                    prog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(intent);
                                }

                            }
                        });

                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


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

    public void choosevideo(View view) {
        if (uri_image == null) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Video"), 103);
        } else {
            Toast.makeText(getApplicationContext(), "انت مختار صورة", Toast.LENGTH_SHORT).show();
        }
    }

    public void removeuri(View view) {
        uri_image = null;
        url_txt.setText("المسار");
    }

    public void addwordfile(View view) {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(Intent.createChooser(intent, "Select word"), word_id);
    }

}

