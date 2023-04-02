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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewWorkoutActivity extends AppCompatActivity {

    private static final String LOG_TAG = NewWorkoutActivity.class.getName();
    private int counter = 1;
    private FirebaseUser user;
    private TextView dateTV;
    private LinearLayout linearLayout;
    private FirebaseFirestore mFirestore;
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
        mFirestore = FirebaseFirestore.getInstance();
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
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.logout_button:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                return true;
            case R.id.home_button:
                Intent homeIntent = new Intent(this, GymMenuActivity.class);
                startActivity(homeIntent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newExercise(View view) {
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addView = layoutInflater.inflate(R.layout.edzes, null);
        TextView gyakorlatSzama = addView.findViewById(R.id.gyakorlatSzama);
        gyakorlatSzama.setTextSize(20);
        gyakorlatSzama.setText(String.format("%d.", counter++));

        Button buttonRemove = addView.findViewById(R.id.remove);
        buttonRemove.setOnClickListener(v -> {
            ((LinearLayout)addView.getParent()).removeView(addView);
            counter--;
            reorderChildren();
        });

        linearLayout.addView(addView);
    }

    private void reorderChildren() {
        for (int i = 3; i <= counter+1; i++) {
            View view = linearLayout.getChildAt(i);
            TextView gyakorlatSzama = view.findViewById(R.id.gyakorlatSzama);
            gyakorlatSzama.setText(String.format("%d.", i-2));
        }
    }

    public void saveWorkout(View view) {
        if (linearLayout.getChildCount() <= 3) {
            return;
        }

        boolean skipped = false;

        CollectionReference mWorkouts = mFirestore.collection("Workouts");
        List<Gyakorlat> gyakorlatok = new ArrayList<>();

        for (int i = 3; i <= counter+1; i++) {
            View currentView = linearLayout.getChildAt(i);

            TextView ismetlesSzamTV = currentView.findViewById(R.id.ismetles);
            TextView gyakorlatNeveTV = currentView.findViewById(R.id.gyakorlatNeve);
            TextView sulyTV = currentView.findViewById(R.id.suly);

            double suly = 0.0;

            if (gyakorlatNeveTV.getText().toString().isEmpty() || ismetlesSzamTV.getText().toString().isEmpty()) {
                skipped = true;
                continue;
            }

            if (!sulyTV.getText().toString().isEmpty()) {
                suly = Double.parseDouble(sulyTV.getText().toString());
            }

            Gyakorlat currentGyakorlat = new Gyakorlat(
                    i-2,
                    gyakorlatNeveTV.getText().toString(),
                    suly,
                    Integer.parseInt(ismetlesSzamTV.getText().toString())
            );

            gyakorlatok.add(currentGyakorlat);
        }

        if (!gyakorlatok.isEmpty()) {

            if (skipped) {
                int i = 1;
                for (Gyakorlat gyakorlat : gyakorlatok) {
                    gyakorlat.setId(i++);
                }
            }

            Edzes edzes = new Edzes(user.getEmail(), new Date(), gyakorlatok);
            mWorkouts.add(edzes);
            finish();
        }
    }
}