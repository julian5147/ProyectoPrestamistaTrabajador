package aplicacion.prestamista.prestamistaprojecttrabajador;

import androidx.appcompat.app.AppCompatActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cuota;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.DatePickerFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que me permite controlar la interfaz donde ingreso la fecha y el valor de la cuota previamente
 * elegida, además implementar el registro de la cuota en la base de datos en firebase
 *
 * @author Julián Salgado
 * @version 1.0
 */
public class CuotaActivity extends AppCompatActivity {

    private EditText editTextFecha;
    private EditText editTextValorPagadoCuota;
    private TextView textViewTitulo;
    private Button buttonRegistrarCuota;
    private DatabaseReference prestamista;
    private String uid;
    private String clienteId;
    private String titulo;
    private String cuotaId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuota);
        textViewTitulo = findViewById(R.id.titleCuota);
        prestamista = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        clienteId = intent.getStringExtra("clienteId");
        cuotaId = intent.getStringExtra("cuotaId");
        titulo = intent.getStringExtra("titulo");
        editTextFecha = findViewById(R.id.editTextFechaCuota);
        editTextValorPagadoCuota = findViewById(R.id.editTextValorPagadoCuota);
        buttonRegistrarCuota = findViewById(R.id.buttonRegsitrarCuota);
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        buttonRegistrarCuota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCuota();
            }
        });
        textViewTitulo.setText(titulo);
    }

    /**
     * Método que me permite mostrar el calendario al usuario para que este pueda elegir la fecha en
     * el cuál la cuota fue pagada evitando cambios de formato a la hora de ingresar la fecha
     */
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                editTextFecha.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
        System.out.println(titulo);
    }

    /**
     * Método que se encarga de registrar la cuota elegida previamente, en la base de datos como
     * una lista de cuotas
     */
    private void registrarCuota() {
        try {
            Cuota cuota = obtenerCuota();
            prestamista.child("trabajadores").child(uid).child("clientes")
                    .child(clienteId).child("Cuotas").child(cuota.getCuotaId()).setValue(cuota);
            Toast.makeText(CuotaActivity.this, "Cuota registrada", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(CuotaActivity.this, MainActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            Toast.makeText(CuotaActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Método que se encarga de obtener los valores ingresados por el usuario convirtiendolo en un objeto
     * Cuota, este método es requerido por el método registrarCuota para registrar esa respectiva cuota
     *
     * @return un objeto Cuota si los campos no están vacios
     * @throws Exception lanza la excepción cuando los campos no fueron ingresados por el usuario y
     *                   el oprime el botón registrar cuota
     */
    private Cuota obtenerCuota() throws Exception {
        Date fecha = ParseFecha(editTextFecha.getText().toString());
        double valorPagado = Double.parseDouble(editTextValorPagadoCuota.getText().toString());
        if (fecha == null || valorPagado == 0) {
            throw new Exception("Los campos no pueden estar vacios");
        }
        prestamista.push();
        Cuota cuota = new Cuota(cuotaId, fecha, valorPagado);
        return cuota;
    }

    /**
     * Permite convertir un String en fecha (Date).
     *
     * @param fecha Cadena de fecha dd/MM/yyyy
     * @return Objeto Date
     */
    private static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("dd / MM / yyyy");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fecha);
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
    }

}
