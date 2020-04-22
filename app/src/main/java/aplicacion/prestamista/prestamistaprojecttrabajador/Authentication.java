package aplicacion.prestamista.prestamistaprojecttrabajador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

/**
 * Clase que me permite controlar la interfaz de autenticación, además programar el comprotamiento
 * a la hora de registrar e inicar sesión
 *
 * @author Julián Salgado
 * @version 1.0
 */
public class Authentication extends AppCompatActivity {

    //defining view objects
    private TextInputLayout TextEmail;
    private TextInputLayout TextPassword;
    private TextView textViewRestablecerContraseña;
    private Button btnRegistrar, btnLogin;
    private ProgressDialog progressDialog;
    //Declaramos un objeto firebaseAuth
    private FirebaseAuth firebaseAuth;
    private FirebaseUser trabajador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autenticacion);
        //inicializamos el objeto firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();
        trabajador = FirebaseAuth.getInstance().getCurrentUser();
        if (trabajador != null) {
            String uid = trabajador.getUid();
            Intent intencion = new Intent(getApplication(), MainActivity.class);
            intencion.putExtra("uid", uid);
            startActivity(intencion);
            finish();
        }
        //Referenciamos los views
        TextEmail = findViewById(R.id.TxtEmail);
        TextPassword = findViewById(R.id.TxtPassword);
        btnRegistrar = (Button) findViewById(R.id.buttonRegistrar);
        btnLogin = (Button) findViewById(R.id.buttonLogin);
        textViewRestablecerContraseña = findViewById(R.id.textViewRestablecerContraseña);
        textViewRestablecerContraseña.setPaintFlags(textViewRestablecerContraseña.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewRestablecerContraseña.setText("¿Olvidaste tu contraseña?");
        progressDialog = new ProgressDialog(this);

        //asociamos un oyente al evento clic del botón
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loguearUsuario();
            }
        });
        textViewRestablecerContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Authentication.this, RestablecerPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Método que se encarga de registrar un usuario y validar que los campos no esten vacios,
     * mostrando sus respectivos mensajes al usuario, este método es utilizado en el onClick del
     * botón de registrarse
     */
    private void registrarUsuario() {

        //Obtenemos el email y la contraseña desde las cajas de texto
        String email = TextEmail.getEditText().getText().toString().trim();
        String password = TextPassword.getEditText().getText().toString().trim();

        if (!validarEmail(email) | !validarContraseña(password)) {
            return;
        }

        progressDialog.setMessage("Realizando registro en linea...");
        progressDialog.show();

        //registramos un nuevo usuario
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {

                            Toast.makeText(Authentication.this, "Se ha registrado el usuario con el email: " + TextEmail.getEditText().getText(), Toast.LENGTH_LONG).show();
                        } else {
                            translate(task.getException().getMessage()).addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    Toast.makeText(Authentication.this, s, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        progressDialog.dismiss();
                    }
                });

    }

    private boolean validarEmail(String email) {

        //Verificamos que las cajas de texto no esten vacías
        if (TextUtils.isEmpty(email)) {//(precio.equals(""))
            TextEmail.setError("Se debe ingresar un email");
            return false;
        } else {
            TextEmail.setError(null);
            return true;
        }
    }

    private boolean validarContraseña(String password) {
        if (TextUtils.isEmpty(password)) {
            TextPassword.setError("Falta ingresar la contraseña");
            return false;
        } else if (password.length() < 8) {
            TextPassword.setError("La contraseña debe ser de maximo 8 caracteres");
            return false;
        } else {
            TextPassword.setError(null);
            return true;
        }
    }

    /**
     * Método que se encarga de verificar si un usuario se puede loguear, además de validar que los campos
     * no esten vacios, mostrando sus respectivos mensajes al usuario, este método es utilizado en el onClick del
     * botón de inciar sesión
     */
    private void loguearUsuario() {
        //Obtenemos el email y la contraseña desde las cajas de texto
        final String email = TextEmail.getEditText().getText().toString().trim();
        String password = TextPassword.getEditText().getText().toString().trim();

        if (!validarEmail(email) | !validarContraseña(password)) {
            return;
        }

        progressDialog.setMessage("Realizando consulta en linea...");
        progressDialog.show();

        //loguear usuario
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if (task.isSuccessful()) {
                            Toast.makeText(Authentication.this, "Bienvenido: " + TextEmail.getEditText().getText(), Toast.LENGTH_LONG).show();
                            trabajador = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = trabajador.getUid();
                            Intent intencion = new Intent(getApplication(), MainActivity.class);
                            intencion.putExtra("uid", uid);
                            startActivity(intencion);
                            finish();

                        } else {
                            translate(task.getException().getMessage()).addOnSuccessListener(new OnSuccessListener<String>() {
                                @Override
                                public void onSuccess(String s) {
                                    Toast.makeText(Authentication.this, s, Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                        progressDialog.dismiss();
                    }
                });

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
