package hu.mobilalk.yagta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewWorkoutActivity extends AppCompatActivity {

    private static final String LOG_TAG = NewWorkoutActivity.class.getName();
    private static final String PREF_KEY = NewWorkoutActivity.class.getPackage().toString();
    private int counter = 1;
    private FirebaseUser user;

    TextView dateTV;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_workout);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
        dateTV = findViewById(R.id.dateTextView);
        dateTV.setText(new SimpleDateFormat("yyyy.MM.dd.", Locale.getDefault()).format(new Date()));
        linearLayout = findViewById(R.id.linearLayout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.gym_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_button:
                return true;
            case R.id.logout_button:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newSet(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.szett, null);
        TextView szettSzama = (TextView)addView.findViewById(R.id.szettSzama);
        szettSzama.setTextSize(20);
        szettSzama.setText(String.format("%d. szett", counter++));


        Button buttonRemove = (Button)addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(v -> {
            ((LinearLayout)addView.getParent()).removeView(addView);
            counter--;
            reorderChildren();
        });

        linearLayout.addView(addView);
    }

    private void reorderChildren() {
        for (int i = 2; i <= counter; i++) {
            View view = linearLayout.getChildAt(i);
            TextView szettSzama = (TextView)view.findViewById(R.id.szettSzama);
            szettSzama.setText(String.format("%d. szett", i-1));
        }
    }
}