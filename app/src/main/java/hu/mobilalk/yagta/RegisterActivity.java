package hu.mobilalk.yagta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private static final String LOG_TAG = RegisterActivity.class.getName();
    private static final String PREF_KEY = MainActivity.class.getPackage().toString();

    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY", 0);

        if (secret_key != 666) {
            finish();
        }

        userNameET = findViewById(R.id.userNameEditText);
        emailET = findViewById(R.id.userEmailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);
        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);
        String username = preferences.getString("userName", "");
        String password = preferences.getString("password", "");

        userNameET.setText(username);
        passwordET.setText(password);
    }

    public void register(View view) {
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if (!password.equals(passwordAgain)) {
            Log.e(LOG_TAG, "Nem jó a jelszó megerősítése!");
            return;
        }
        // TODO: Regisztráció
        Log.i(LOG_TAG, "Regisztrált: " + userName + " email: " + email + " jelszo: " + password);
    }

    public void cancel(View view) {
        finish();
    }
}