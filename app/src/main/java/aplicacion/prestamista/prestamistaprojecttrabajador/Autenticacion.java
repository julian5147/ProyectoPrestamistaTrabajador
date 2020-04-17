package aplicacion.prestamista.prestamistaprojecttrabajador;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Clase que me permite controlar la interfaz de autenticación, además programar el comprotamiento
 * a la hora de registrar e inicar sesión
 *
 * @author Julián Salgado
 * @version 1.0
 */
public class Autenticacion extends AppCompatActivity {

    //defining view objects
    private TextInputLayout TextEmail;
    private TextInputLayout TextPassword;
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
        btnRegistrar = (Button) findViewById(R.id.botonRegistrar);
        btnLogin = (Button) findViewById(R.id.botonLogin);

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

        if(!validarEmail(email) | !validarContraseña(password)){
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

                            Toast.makeText(Autenticacion.this, "Se ha registrado el usuario con el email: " + TextEmail.getEditText().getText(), Toast.LENGTH_LONG).show();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(Autenticacion.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Autenticacion.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
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

        if(!validarEmail(email) | !validarContraseña(password)){
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
                            Toast.makeText(Autenticacion.this, "Bienvenido: " + TextEmail.getEditText().getText(), Toast.LENGTH_LONG).show();
                            trabajador = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = trabajador.getUid();
                            Intent intencion = new Intent(getApplication(), MainActivity.class);
                            intencion.putExtra("uid", uid);
                            startActivity(intencion);
                            finish();

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {//si se presenta una colisión
                                Toast.makeText(Autenticacion.this, "Ese usuario ya existe ", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Autenticacion.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });

    }


}
