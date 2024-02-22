package com.gerson.ardoak;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class HomeFragment extends Fragment {
    Button botonSingIn;
    Button botonLogIn;
    EditText name;
    EditText pasword;

    NavController navController;
    UsuariosDao usuariosDao;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        botonSingIn = view.findViewById(R.id.singIn);
        botonLogIn = view.findViewById(R.id.logIn);
        name = view.findViewById(R.id.textName);
        pasword = view.findViewById(R.id.textPassword);

        usuariosDao = AppDataBase.obtainInstance(requireContext()).usuariosDao();
        botonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_homeFragment_to_crearUsuarioFragment);
            }
        });

        botonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = name.getText().toString().trim();
                String pass = pasword.getText().toString().trim();
                if (nombre.isEmpty() || pass.isEmpty() ) {
                    // Campos incompletos, mostrar un mensaje de alerta
                    Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else{

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if (!usuariosDao.existeUsuario(nombre,pass)){
                                requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(requireContext(), "El Usuario no existe.", Toast.LENGTH_SHORT).show();
                                }
                            }); }else{
                                requireActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        navController.navigate(R.id.action_homeFragment_to_listaDeVinosFragment);
                                    }
                                });                            }
                        }
                    }).start();
                }
            }
        });
    }
}