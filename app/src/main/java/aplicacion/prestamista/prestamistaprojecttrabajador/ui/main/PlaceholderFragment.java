package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
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
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textViewFecha.setText(s.getFecha().toString());
                //textViewValor.setText(String.valueOf(s.getValorPrestado()));
                //textViewNombre.setText(s.getNombre());
                //textViewDireccion.setText(s.getDireccion());
                //textViewTelefono.setText(s.getTelefono());
                //textViewCuota.setText(String.valueOf(s.getValorCuota()));
                textViewDireccion.setText(s);
            }
        });
        return root;
    }
}