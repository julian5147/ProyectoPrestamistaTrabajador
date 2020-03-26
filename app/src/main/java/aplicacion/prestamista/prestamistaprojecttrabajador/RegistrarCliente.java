package aplicacion.prestamista.prestamistaprojecttrabajador;

import androidx.appcompat.app.AppCompatActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.DatePickerFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RegistrarCliente extends AppCompatActivity {

    private EditText editTextFecha;
    private EditText editTextValor;
    private EditText editTextNombre;
    private EditText editTextDireccion;
    private EditText editTextTelefono;
    private EditText editTextCuota;
    private EditText editTextNumeroCuotas;
    private Button buttonRegistrar;
    private String uid;
    private DatabaseReference prestamista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prestamista = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_registrar_cliente);
        editTextFecha = findViewById(R.id.editTextFecha);
        editTextValor = findViewById(R.id.editTextValor);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextDireccion = findViewById(R.id.editTextDireccion);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextCuota = findViewById(R.id.editTextCuota);
        editTextNumeroCuotas = findViewById(R.id.editTextNumCuotas);
        buttonRegistrar = findViewById(R.id.buttonRegistrarCliente);
        Intent intent = getIntent();
        uid = (String) intent.getSerializableExtra("uid");
        editTextFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        buttonRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarClientes();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                final String selectedDate = day + " / " + (month + 1) + " / " + year;
                editTextFecha.setText(selectedDate);
            }

        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private void registrarClientes() {
        try {
            Cliente cliente = obtenerCliente();
            prestamista.child("trabajadores").child(uid).child("clientes")
                    .child(cliente.getClienteId()).setValue(cliente);
            Toast.makeText(RegistrarCliente.this, "Cliente registrado", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private Cliente obtenerCliente() throws Exception {
        Date fecha = ParseFecha(editTextFecha.getText().toString());
        double valorPrestado = Double.parseDouble(editTextValor.getText().toString());
        String nombre = editTextNombre.getText().toString();
        String direccion = editTextDireccion.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        double valorCuota = Double.parseDouble(editTextCuota.getText().toString());
        int numCuotas = Integer.parseInt(editTextNumeroCuotas.getText().toString());
        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(direccion) || TextUtils.isEmpty(telefono)
                || fecha == null || valorCuota == 0 || valorPrestado == 0 || numCuotas == 0) {
            throw new Exception("Los campos no pueden estar vacios");
        }
        String clienteId = prestamista.push().getKey();
        Cliente cliente = Cliente.builder(clienteId, nombre).fecha(fecha).valorPrestado(valorPrestado)
                .direccion(direccion).telefono(telefono).valorCuota(valorCuota).numeroCuotas(numCuotas).build();
        return cliente;
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
