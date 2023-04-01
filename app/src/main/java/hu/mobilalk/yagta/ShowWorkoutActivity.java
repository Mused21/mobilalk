package hu.mobilalk.yagta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class ShowWorkoutActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mWorkouts;
    private LinearLayout linearLayout;

    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_workout);

        mFirestore = FirebaseFirestore.getInstance();
        mWorkouts = mFirestore.collection("Workouts");
        linearLayout = findViewById(R.id.linearLayout);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
        String documentPath = getIntent().getStringExtra("documentPath");

        mWorkouts.document(documentPath)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Edzes currentEdzes = documentSnapshot.toObject(Edzes.class);
                    LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    dateTextView = findViewById(R.id.dateTextView);
                    dateTextView.setText(dateFormat.format(currentEdzes.getDate()));

                    for (Gyakorlat gyakorlat : currentEdzes.getGyakorlatok()) {
                        final View addView = layoutInflater.inflate(R.layout.previous_edzes, null);

                        TextView gyakorlatSzama = addView.findViewById(R.id.gyakorlatSzama);
                        gyakorlatSzama.setText(String.format("%d.", gyakorlat.getId()));

                        TextView gyakorlatNeve = addView.findViewById(R.id.gyakorlatNeve);
                        gyakorlatNeve.setText(gyakorlat.getGyakorlatNev());

                        TextView ismetles = addView.findViewById(R.id.ismetles);
                        ismetles.setText(String.valueOf(gyakorlat.getIsmetles()));

                        TextView suly = addView.findViewById(R.id.suly);
                        suly.setText(String.valueOf(gyakorlat.getSuly()));

                        linearLayout.addView(addView);
                    }
                });
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
}