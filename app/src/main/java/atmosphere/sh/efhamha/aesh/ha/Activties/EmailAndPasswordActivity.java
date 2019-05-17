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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import atmosphere.sh.efhamha.aesh.ha.Helpers.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EmailAndPasswordActivity extends AppCompatActivity {

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

    private String email, password;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private static final String TAG = "signUp";
    private Uri photoPath;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_and_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        profileImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON_TOUCH)
                        .setAspectRatio(1,1)
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
            if (resultCode == Activity.RESULT_OK)
            {
                if (result != null)
                {
                    photoPath = result.getUri();

                    Picasso.get()
                            .load(photoPath)
                            .placeholder(R.drawable.user)
                            .error(R.drawable.user)
                            .into(profileImage);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    @OnClick({R.id.signUp_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signUp_button:
                if(getInputData())
                    signUp(email, password);
                break;
        }
    }

    private void signUp(String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("رجاء الأنتظار....");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            emialAddressVerification(user);
                            alertDialog = new AlertDialog.Builder(EmailAndPasswordActivity.this);
                            alertDialog.setCancelable(false);
                            alertDialog.setTitle("الرجاء تأكيد الايميل");
                            alertDialog.setMessage("أفحص بريدك الألكتروني لتأكيد الأيميل");
                            alertDialog.setPositiveButton("تم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                            alertDialog.show();
                            //startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                            //finish();
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
        if (!(InputValidator.signUpValidation(signUpEmailEditText, signUpPasswordEditText, signUpConfirmPasswordEditText))) {
            return false;
        }
        email = signUpEmailEditText.getText().toString().trim();
        password = signUpPasswordEditText.getText().toString().trim();

        return true;
    }

    private void emialAddressVerification(FirebaseUser user){
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
}
