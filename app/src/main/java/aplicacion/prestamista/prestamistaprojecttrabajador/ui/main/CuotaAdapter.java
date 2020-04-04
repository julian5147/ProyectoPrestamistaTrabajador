package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;

/**
 * Clase que me permite adaptar un layout.xml, con el objetivo de listar todas la cuotas
 * que cada cliente tiene que pagar
 */
public class CuotaAdapter extends ArrayAdapter<String> {

    private ArrayList<String> cuotas;
    private Context context;

    public CuotaAdapter(Context context, ArrayList<String> cuotas) {
        super(context, R.layout.cuota_layout, cuotas);
        this.cuotas = cuotas;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cuota_layout,parent,false);
        String cuota = this.cuotas.get(position);
        TextView textViewCuota =  view.findViewById(R.id.textViewCuota);
        textViewCuota.setText(cuota);
        return view;
    }
}
