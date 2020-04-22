package aplicacion.prestamista.prestamistaprojecttrabajador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

public class RestablecerPasswordActivity extends AppCompatActivity {

    private TextInputLayout textInputLayoutEmail;
    private Button buttonRestablecerPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restablecer_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Restablecer Contraseña");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        textInputLayoutEmail = findViewById(R.id.TxtEmailRestablecer);
        buttonRestablecerPassword = findViewById(R.id.buttonRestablecerPassword);
        buttonRestablecerPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = textInputLayoutEmail.getEditText().getText().toString().trim();
        if (!validarEmail(email)) {
            return;
        }
        progressDialog.setMessage("Espere un momento...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        firebaseAuth.setLanguageCode("es");
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RestablecerPasswordActivity.this, "Se ha enviado un correo para restablecer tu contraseña",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RestablecerPasswordActivity.this, Authentication.class);
                    startActivity(intent);
                    finish();
                } else {
                    translate(task.getException().getMessage()).addOnSuccessListener(new OnSuccessListener<String>() {
                        @Override
                        public void onSuccess(String s) {
                            Toast.makeText(RestablecerPasswordActivity.this, s, Toast.LENGTH_LONG).show();
                        }
                    });
                }
                progressDialog.dismiss();
            }
        });
    }

    private boolean validarEmail(String email) {

        if (TextUtils.isEmpty(email)) {
            textInputLayoutEmail.setError("Se debe ingresar un email");
            return false;
        } else {
            textInputLayoutEmail.setError(null);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Task<String> translate(final String text) {
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(FirebaseTranslateLanguage.EN)
                .setTargetLanguage(FirebaseTranslateLanguage.ES)
                .build();
        final FirebaseTranslator translator =
                FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .requireWifi()
                .build();
        return translator.downloadModelIfNeeded(conditions).continueWithTask(
                new Continuation<Void, Task<String>>() {
                    @Override
                    public Task<String> then(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            return translator.translate(text);
                        } else {
                            Exception e = task.getException();
                            if (e == null) {
                                e = new Exception(getApplication().getString(R.string.unknown_error));
                            }
                            return Tasks.forException(e);
                        }
                    }
                });
    }
}
