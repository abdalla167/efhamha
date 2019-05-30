package atmosphere.sh.efhamha.aesh.ha.Helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

public class InputValidator {

    public static boolean signUpValidation(Context context, EditText userName, EditText emailET, EditText passwordET, EditText confirmPasswordET) {

        String name = userName.getText().toString().trim();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String confirmPassword = confirmPasswordET.getText().toString().trim();
        if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()  || confirmPassword.isEmpty() || password.length() < 6 || !password.equals(confirmPassword)) {

            if(name.isEmpty())
                Toast.makeText(context, "يرجي كتابة اسم المستخدم", Toast.LENGTH_LONG).show();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(context, "البريد الألكتروني غير صالح", Toast.LENGTH_LONG).show();

            if (email.isEmpty())
                Toast.makeText(context, "يرجي كتابة البريد الألكتروني", Toast.LENGTH_LONG).show();

            if (password.length() < 6)
                Toast.makeText(context, "يجب ادخال كلمه سر اكبر من 6", Toast.LENGTH_LONG).show();

            if (password.isEmpty())
                Toast.makeText(context, "يرجي كتابة الباسورد", Toast.LENGTH_LONG).show();

            if (confirmPassword.isEmpty())
                Toast.makeText(context, "يرجي تأكيد كلمة السر", Toast.LENGTH_LONG).show();

            if (!(password.equals(confirmPassword)))
                Toast.makeText(context, "كلمه السر غير متطابقه", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }
    public static boolean signInValidation(Context context, EditText emailET, EditText passwordET) {

        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || password.isEmpty()) {

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(context, "البريد الألكتروني غير صالح", Toast.LENGTH_LONG).show();

            if (email.isEmpty())
                Toast.makeText(context, "يرجي كتابة البريد الألكتروني", Toast.LENGTH_LONG).show();

            if (password.isEmpty())
                Toast.makeText(context, "يرجي كتابة الباسورد", Toast.LENGTH_LONG).show();


            return false;
        }
        return true;
    }
    public static boolean emailValidation(Context context, EditText emailET){

        String email = emailET.getText().toString().trim();

        if(email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                Toast.makeText(context, "البريد الألكتروني غير صالح", Toast.LENGTH_LONG).show();

            if (email.isEmpty())
                Toast.makeText(context, "يرجي كتابة البريد الألكتروني", Toast.LENGTH_LONG).show();

            return false;
        }
        return true;
    }
}
