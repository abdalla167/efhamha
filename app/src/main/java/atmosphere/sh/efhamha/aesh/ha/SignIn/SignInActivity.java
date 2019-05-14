package atmosphere.sh.efhamha.aesh.ha.SignIn;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import atmosphere.sh.efhamha.aesh.ha.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.R;
import atmosphere.sh.efhamha.aesh.ha.SignUp.EmailAndPasswordActivity;
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
    Button signInGoogleButton;
    @BindView(R.id.toggle_textView)
    TextView toggleTextView;

    private String email, password;
    //Google
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_GOOGLE_SIGN_IN = 1;
    private static final String TAG = "GoogleLogin";

    private static final String TAG1 = "GoogleLogin";


    //Firebase
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        toggleTextView.setVisibility(View.GONE);
        signInPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        signInPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (signInPasswordEditText.getText().length() >= 1)
                    toggleTextView.setVisibility(View.VISIBLE);
                else
                    toggleTextView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toggleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleTextView.getText().equals("أظهار")) {
                    toggleTextView.setText("أخفاء");
                    signInPasswordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    signInPasswordEditText.setSelection(signInPasswordEditText.length());
                } else {
                    toggleTextView.setText("أظهار");
                    signInPasswordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    signInPasswordEditText.setSelection(signInPasswordEditText.length());
                }
            }
        });

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
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            }
        }
    }

    //Google Method
    //Firebase Authentication With Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            signInEmailEditText.setText(user.getDisplayName());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    @OnClick({R.id.signIn_forget_password_text_view, R.id.signIn_button, R.id.signIn_signUp_text_view, R.id.signIn_google_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.signIn_forget_password_text_view:
                break;
            case R.id.signIn_button:
                if(getInputData())
                    signIn(email, password);
                break;
            case R.id.signIn_signUp_text_view:
                startActivity(new Intent(SignInActivity.this, EmailAndPasswordActivity.class));
                break;
            case R.id.signIn_google_button:
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
                break;
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG1, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignInActivity.this, "Login in!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG1, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed. " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private boolean getInputData() {
        if (!(InputValidator.signInValidation(signInEmailEditText, signInPasswordEditText))) {
            return false;
        }
        email = signInEmailEditText.getText().toString().trim();
        password = signInPasswordEditText.getText().toString().trim();

        return true;
    }
}
