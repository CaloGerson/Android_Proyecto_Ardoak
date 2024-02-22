package com.gerson.ardoak;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListaDeVinosFragment extends Fragment {
    ArrayList<Vino> listaVinos;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    RecyclerView recyclerVinos;
    VinosDao vinosDao;
    NavController navController;

    Button gehitu;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_de_vinos, container, false);
        listaVinos= new ArrayList<>();
        vinosDao = AppDataBase.obtainInstance(requireContext()).vinosDao();

        recyclerVinos = view.findViewById(R.id.recyclerId);
        recyclerVinos.setLayoutManager(new LinearLayoutManager(requireContext()));

        consultarListaVinos();
        AdapterDatos adapterDatos = new AdapterDatos(listaVinos);
        recyclerVinos.setAdapter(adapterDatos);
        


        return view;



    }

    private void consultarListaVinos() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Consulta de la base de datos
                final List<Vino> vinos = vinosDao.getAllVinos();

                // Actualización de la interfaz de usuario en el hilo principal
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaVinos.clear();
                        listaVinos.addAll(vinos);
                        recyclerVinos.getAdapter().notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        gehitu=view.findViewById(R.id.buttonGehitu);
        gehitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_listaDeVinosFragment_to_gehituArdoaFragment);
            }
        });
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(requireContext(), "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(requireContext(), "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

