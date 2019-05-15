package atmosphere.sh.efhamha.aesh.ha.Activties;

import android.util.Patterns;
import android.widget.EditText;

public class InputValidator {

    public static boolean signUpValidation(EditText emailET, EditText passwordET, EditText confirmPasswordET) {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()  || confirmPassword.isEmpty() || password.length() < 6 || !password.equals(confirmPassword)) {

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
