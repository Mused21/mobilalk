package hu.mobilalk.yagta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PreviousWorkoutsActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;
    private CollectionReference mWorkouts;
    private FirebaseUser user;
    private LinearLayout linearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_workouts);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            finish();
        }

        linearLayout = findViewById(R.id.linearLayout);
        mFirestore = FirebaseFirestore.getInstance();
        mWorkouts = mFirestore.collection("Workouts");
        mWorkouts.whereEqualTo("email", user.getEmail()).orderBy("date")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Edzes> edzesek = new ArrayList<>();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault());
                    Map<Date, String> map = new HashMap<>();
                    for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                        Edzes edzes = document.toObject(Edzes.class);
                        edzesek.add(edzes);
                        map.put(edzes.getDate(), document.getId());
                    }

                    for (Edzes edzes : edzesek) {
                        TableRow tr = (TableRow) getLayoutInflater().inflate(R.layout.felsorolas, null);

                        Button curentDeleteButton = tr.findViewById(R.id.removeWorkout);
                        curentDeleteButton.setOnClickListener(v -> {
                            ((LinearLayout)tr.getParent()).removeView(tr);
                            mWorkouts.document(map.get(edzes.getDate())).delete();
                        });

                        Button currentWorkoutButton = tr.findViewById(R.id.goToWorkout);
                        currentWorkoutButton.setText(dateFormat.format(edzes.getDate()));
                        currentWorkoutButton.setOnClickListener(v -> {
                            Intent intent = new Intent(this, ShowWorkoutActivity.class);
                            intent.putExtra("documentPath", map.get(edzes.getDate()));
                            startActivity(intent);
                        });
                        linearLayout.addView(tr);
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

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}