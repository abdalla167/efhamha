package atmosphere.sh.efhamha.aesh.ha.SignUp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import atmosphere.sh.efhamha.aesh.ha.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.R;
import atmosphere.sh.efhamha.aesh.ha.SignIn.SignInActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EmailAndPasswordActivity extends AppCompatActivity {

    @BindView(R.id.signUp_email_editText)
    EditText signUpEmailEditText;
    @BindView(R.id.signUp_password_editText)
    EditText signUpPasswordEditText;
    @BindView(R.id.signUp_button)
    Button signUpButton;
    @BindView(R.id.signUp_confirm_password_editText)
    EditText signUpConfirmPasswordEditText;

    private String email, password;
    private ProgressDialog progressDialog;
    private static final String TAG = "signUp";

    //Firebase
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_and_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
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
                            Toast.makeText(EmailAndPasswordActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
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
}
