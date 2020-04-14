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
public class SectionsPagerAdapterInit extends FragmentPagerAdapter {

    private List<Fragment> listaDeFragmentos = new ArrayList<>();
    private List<String> listaDeTitulosDeFragmentos = new ArrayList<>();

    public SectionsPagerAdapterInit(FragmentManager manager) {
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

    @Override
    public CharSequence getPageTitle(int position) {
        return listaDeTitulosDeFragmentos.get(position);
    }

    public List<Fragment> getListaDeFragmentos() {
        return listaDeFragmentos;
    }

    public List<String> getListaDeTitulosDeFragmentos() {
        return listaDeTitulosDeFragmentos;
    }

    public void setListaDeFragmentos(List<Fragment> listaDeFragmentos) {
        this.listaDeFragmentos = listaDeFragmentos;
    }

    public void setListaDeTitulosDeFragmentos(List<String> listaDeTitulosDeFragmentos) {
        this.listaDeTitulosDeFragmentos = listaDeTitulosDeFragmentos;
    }
}
