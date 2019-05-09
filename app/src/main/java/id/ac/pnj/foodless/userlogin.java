package id.ac.pnj.foodless;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class userlogin extends AppCompatActivity implements View.OnClickListener {

    private Button buttonLogin, buttonRegister;
    private EditText editTextEmail, editTextPassword;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //Home Activity here
            startActivity(new Intent(userlogin.this, userProfile.class));
        }

        editTextEmail = findViewById(R.id.edit_email_login);
        editTextPassword = findViewById(R.id.edit_password_login);
        buttonLogin = findViewById(R.id.btn_login);
        buttonRegister = findViewById(R.id.btn_register);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        progressBar = findViewById(R.id.progressBar);
        progressDialog = new ProgressDialog(this);
    }

    private void userLogin(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging In");
        progressDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonLogin){
            String email = editTextEmail.getText().toString().trim();
            final String password = editTextPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(getApplicationContext(), "Please enter your e-mail", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password))
            {
                Toast.makeText(getApplicationContext(), "Please enter your password!", Toast.LENGTH_SHORT).show();
                return;
            } else if (password.length() < 8)
            {
                Toast.makeText(getApplicationContext(), "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(userlogin.this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful())
                    {
                        // there was an error
                        if (password.length() < 8)
                        {
                            editTextPassword.setError(getString(R.string.minimum_password));
                        } else
                        {
                            Toast.makeText(userlogin.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    } else
                    {
                        checkIfEmailVerified();
                    }
                }
            });
        }

        if(v == buttonRegister){
            finish();
            startActivity(new Intent(this, userRegister.class));
        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            finish();
            Toast.makeText(userlogin.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, userProfile.class));
            finish();
        }
        else if (!user.isEmailVerified())
        {

            Toast.makeText(userlogin.this, "Akun belum terverifikasi, Cek Email anda untuk verifikasi", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();

        }

        else
        {
            Toast.makeText(userlogin.this, "Terjadi Error Sementara", Toast.LENGTH_SHORT).show();
        }

        // email is not verified, so just prompt the message to the user and restart this activity.
        // NOTE: don't forget to log out the user.

        //restart this activity
    }
}
