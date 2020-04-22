package aplicacion.prestamista.prestamistaprojecttrabajador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class GastosFragment extends Fragment {

    private TextInputLayout editTextPagos;
    private TextInputLayout editTextGasolina;
    private TextInputLayout editTextPinchazos;
    private TextInputLayout editTextMantenimiento;
    private String uid;
    private Button buttonRegistrarGastos;
    private DatabaseReference prestamista;

    public GastosFragment(String uid) {
        this.uid = uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gastos, container, false);
        prestamista = FirebaseDatabase.getInstance().getReference();
        prestamista.keepSynced(true);
        editTextPagos = view.findViewById(R.id.editTextPagos);
        editTextGasolina = view.findViewById(R.id.editTextGasolina);
        editTextPinchazos = view.findViewById(R.id.editTextPinchazos);
        editTextMantenimiento = view.findViewById(R.id.editTextMantenimiento);
        buttonRegistrarGastos = view.findViewById(R.id.buttonRegsitrarGastos);
        buttonRegistrarGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGastosDiarios();
            }
        });
        return view;
    }

    /**
     * Método que me permite calcular los gastos de cada trabajador previamente ingresados por el
     * mismo
     *
     * @return el valor total de los gastos diarios que ingreso
     */
    private double obtenerGastos() {
        double totalGastos = 0;
        double pagos = editTextPagos.getEditText().getText().toString().isEmpty() ? 0 :
                Double.parseDouble(editTextPagos.getEditText().getText().toString());
        double gasolina = editTextGasolina.getEditText().getText().toString().isEmpty() ? 0 :
                Double.parseDouble(editTextGasolina.getEditText().getText().toString());
        double pinchazos = editTextPinchazos.getEditText().getText().toString().isEmpty() ? 0 :
                Double.parseDouble(editTextPinchazos.getEditText().getText().toString());
        double mantenimiento = editTextMantenimiento.getEditText().getText().toString().isEmpty() ? 0 :
                Double.parseDouble(editTextMantenimiento.getEditText().getText().toString());
        totalGastos = pagos + gasolina + pinchazos + mantenimiento;
        return totalGastos;
    }

    /**
     * Método que se encarga de registrar los gastos diarios del trabajador previamente ingresados
     */
    private void registrarGastosDiarios() {
        double totalGastosDiarios = obtenerGastos();
        prestamista.child("trabajadores").child(uid).child("TotalGastosDiario")
                .setValue(totalGastosDiarios);
        Toast.makeText(getContext(), "Gastos Diario Registrados", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra("uid", uid);
        startActivity(intent);
    }
}
