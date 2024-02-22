package com.gerson.ardoak;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import java.util.Map;

public class GehituArdoaFragment extends Fragment {

     ImageView previewImageView;
     Button btnSelectImage;
     TextView messageTextView;
     EditText editTextIzena, editTextOrigen, editTextTipo, editTextPrecio;
     Button buttonGoBack, buttonSaveVino;
     NavController navController;
     VinosDao vinosDao;
     Uri selectedImageUri;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gehitu_ardoa, container, false);
    }

    private void CargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Seleciones la palicacion"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                selectedImageUri = data.getData();

                // Obten la ruta local del archivo
                String imagePath = getRealPathFromURI(selectedImageUri);

                // Muestra la ruta en el log
                Log.d("LOCAL_PATH_DEBUG", "Local Path: " + imagePath);
                Log.d("uri", "Local uri: " + selectedImageUri.toString());


                // Actualiza la vista previa si es necesario
                previewImageView.setImageURI(selectedImageUri);
            }
        }
    }

    // Método para obtener la ruta local del archivo desde la URI
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        ContentResolver contentResolver = requireActivity().getContentResolver();
        Cursor cursor = contentResolver.query(contentUri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String filePath = cursor.getString(column_index);
            cursor.close();
            return filePath;
        } else {
            return contentUri.getPath(); // Si no se puede obtener desde el cursor, intenta obtener la ruta directamente desde la URI
        }
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        editTextIzena = view.findViewById(R.id.gehituardoa_editTextIzena);
        editTextOrigen = view.findViewById(R.id.gehiruardoa_editTextOrigen);
        editTextTipo = view.findViewById(R.id.gehituardoa_editTextTipo);
        editTextPrecio = view.findViewById(R.id.gehituardoa_editTexPrecio);
        buttonGoBack = view.findViewById(R.id.buttonGoBack);
        buttonSaveVino = view.findViewById(R.id.buttonSaveVino);
        vinosDao = AppDataBase.obtainInstance(requireContext()).vinosDao();
        //---------------
        previewImageView = (ImageView) view.findViewById(R.id.previewImageView);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        messageTextView = view.findViewById(R.id.messageTextView);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });

        //-----------------


        buttonSaveVino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nombre = editTextIzena.getText().toString().trim();
                    String origen = editTextOrigen.getText().toString().trim();
                    String tipo = editTextTipo.getText().toString().trim();
                    double precio = Double.parseDouble(editTextPrecio.getText().toString().trim());

                    if (nombre.isEmpty() || origen.isEmpty() || tipo.isEmpty()) {
                        Toast.makeText(getContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                    } else {
                        Vino vino = new Vino();
                        vino.setNombre(nombre);
                        vino.setOrigen(origen);
                        vino.setTipo(tipo);
                        vino.setPrecio(precio);
                        vino.setImagePath(selectedImageUri.toString());

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                vinosDao.insertVino(vino);
                                // Mostrar Toast en el hilo principal (UI Thread)
                                Activity activity = getActivity();
                                if (activity != null) {
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getContext(), "Vino guardado correctamente", Toast.LENGTH_SHORT).show();
                                            navController.navigate(R.id.action_gehituArdoaFragment_to_listaDeVinosFragment);
                                        }
                                    });
                                }
                            }
                        }).start();
                    }

                } catch (Exception e) {
                    // Manejo de excepciones aquí, si es necesario
                    e.printStackTrace();
                    Log.e("CreacionUsuario", "Excepción al crear la cuenta: " + e.getMessage());
                }
            }
        });
    }


}
