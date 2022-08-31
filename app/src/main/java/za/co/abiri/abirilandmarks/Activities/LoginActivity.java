package za.co.abiri.abirilandmarks.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import za.co.abiri.abirilandmarks.R;

public class LoginActivity extends AppCompatActivity {

    //Declarations
    private EditText userEmail,userPassword;
    private Button loginBtn;
    private ProgressBar loginProgress;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private ImageView loginPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Items in xml file
        userEmail = findViewById(R.id.login_email);
        userPassword = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.loginButton);
        loginProgress = findViewById(R.id.login_progress);
        mAuth = FirebaseAuth.getInstance();
        //Referencing HomeActivity //Later changed to Home.class
        HomeActivity = new Intent(this, za.co.abiri.abirilandmarks.Activities.Home.class);

        //Photo
        loginPhoto = findViewById(R.id.login_photo);
        loginPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent registerActivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registerActivity);
                finish();
            }
        });

        loginProgress.setVisibility(View.INVISIBLE);

        //Makes button invisible but progressbar visible
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginProgress.setVisibility(View.VISIBLE);
                loginBtn.setVisibility(View.INVISIBLE);

                final String email = userEmail.getText().toString();
                final String password = userPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Please verify all fields");
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
                }
                else
                {
                  //Sign in method with parameters
                  signIn(email,password);
                }

            }
        });


    }

    //CREATED METHODS
    //Sign-in method
    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    loginProgress.setVisibility(View.INVISIBLE);
                    loginBtn.setVisibility(View.VISIBLE);
                    //Update UI similar to RegisterActivity
                    updateUI();

                }
                else
                    showMessage(task.getException().getMessage());
                    loginBtn.setVisibility(View.VISIBLE);
                    loginProgress.setVisibility(View.INVISIBLE);
            }
        });

    }

    //Method to Move to New Activity
    private void updateUI() {
        startActivity(HomeActivity);
        finish();

    }

    //Method to show message
    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }


    //Override Method for Firebase User details
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //If the user is already logged in, need to redirect to HomeActivity
            updateUI();
        }
    }
}