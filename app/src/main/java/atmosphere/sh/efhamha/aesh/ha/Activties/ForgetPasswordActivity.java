package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import atmosphere.sh.efhamha.aesh.ha.Helpers.InputValidator;
import atmosphere.sh.efhamha.aesh.ha.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.reset_email_editText)
    EditText resetEmailEditText;
    @BindView(R.id.btn_reset_password)
    Button btnResetPassword;


    private String email;
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.btn_reset_password)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_reset_password:
                if (getInputData())
                    resetPassowrd(email);
                break;
        }
    }

    private void resetPassowrd(String email) {
        progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
        progressDialog.setMessage("رجاء الأنتظار....");
        progressDialog.show();


        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "افحص بريدك الألكتروني لأعاده تعيين كلمة السر", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                    finish();
                } else{
                    progressDialog.dismiss();
                    Toast.makeText(ForgetPasswordActivity.this, "تعذر أرسال الأيميل" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean getInputData(){
        if(!InputValidator.emailValidation(getApplicationContext(), resetEmailEditText)) {
            return false;
        }
        email = resetEmailEditText.getText().toString().trim();
        return true;
    }
}
