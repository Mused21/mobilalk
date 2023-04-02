package hu.mobilalk.yagta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser mUser;
    private EditText userNameEditText;
    private TextView userEmailTextView;
    private EditText userWeightEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private CollectionReference mUsers;
    private User user;
    private String userDocumentPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();

        if (mUser == null) {
            finish();
        }

        userNameEditText = findViewById(R.id.userNameEditText);
        userEmailTextView = findViewById(R.id.userEmailTextView);
        userWeightEditText = findViewById(R.id.userWeightEditText);
        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);

        mUsers = mFirestore.collection("Users");
        mUsers.whereEqualTo("email", mUser.getEmail())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    user = new User();
                    for (QueryDocumentSnapshot document: queryDocumentSnapshots) {
                        user = document.toObject(User.class);
                        userDocumentPath = document.getId();
                    }
                    userNameEditText.setText(user.getUserName());
                    userEmailTextView.setText(user.getEmail());
                    userWeightEditText.setText(String.valueOf(user.getTestsuly()));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void edit(View view) {
        String newUserName = userNameEditText.getText().toString().trim();
        User userToSave = user;
        boolean nameChanged = false;
        if (!user.getUserName().equals(newUserName) && !newUserName.isEmpty()) {
            nameChanged = true;
            userToSave.setUserName(newUserName);
        }

        double newTestSuly = Double.parseDouble(userWeightEditText.getText().toString());
        boolean testsulyChanged = false;
        if (user.getTestsuly() != newTestSuly) {
            testsulyChanged = true;
            userToSave.setTestsuly(newTestSuly);
        }

        if (testsulyChanged || nameChanged) {
            mUsers.document(userDocumentPath).update("userName", userToSave.getUserName(), "testsuly", userToSave.getTestsuly());
        }

        String oldPassword = oldPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();

        if (!oldPassword.isEmpty() && !newPassword.isEmpty() && !oldPassword.equals(newPassword)) {
            AuthCredential credential = EmailAuthProvider.getCredential(mUser.getEmail(), oldPassword);
            mUser.reauthenticate(credential).addOnSuccessListener(unused -> {
                mUser.updatePassword(newPassword);
                Intent logoutIntent = new Intent(this, MainActivity.class);
                logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(logoutIntent);
            });
        }
    }
}