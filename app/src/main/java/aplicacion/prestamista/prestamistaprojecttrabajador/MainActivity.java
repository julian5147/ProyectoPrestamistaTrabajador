package aplicacion.prestamista.prestamistaprojecttrabajador;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import aplicacion.prestamista.prestamistaprojecttrabajador.entities.Cliente;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.PageViewModel;
import aplicacion.prestamista.prestamistaprojecttrabajador.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private String uid;
    private List<Cliente> clientes;
    private DatabaseReference prestamista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prestamista = FirebaseDatabase.getInstance().getReference();
        clientes = new ArrayList<>();
        Intent intent = getIntent();
        uid = (String) intent.getSerializableExtra("uid");
        getClientesFromFirebase();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println(""+clientes.get(0).getDireccion());
                Intent intent = new Intent(MainActivity.this, RegistrarCliente.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });
    }

    private void getClientesFromFirebase() {
        Query myClienteQuery = prestamista.child("trabajadores").child(uid)
                .child("clientes");
        myClienteQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String clienteId = ds.child("clienteId").getValue().toString();
                        String nombre = ds.child("nombre").getValue().toString();
                        Date fecha = ds.child("fecha").getValue(Date.class);
                        double valorPrestado = Double.parseDouble(ds.child("valorPrestado").getValue().toString());
                        String direccion = ds.child("direccion").getValue().toString();
                        String telefono = ds.child("telefono").getValue().toString();
                        double valorCuota = Double.parseDouble(ds.child("valorCuota").getValue().toString());
                        int numeroCuotas = Integer.parseInt(ds.child("numeroCuotas").getValue().toString());
                        Cliente cliente = Cliente.builder(clienteId, nombre).fecha(fecha).valorPrestado(valorPrestado)
                                .direccion(direccion).telefono(telefono).valorCuota(valorCuota).numeroCuotas(numeroCuotas).build();
                        System.out.println(""+ds.getValue());
                        clientes.add(cliente);
                    }
                } else {
                    System.out.println("entro");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Fallo la lectura: " + databaseError.getCode());
            }
        });
    }
}