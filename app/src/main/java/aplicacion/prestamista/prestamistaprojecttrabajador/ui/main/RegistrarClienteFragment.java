package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import aplicacion.prestamista.prestamistaprojecttrabajador.MainActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RegistrarClienteFragment extends Fragment {
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
    private FragmentManager fragmentManager;

    public RegistrarClienteFragment(String uid, FragmentManager fragmentManager) {
        this.uid = uid;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_registrar_cliente, container, false);
        prestamista = FirebaseDatabase.getInstance().getReference();
        editTextFecha = view.findViewById(R.id.editTextFecha);
        editTextValor = view.findViewById(R.id.editTextValor);
        editTextNombre = view.findViewById(R.id.editTextNombre);
        editTextDireccion = view.findViewById(R.id.editTextDireccion);
        editTextTelefono = view.findViewById(R.id.editTextTelefono);
        editTextCuota = view.findViewById(R.id.editTextCuota);
        editTextNumeroCuotas = view.findViewById(R.id.editTextNumCuotas);
        buttonRegistrar = view.findViewById(R.id.buttonRegistrarCliente);
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
        return view;
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
        newFragment.show(fragmentManager, "datePicker");
    }

    /**
     * Método que me permite registar un cliente en la base de datos asociado al trabajador el cual
     * esta autenticado
     */
    private void registrarClientes() {
        try {
            Cliente cliente = obtenerCliente();
            prestamista.child("trabajadores").child(uid).child("clientes")
                    .child(cliente.getClienteId()).setValue(cliente);
            Toast.makeText(getContext(), "Cliente registrado", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Método que se encarga de obtener los valores ingresados por el usuario convirtiendolo en un objeto
     * Cliente, este método es requerido por el método registrarClientes para registrar ese respectivo Cliente
     *
     * @return un objeto Cliente si los campos no están vacios
     * @throws Exception lanza la excepción cuando los campos no fueron ingresados por el usuario y
     *                   el oprime el botón registrar
     */
    private Cliente obtenerCliente() throws Exception {
        String nombre = editTextNombre.getText().toString();
        String direccion = editTextDireccion.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty()
                || editTextFecha.getText().toString().isEmpty() || editTextCuota.getText().toString().isEmpty()
                || editTextValor.getText().toString().isEmpty() || editTextNumeroCuotas.getText().toString().isEmpty()) {
            throw new Exception("Los campos no pueden estar vacios");

        }
        Date fecha = ParseFecha(editTextFecha.getText().toString());
        double valorPrestado = Double.parseDouble(editTextValor.getText().toString());
        double valorCuota = Double.parseDouble(editTextCuota.getText().toString());
        int numCuotas = Integer.parseInt(editTextNumeroCuotas.getText().toString());
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
