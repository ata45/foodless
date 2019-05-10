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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class userRegister extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegisIndividu;
    private EditText editNamaRegis;
    private EditText editAddressRegis;
    private EditText editNoTelpRegis;
    private EditText editEmailRegis;
    private EditText editPasswordRegis;
    private TextView textSignIn;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);

        buttonRegisIndividu = findViewById(R.id.btn_register);

        editNamaRegis = findViewById(R.id.edit_nama_regis);
        editAddressRegis = findViewById(R.id.edit_address_regis);
        editNoTelpRegis = findViewById(R.id.edit_notelp_regis);
        editEmailRegis = findViewById(R.id.edit_email_regis);
//        editPasswordRegis = findViewById(R.id.edit_password_regis);
//
//        textSignIn = findViewById(R.id.txt_sign_in);

        buttonRegisIndividu.setOnClickListener(this);
        textSignIn.setOnClickListener(this);
    }

    private void registerUser(){
        String nama = editNamaRegis.getText().toString().trim();
        String address = editAddressRegis.getText().toString().trim();
        String notelp = editNoTelpRegis.getText().toString().trim();
        String email = editEmailRegis.getText().toString().trim();
        String password = editPasswordRegis.getText().toString().trim();

        if(TextUtils.isEmpty(nama)){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(address)){
            Toast.makeText(this, "Please enter your address", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(notelp)){
            Toast.makeText(this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter your e-mail", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }

        if(TextUtils.isEmpty(password)){
            //password is empty
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return;
            //stopping the fuction execution further
        }

        //if validations are accepted, show a progress bar first
        progressDialog.setMessage("Registering User...");
        progressDialog.show();

//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            //user is successfully registered and logged in
//                            Toast.makeText(this, "Successfully Registered", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(this, "Could not register. Please try again", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
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

