package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import aplicacion.prestamista.prestamistaprojecttrabajador.CuotaActivity;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cuota;

/**
 * Es el fragment el cual se crea por cada cliente registrardo, me permite visualizar la información
 * de cada cliente con su respectivo tab
 */
public class PlaceholderFragmentInicial extends Fragment {

    private static final int REQUEST_CALL = 1;
    private Cliente cliente;
    private ArrayList<String> cuotas;
    String uid;
    private DatabaseReference prestamista;
    private TextView textViewFecha;
    private TextView textViewValor;
    private TextView textViewNombre;
    private TextView textViewDireccion;
    private TextView textViewTelefono;
    private TextView textViewCuota;
    private ListView listViewCuotas;
    private ImageButton eliminarClient;
    private ImageButton llamarClient;

    public PlaceholderFragmentInicial(Cliente cliente, String uid) {
        this.cliente = cliente;
        this.cuotas = new ArrayList<>();
        this.uid = uid;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        textViewFecha = root.findViewById(R.id.textViewFecha);
        textViewValor = root.findViewById(R.id.textViewValor);
        textViewNombre = root.findViewById(R.id.textViewNombre);
        textViewDireccion = root.findViewById(R.id.textViewDireccion);
        textViewTelefono = root.findViewById(R.id.textViewTelefono);
        textViewCuota = root.findViewById(R.id.textViewCuota);
        listViewCuotas = root.findViewById(R.id.listViewCuotas);
        eliminarClient = root.findViewById(R.id.eliminar);
        llamarClient = root.findViewById(R.id.llamar);
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
        llamarClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llamarCliente();
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

    private void llamarCliente() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setTitle("Confirmar");
        builder.setMessage("¿Estas seguro que deseas llamar este cliente?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                } else {
                    String dial = "tel:" + cliente.getTelefono();
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CALL){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                llamarCliente();
            }else {
                Toast.makeText(getContext(),"Permission DENIED",Toast.LENGTH_LONG).show();
            }
        }
    }
}