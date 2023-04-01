package hu.mobilalk.yagta;

import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.ActivityOptions;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class GymMenuActivity extends AppCompatActivity {
    private static final String LOG_TAG = GymMenuActivity.class.getName();

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 101;
    private FirebaseUser user;
    private TextView welcomeText;
    private Resources resources;
    private FirebaseFirestore mFirestore;
    private AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym_menu);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.d(LOG_TAG, "Authenticated user!");
        } else {
            Log.d(LOG_TAG, "Unauthenticated user!");
            finish();
        }
        resources = getResources();
        mFirestore = FirebaseFirestore.getInstance();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        mFirestore.collection("Users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    User user = new User();
            for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                user = document.toObject(User.class);
            }
            welcomeText = findViewById(R.id.welcomeTextView);
            welcomeText.setText(String.format(resources.getString(R.string.welcome_message), user.getUserName()));
        });
        checkUserPermission();
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
                return true;
            case R.id.logout_button:
                FirebaseAuth.getInstance().signOut();
                Intent logoutIntent = new Intent(this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
                return true;
            case R.id.home_button:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newWorkout(View view) {
        Intent intent = new Intent(this, NewWorkoutActivity.class);
        startActivity(intent);
    }

    public void previousWorkouts(View view) {
        Intent intent = new Intent(this, PreviousWorkoutsActivity.class);
        startActivity(intent);
    }

    private void setAlarmManager() {
        // teszteléshez
        long repeatInterval = 60 * 1000;
        // long repeatInterval = AlarmManager.INTERVAL_DAY;
        long triggerTime = SystemClock.elapsedRealtime() + repeatInterval;
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE);

        alarmManager.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerTime,
                repeatInterval,
                pendingIntent);
    }

    private void checkUserPermission() {
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(GymMenuActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},REQUEST_CODE_ASK_PERMISSIONS);
            } else {
                setAlarmManager();
            }
        } else {
            setAlarmManager();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setAlarmManager();
            } else {
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}