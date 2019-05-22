package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;

import java.util.List;

import atmosphere.sh.efhamha.aesh.ha.AdminApp.AdminActivity;
import atmosphere.sh.efhamha.aesh.ha.Helpers.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignInActivity extends AppCompatActivity {
    @BindView(R.id.signIn_email_editText)
    EditText signInEmailEditText;
    @BindView(R.id.signIn_password_editText)
    EditText signInPasswordEditText;
    @BindView(R.id.signIn_forget_password_text_view)
    TextView signInForgetPasswordTextView;
    @BindView(R.id.signIn_button)
    Button signInButton;
    @BindView(R.id.signIn_signUp_text_view)
    TextView signInSignUpTextView;
    @BindView(R.id.signIn_google_button)
    ImageView signInGoogleButton;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @BindView(R.id.signIn_facebook_button)
    ImageView signInFacebookButton;

    private String email, password;
    private ProgressDialog progressDialog;
    private AlertDialog.Builder alertDialog;
    private static final String TAG1 = "SignInWithEmail";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseUser currentFirebaseUser;


    //Google
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_GOOGLE_SIGN_IN = 1;
    private static final String TAG = "GoogleLogin";

    //Facebook
    private CallbackManager callbackManager;
    private static final String TAG2 = "FacebookLogin";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        ////////////////////////////////////// Google SignIn //////////////////////////////////////////////////////////////////////////////////////
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        ////////////////////////////////////// Facebook SignIn ///////////////////////////////////////////////////////////////////////////////////
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile", "user_friends");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG2, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Toast.makeText(SignInActivity.this, "onCancel", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SignInActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(this, "Google sign in failed" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Google Method
    //Firebase Authentication With Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct)
    {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    // Facebook Method
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signIn(String email, String password) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("رجاء الأنتظار ....");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG1, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (!user.isEmailVerified() && !(user.getEmail().equals("admin@admin.com")))
                            {
                                alertDialog = new AlertDialog.Builder(SignInActivity.this);
                                alertDialog.setCancelable(false);
                                alertDialog.setTitle("الرجاء تأكيد الايميل");
                                alertDialog.setMessage("أفحص بريدك الألكتروني لتأكيد الأيميل");
                                alertDialog.setPositiveButton("تم", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which)
                                    {

                                    }
                                });
                                alertDialog.show();
                            } else if (user.getEmail().equals("admin@admin.com"))
                            {
                                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(intent);
                            } else if (user.isEmailVerified() && !(user.getEmail().equals("admin@admin.com")))
                            {
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }
                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG1, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean getInputData() {
        if (!(InputValidator.signInValidation(signInEmailEditText, signInPasswordEditText)))
        {
            return false;
        }
        email = signInEmailEditText.getText().toString().trim();
        password = signInPasswordEditText.getText().toString().trim();

        return true;
    }

    @OnClick({R.id.signIn_forget_password_text_view, R.id.signIn_button, R.id.signIn_signUp_text_view, R.id.signIn_google_button, R.id.signIn_facebook_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signIn_forget_password_text_view:
                startActivity(new Intent(getApplicationContext(), ForgetPasswordActivity.class));
                //signOut();
                break;
            case R.id.signIn_button:
                if (getInputData())
                    signIn(email, password);
                break;
            case R.id.signIn_signUp_text_view:
                startActivity(new Intent(getApplicationContext(), EmailAndPasswordActivity.class));
                break;
            case R.id.signIn_google_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
            case R.id.signIn_facebook_button:
                loginButton.performClick();
        }
    }

    public void updateUI()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }
}
