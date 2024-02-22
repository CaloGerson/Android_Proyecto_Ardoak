package com.gerson.ardoak;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class CrearUsuarioFragment extends Fragment {
    EditText name;
    EditText correo;
    EditText password1;
    EditText password2;
    Button crearCuneta;
    NavController navController;
    UsuariosDao usuariosDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_crear_usuario, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        name = view.findViewById(R.id.inputName);
        correo = view.findViewById(R.id.inputCorreo);
        password1 = view.findViewById(R.id.inputPassword1);
        password2 = view.findViewById(R.id.inputPassword2);
        crearCuneta = view.findViewById(R.id.buttonCrearCuenta);
        usuariosDao = AppDataBase.obtainInstance(requireContext()).usuariosDao();

        crearCuneta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    // Obtener el texto de los EditText
                    String nombre = name.getText().toString().trim();
                    String email = correo.getText().toString().trim();
                    String pass1 = password1.getText().toString().trim();
                    String pass2 = password2.getText().toString().trim();
                    // Verificar si los campos están vacíos
                    if (nombre.isEmpty() || email.isEmpty() || pass1.isEmpty() || pass2.isEmpty()) {
                        // Campos incompletos, mostrar un mensaje de alerta
                        Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    } else if (!pass1.equals(pass2)){

                        Toast.makeText(getContext(), "Las contraseñas no concuerdan", Toast.LENGTH_SHORT).show();

                    }else {

                        // Crear un objeto Usuario con la información
                        Usuario nuevoUsuario = new Usuario(nombre, email, pass1);
                        // Utilizar un hilo de fondo para realizar la inserción
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // Insertar el nuevo usuario en la base de datos
                                long idUsuarioInsertado = usuariosDao.insert(nuevoUsuario);

                                // Verificar si la inserción fue exitosa
                                if (idUsuarioInsertado != -1) {
                                    // Nuevo usuario creado exitosamente
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show();
                                            // Navegar de vuelta a fragment_home
                                            navController.navigate(R.id.action_crearUsuarioFragment_to_homeFragment);
                                        }
                                    });
                                } else {
                                    // Error al crear el usuario
                                    requireActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Error al crear la cuenta", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }}).start();


                    }

                }catch (Exception e){
                    // Manejo de excepciones aquí, si es necesario
                    e.printStackTrace();
                    Log.e("CreacionUsuario", "Excepción al crear la cuenta: " + e.getMessage());

                }

            }
        });

    }



}