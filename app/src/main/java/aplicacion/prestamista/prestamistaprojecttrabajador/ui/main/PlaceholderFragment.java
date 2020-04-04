package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import aplicacion.prestamista.prestamistaprojecttrabajador.CuotaActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cuota;

/**
 * Es el fragment el cual se crea por cada cliente registrardo, me permite visualizar la información
 * de cada cliente con su respectivo tab
 */
public class PlaceholderFragment extends Fragment {

    private Cliente cliente;
    private ArrayList<String> cuotas;
    String uid;
    private DatabaseReference prestamista;

    public PlaceholderFragment(Cliente cliente, String uid) {
        this.cliente = cliente;
        this.cuotas = new ArrayList<>();
        this.uid = uid;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textViewFecha = root.findViewById(R.id.textViewFecha);
        final TextView textViewValor = root.findViewById(R.id.textViewValor);
        final TextView textViewNombre = root.findViewById(R.id.textViewNombre);
        final TextView textViewDireccion = root.findViewById(R.id.textViewDireccion);
        final TextView textViewTelefono = root.findViewById(R.id.textViewTelefono);
        final TextView textViewCuota = root.findViewById(R.id.textViewCuota);
        final ListView listViewCuotas = root.findViewById(R.id.listViewCuotas);
        final FloatingActionButton eliminarClient = root.findViewById(R.id.floatingActionButton);
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        adaptarListView(formato);
        listViewCuotas.setItemsCanFocus(true);
        textViewFecha.setText(formato.format(cliente.getFecha()));
        textViewValor.setText(String.valueOf(cliente.getValorPrestado()));
        textViewNombre.setText(cliente.getNombre());
        textViewDireccion.setText(cliente.getDireccion());
        textViewTelefono.setText(cliente.getTelefono());
        textViewCuota.setText(String.valueOf(cliente.getValorCuota()));
        CuotaAdapter cuotaAdapter = new CuotaAdapter(getContext(), cuotas);
        listViewCuotas.setAdapter(cuotaAdapter);
        onClick(listViewCuotas);
        eliminarClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarCliente();
            }
        });
        return root;
    }

    private void eliminarCliente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Estas seguro que deseas eliminar este cliente?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                prestamista = FirebaseDatabase.getInstance().getReference();
                prestamista.child("trabajadores").child(uid).child("clientes")
                        .child(cliente.getClienteId()).removeValue();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.create().show();
    }

    /**
     * Adapta un list view para que se visualicen las cuotas, ya sea que ya hayan sido registradas o
     * no, el método es solicitado por el onCreateView
     *
     * @param formato de la fecha para visualizarlo de está manera dd/MM/yyyy
     */
    private void adaptarListView(SimpleDateFormat formato) {
        cuotas.clear();
        for (int i = 1; i <= cliente.getNumeroCuotas(); i++) {
            if (!cliente.getCuotas().isEmpty()) {
                if (i <= cliente.getCuotas().size()) {
                    Cuota cuota = cliente.getCuotas().get((i - 1));
                    cuotas.add("Cuota " + i + "\nFecha: " + formato.format(cuota.getFecha()) + "\nValor: "
                            + cuota.getValorPagado());
                } else {
                    cuotas.add("Cuota " + i);
                }
            } else {
                cuotas.add("Cuota " + i);
            }
        }
    }

    /**
     * Método que permite escuchar cuando el usuario hace click en algún item del listView de manera que este
     * navegue al CuotaActvity para registrar la cuota elegida
     *
     * @param listViewCuotas hace referencia al listView el cuál se debe escuchar el evento onClick
     */
    private void onClick(ListView listViewCuotas) {
        listViewCuotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent1 = new Intent(getContext(), CuotaActivity.class);
                intent1.putExtra("clienteId", cliente.getClienteId());
                intent1.putExtra("uid", uid);
                intent1.putExtra("cuotaId", "" + (i + 1));
                intent1.putExtra("titulo", "Cuota " + (i + 1));
                startActivity(intent1);
            }
        });
    }
}