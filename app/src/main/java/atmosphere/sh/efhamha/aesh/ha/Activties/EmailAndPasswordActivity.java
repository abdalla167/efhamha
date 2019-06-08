package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import java.util.UUID;

import atmosphere.sh.efhamha.aesh.ha.Helpers.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.Models.UserModel;
import atmosphere.sh.efhamha.aesh.ha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmailAndPasswordActivity extends AppCompatActivity
{
    @BindView(R.id.signUp_email_editText)
    EditText signUpEmailEditText;
    @BindView(R.id.signUp_password_editText)
    EditText signUpPasswordEditText;
    @BindView(R.id.signUp_button)
    Button signUpButton;
    @BindView(R.id.signUp_confirm_password_editText)
    EditText signUpConfirmPasswordEditText;
    @BindView(R.id.profile_image)
    CircleImageView profileImage;
    @BindView(R.id.username_field)
    EditText usernameField;
    TextView textView;
    private String userName, email, password;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private static final String TAG = "signUp";
    private Uri photoPath;

    //Firebase
    private FirebaseAuth mAuth;

    //Firebase Database
    FirebaseDatabase database;
    DatabaseReference ref;

    FirebaseStorage firebaseStorage;
    StorageReference mainRef, userImagesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_and_password);

        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        mainRef = firebaseStorage.getReference();
        userImagesRef = mainRef.child("images/users");
textView=findViewById(R.id.logen_agin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_24dp);
        getSupportActionBar().setTitle("");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(EmailAndPasswordActivity.this,SignInActivity.class);
                startActivity(intent);

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1, 1)
                        .start(EmailAndPasswordActivity.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                if (result != null) {
                    photoPath = result.getUri();
                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(profileImage);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @OnClick({R.id.signUp_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signUp_button:
                if (getInputData())
                    signUp(email, password);
                break;
        }
    }

    private void signUp(final String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("برجاء الانتظار ....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Log.d(TAG, "createUserWithEmail:success");
                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            emialAddressVerification(user);
                            alertDialog = new AlertDialog.Builder(EmailAndPasswordActivity.this);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("الرجاء تأكيد الايميل");
                            alertDialog.setMessage("افحص بريدك الالكتروني لتأكيد الايميل");
                            alertDialog.setPositiveButton("تم", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    //لازم يفعل الأيميل عشان يدخل
                                    Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alertDialog.show();
                            //Upload Image To Firebase Storage
                            saveUser(photoPath, user.getUid());
                        } else {
                            progressDialog.dismiss();

                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailAndPasswordActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean getInputData() {
        if (photoPath == null || Uri.EMPTY.equals(photoPath)) {
            Toast.makeText(this, "يرجي أختيار صوره", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!(InputValidator.signUpValidation(getApplicationContext(), usernameField, signUpEmailEditText, signUpPasswordEditText, signUpConfirmPasswordEditText))) {
            return false;
        }

        userName = usernameField.getText().toString();
        email = signUpEmailEditText.getText().toString().trim();
        password = signUpPasswordEditText.getText().toString().trim();

        return true;
    }

    private void emialAddressVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.e(TAG, "Verification email sent to ");
                        } else {
                            Log.e(TAG, "sendEmailVerification failed!", task.getException());
                        }
                    }
                });
    }

    private void saveUser(Uri uri, String id) {

        final String imageName = UUID.randomUUID().toString() + ".jpg";
        final String userId = id;

        userImagesRef.child(userId + "/" + imageName).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        userImagesRef.child(userId + "/" + imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downUri = uri.toString();
                                ref = database.getReference();
                                UserModel userModel = new UserModel(userId, userName, email, downUri);
                                ref.child("Users").child(userId).setValue(userModel);
                            }
                        });
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}