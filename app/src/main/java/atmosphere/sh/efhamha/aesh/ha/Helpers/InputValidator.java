package atmosphere.sh.efhamha.aesh.ha.Helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidator {

    public static boolean signUpValidation(EditText userName, EditText emailET, EditText passwordET, EditText confirmPasswordET) {

        String name = userName.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()  || confirmPassword.isEmpty() || password.length() < 6 || !password.equals(confirmPassword)) {

            if(name.isEmpty())
                userName.setError("يرجي كتابة اسم المستخدم");

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                emailET.setError("البريد الألكتروني غير صالح");

            if (email.isEmpty())
                emailET.setError("يرجي كتابة البريد الألكتروني");

            if (password.length() < 6)
                passwordET.setError("يجب ادخال كلمه سر اكبر من 6");

            if (password.isEmpty())
                passwordET.setError("يرجي كتابة الباسورد");

            if (confirmPassword.isEmpty())
                confirmPasswordET.setError("يرجي تأكيد كلمة السر");

            if (!(password.equals(confirmPassword)))
                confirmPasswordET.setError("كلمه السر غير متطابقه");

            return false;
        }
        return true;
    }
    public static boolean signInValidation(EditText emailET, EditText passwordET) {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()) {

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                emailET.setError("البريد الألكتروني غير صالح");

            if (email.isEmpty())
                emailET.setError("يرجي كتابة البريد الألكتروني");

            if (password.isEmpty())
                passwordET.setError("يرجي كتابة الباسورد");


            return false;
        }
        return true;
    }
    public static boolean emailValidation(EditText emailET){

        String email = emailET.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                emailET.setError("البريد الألكتروني غير صالح");

            if (email.isEmpty())
                emailET.setError("يرجي كتابة البريد الألكتروني");

            return false;
        }
        return true;
    }
}
