package id.ac.pnj.foodless;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

public class userRegister extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegisIndividu;
    private EditText editNamaRegis, editAddressRegis, editNoTelpRegis, editEmailRegis, editPasswordRegis;
    private TextView textSignIn;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress_regis);

        buttonRegisIndividu = findViewById(R.id.btn_register);

        editNamaRegis = findViewById(R.id.edit_nama_regis);
        editAddressRegis = findViewById(R.id.edit_address_regis);
        editNoTelpRegis = findViewById(R.id.edit_notelp_regis);
        editEmailRegis = findViewById(R.id.edit_email_regis);
        editPasswordRegis = findViewById(R.id.edit_password_regis);

        textSignIn = findViewById(R.id.txt_sign_in);

        buttonRegisIndividu.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if(firebaseAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(userRegister.this, userProfile.class));
        }
    }

    private void registerUser(){
        final String nama = editNamaRegis.getText().toString().trim();
        final String address = editAddressRegis.getText().toString().trim();
        final String notelp = editNoTelpRegis.getText().toString().trim();
        final String email = editEmailRegis.getText().toString().trim();
        final String password = editPasswordRegis.getText().toString().trim();

        if(nama.isEmpty()){
            editNamaRegis.setError(getString(R.string.error_input_nama));
            editNamaRegis.requestFocus();
            return;
        }

        if (address.isEmpty()){
            editAddressRegis.setError(getString(R.string.error_input_address));
            editAddressRegis.requestFocus();
            return;
        }

        if(notelp.isEmpty()){
            editNoTelpRegis.setError(getString(R.string.error_input_phone));
            editNoTelpRegis.requestFocus();
            return;
        }

        if(email.isEmpty()){
            editEmailRegis.setError(getString(R.string.error_input_email));
            editEmailRegis.requestFocus();
            return;
        }

        if(password.isEmpty()){
            editPasswordRegis.setError(getString(R.string.error_input_password));
            editPasswordRegis.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            @SuppressLint("RestrictedApi") User user = new User (nama, email, address, notelp, password);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                    public void onComplete(@NonNull Task<Void> task)
                                    {
                                        progressBar.setVisibility(View.GONE);
                                        if (task.isSuccessful())
                                        {
                                            sendEmailVerif();

                                        } else
                                        {
                                            Toast.makeText(userRegister.this, "Register failed", Toast.LENGTH_LONG).show();
                                        }
                                    }
                            });
                    } else {
                         Toast.makeText(userRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void sendEmailVerif()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            //email sent
                            //after the email is sent, just log out the user and finish this activity
                            firebaseAuth.signOut();
                            Toast.makeText(userRegister.this, getString(R.string.succeed_register), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(userRegister.this, userlogin.class));
                            finish();
                        }
                        else
                        {
                            //email is not sent
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0,0);
                            startActivity(getIntent());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == buttonRegisIndividu){
            registerUser();
        }

        if (v == textSignIn){
            //will open login activity
            finish();
            startActivity(new Intent(this, userlogin.class));

        }
    }
}

