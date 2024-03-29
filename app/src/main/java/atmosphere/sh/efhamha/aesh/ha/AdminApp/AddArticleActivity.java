package atmosphere.sh.efhamha.aesh.ha.AdminApp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.UUID;

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
    private TextView arc_title, arc_source, arc_content, url_txt, captiontext;
    private Spinner spinner;
    String category;
    int type;
    private Button addpdffile;

    private StorageReference mstorageref = FirebaseStorage.getInstance().getReference("ArticlesImages");
    private StorageReference videoStorage = FirebaseStorage.getInstance().getReference("ArticlesVideos");
    private DatabaseReference mdatarefre = FirebaseDatabase.getInstance().getReference();

    private ProgressDialog prog;

    int c = 0;
    String time_txt, day_txt, month_txt, year_txt;

    ArrayList<String> captions = new ArrayList<>();
    private Uri imageUri;
    private LinearLayout uploadImageLayout;


    //--------------------


    String title = "";
    String source = "";
    String content = "";
    //--------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);


        im = findViewById(R.id.add_article_image);
        video = findViewById(R.id.add_article_video);
        arc_title = findViewById(R.id.add_article_title);
        arc_source = findViewById(R.id.add_article_by);
        arc_content = findViewById(R.id.add_article_content);
        spinner = findViewById(R.id.categ_spinner);
        addpdffile = findViewById(R.id.addpdf);
        captiontext = findViewById(R.id.add_caption);
        uploadImageLayout = findViewById(R.id.upload_image_layout);

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
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, img_id);
            startActivityForResult(Intent.createChooser(intent, "select"), 1);
        } else {
            Toast.makeText(getApplicationContext(), "انت مختار فيديو", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                type = 1;
                imageUri = data.getData();
                video_uri = null;
                uploadImageLayout.setVisibility(View.VISIBLE);

            }
        } else if (requestCode == 2) {

            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                type = 2;

                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Video.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath,
                        null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String videoPath = c.getString(columnIndex);
                video_uri = selectedImage;
                uri_image = null;
                c.close();
            }

        }
        //3shan uri bta3 l word file
        else if (requestCode == word_id) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                word_uri = data.getData();
                addpdffile.setText("تغير ملف");

                Toast.makeText(this, "تم ادراج ملف ", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void upload_article(View view) {

        if ((uri_image == null && video_uri == null) || arc_title.getText().toString().equals("") || arc_source.getText().toString().equals("") || arc_content.getText().toString().equals("") || category.equals("اختار موضوع")) {
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


            title = arc_title.getText().toString();
            source = arc_source.getText().toString();
            content = arc_content.getText().toString();

            if (imageUri != null) {
                String ID = mdatarefre.push().getKey();
                ArticleModel obj = new ArticleModel(ID, image_url, title, content, source, category, time_txt, day_txt, month_txt, year_txt, word_url, type, captions);
                mdatarefre.child("Articles").child(ID).setValue(obj);
                mdatarefre.child("Notifications").child(ID).setValue(obj);


                title = "";
                source = "";
                content = "";

                arc_title.setText("");
                arc_source.setText("");
                arc_content.setText("");


                Toast.makeText(AddArticleActivity.this, "تم اضافة المقال بنجاح", Toast.LENGTH_SHORT).show();
                prog.dismiss();
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                startActivity(intent);

            } else if (video_uri != null) {
                final String videoName = UUID.randomUUID().toString() + ".mb4";

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userID = user.getUid();
                videoStorage.child(userID).child(userID + "/" + videoName).putFile(video_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        videoStorage.child(userID).child(userID + "/" + videoName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String ID = mdatarefre.push().getKey();
                                String videoUrl = uri.toString();
                                Log.d("Video", "videoDownloadUrl: " + videoUrl);
                                ArticleModel obj = new ArticleModel(ID, videoUrl, title, content, source, category, time_txt, day_txt, month_txt, year_txt, type);
                                mdatarefre.child("Articles").child(ID).setValue(obj);
                                mdatarefre.child("Notifications").child(ID).setValue(obj);


                                title = "";
                                source = "";
                                content = "";

                                arc_title.setText("");
                                arc_source.setText("");
                                arc_content.setText("");


                                Toast.makeText(AddArticleActivity.this, "تم اضافة المقال بنجاح", Toast.LENGTH_SHORT).show();
                                prog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }
        }
    }

    public void choosevideo(View view) {
        if (uri_image.size() == 0) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            intent.setType("video/*");
            startActivityForResult(intent, 2);
        } else {
            Toast.makeText(getApplicationContext(), "انت مختار صورة", Toast.LENGTH_SHORT).show();
        }
    }

    public void addwordfile(View view) {


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("application/*");
        startActivityForResult(Intent.createChooser(intent, "Select word"), word_id);
    }

    public void uploadImage(View view) {
        if (imageUri == null)
            Toast.makeText(this, "من فضلك إختار صوره", Toast.LENGTH_SHORT).show();
        else {
            String caption = captiontext.getText().toString();

            if (caption.equals("")) {
                captions.add("");
            } else {
                captions.add(caption);
                captiontext.setText("");
            }

            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("رفع الصوره");
            progressDialog.setMessage("جاري تحميل الصوره");
            progressDialog.show();
            final StorageReference filereference = mstorageref.child(System.currentTimeMillis() + "." + getfileextention(imageUri));
            filereference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filereference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            progressDialog.dismiss();
                            Toast.makeText(AddArticleActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                            image_url.add(uri.toString());
                        }
                    });
                }
            });

        }
        uploadImageLayout.setVisibility(View.GONE);
    }

    public void resetInput(View view) {
        title = "";
        source = "";
        content = "";

        arc_title.setText("");
        arc_source.setText("");
        arc_content.setText("");


    }
}


