package aplicacion.prestamista.prestamistaprojecttrabajador;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;
import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cuota;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.PlaceholderFragmentInicial;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.RegistrarClienteFragment;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.SectionsPagerAdapterInit;

/**
 * Clase que me permite consultar y visualizar la información los respectivos clientes
 * del trabajador autenticado
 */
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private String uid;
    private List<Cliente> clientes;
    private DatabaseReference prestamista;
    private SectionsPagerAdapterInit sectionsPagerAdapterInit;
    private ViewPager viewPager;
    private ProgressDialog progressDialog;
    private FrameLayout frameLayout;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Clientes");
        frameLayout = findViewById(R.id.container);
        clientes = new ArrayList<>();
        prestamista = FirebaseDatabase.getInstance().getReference();
        prestamista.keepSynced(true);
        final Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        sectionsPagerAdapterInit = new SectionsPagerAdapterInit(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapterInit);
        progressDialog = new ProgressDialog(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        getClientesFromFirebase();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_add:
                        toolbar.setTitle("Registrar Cliente");
                        RegistrarClienteFragment registrarClienteFragment = new RegistrarClienteFragment(uid, getSupportFragmentManager());
                        openFragment(registrarClienteFragment);
                        frameLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        toolbar.getMenu().getItem(0).setVisible(false);
                        return true;
                    case R.id.navigation_users:
                        toolbar.setTitle("Clientes");
                        frameLayout.setVisibility(View.INVISIBLE);
                        getClientesFromFirebase();
                        viewPager.setVisibility(View.VISIBLE);
                        toolbar.getMenu().getItem(0).setVisible(true);
                        return true;
                    case R.id.navigation_gastos:
                        toolbar.setTitle("Registrar Gastos");
                        GastosFragment gastosFragment = new GastosFragment(uid);
                        openFragment(gastosFragment);
                        frameLayout.setVisibility(View.VISIBLE);
                        viewPager.setVisibility(View.INVISIBLE);
                        toolbar.getMenu().getItem(0).setVisible(false);
                        return true;
                }
                return false;
            }

        });

    }

    /**
     * Método que se encarga de remplazar el fragmento en el cual se esta mostrando en pantalla
     * por el que se selecciona en el menu
     *
     * @param fragment el cual es creado para que sea mostrado en la vista
     */
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * método que se encarga de visualizar dinámicamente todos los clientes que tiene ese trabjador
     * en un fragment con su respectivo tab
     */
    private void agregarTabs() {
        SectionsPagerAdapterInit sectionsPagerAdapterInit = new SectionsPagerAdapterInit(getSupportFragmentManager());
        for (Cliente cliente : clientes) {
            sectionsPagerAdapterInit.agregarFragmento(new PlaceholderFragmentInicial(cliente, uid), cliente.getNombre());
        }
        viewPager.setAdapter(sectionsPagerAdapterInit);
    }

    /**
     * Método que me permite obtener lo clientes de la base de dato en firebase
     * de su respectivo trabajador
     */
    private void getClientesFromFirebase() {
        progressDialog.setMessage("Realizando consulta en linea...");
        progressDialog.show();

        Query myClienteQuery = prestamista.child("trabajadores").child(uid)
                .child("clientes");
        myClienteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    clientes.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String clienteId = ds.child("clienteId").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        Date fecha = ds.child("fecha").getValue(Date.class);
                        double valorPrestado = Double.parseDouble(ds.child("valorPrestado").getValue().toString());
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        double valorCuota = Double.parseDouble(ds.child("valorCuota").getValue().toString());
                        int numeroCuotas = Integer.parseInt(ds.child("numeroCuotas").getValue().toString());
                        List<Cuota> cuotas = new ArrayList<>();
                        if (ds.child("Cuotas").exists()) {
                            for (DataSnapshot cuota : ds.child("Cuotas").getChildren()) {
                                String cuotaId = cuota.child("cuotaId").getValue().toString();
                                Date fechaCuota = cuota.child("fecha").getValue(Date.class);
                                double valorPagado = Double.parseDouble(cuota.child("valorPagado").getValue().toString());
                                Cuota cuotaRegistrada = new Cuota(cuotaId, fechaCuota, valorPagado);
                                cuotas.add(cuotaRegistrada);
                            }
                        }
                        Cliente cliente = Cliente.builder(clienteId, nombre).fecha(fecha).valorPrestado(valorPrestado)
                                .direccion(direccion).telefono(telefono).valorCuota(valorCuota).numeroCuotas(numeroCuotas)
                                .cuotas(cuotas).build();
                        clientes.add(cliente);
                    }
                    agregarTabs();
                    registrarTotalPlataPrestada(clientes);

                } else {
                    Toast.makeText(MainActivity.this, "No tiene clientes registrados", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fallo la lectura: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.overflow, menu);
        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cerrar:
                firebaseAuth.signOut();
                Intent intent2 = new Intent(MainActivity.this, Autenticacion.class);
                startActivity(intent2);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private double calcularTotalPlataPrestada(List<Cliente> clientes) {
        double totalPlataPrestada = 0;

        for (Cliente cliente : clientes) {
            totalPlataPrestada += (cliente.getValorCuota() * ( cliente.getNumeroCuotas() - cliente.getCuotas().size()));
        }

        return totalPlataPrestada;
    }

    private void registrarTotalPlataPrestada(List<Cliente> clientes) {
        prestamista.child("trabajadores").child(uid).child("TotalPlataPrestada")
                .setValue(calcularTotalPlataPrestada(clientes));

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        buscar(s);
        return false;
    }

    private void buscar(final String nombreCliente) {
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getNombre().toLowerCase().contains(nombreCliente.toLowerCase())) {
                viewPager.setCurrentItem(i);
            }
        }
    }

}