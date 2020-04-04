package aplicacion.prestamista.prestamistaprojecttrabajador.ui.main;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import aplicacion.prestamista.prestamistaprojecttrabajador.R;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> listaDeFragmentos = new ArrayList<>();
    private final List<String> listaDeTitulosDeFragmentos = new ArrayList<>();

    public SectionsPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return listaDeFragmentos.get(position);
    }

    @Override
    public int getCount() {
        return listaDeFragmentos.size();
    }

    public void agregarFragmento(Fragment fragment, String title) {
        listaDeFragmentos.add(fragment);
        listaDeTitulosDeFragmentos.add(title);
    }

    // Si es el título de la última pestaña, regresamos null, lo
    // cual regresará el icono únicamente
    @Override
    public CharSequence getPageTitle(int position) {
        return listaDeTitulosDeFragmentos.get(position);
    }

    public List<String> getListaDeTitulosDeFragmentos() {
        return listaDeTitulosDeFragmentos;
    }
}
