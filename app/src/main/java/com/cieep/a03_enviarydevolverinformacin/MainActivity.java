package com.cieep.a03_enviarydevolverinformacin;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cieep.a03_enviarydevolverinformacin.modelos.Direccion;
import com.cieep.a03_enviarydevolverinformacin.modelos.Usuario;

public class MainActivity extends AppCompatActivity {

    // Variables para la VISTA
    private EditText txtPassword;
    private EditText txtEmail;
    private Button btnEnviar;
    private Button btnCrearDirOld;
    private Button btnCrearDirLauncher;

    // CONTANTES PARA RETORNOS DE INFO
    private final int DIRECCIONES = 7;
    private final int COCHES = 8;

    // ActivitiesResultLaunchers
    private ActivityResultLauncher<Intent> crearDirLauncher;
    private ActivityResultLauncher<Intent> crearCocheLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Paso 2 -> Inicializa Variables de Vista
        inicializaVistas();
        // Paso 2.5 -> Enventos de Retorno
        inicializaActivitiesResultLaunchers();

        // Paso 3 -> Definir Eventos

        btnEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();

                Usuario user = new Usuario(email, password);

                Intent intent = new Intent(MainActivity.this, DescifrarPassActivity.class);
                // Maleta para transportar información
                Bundle bundle = new Bundle();
                bundle.putSerializable("USUARIO", user);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        /**
         * Forma Obsoleta para obtener información de otra actividad
         */
        btnCrearDirOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Arranca una actividad, avisa a Android que cuando el destino termine,
                // se tiene que procesar la info que devuelva, en un método especial
                // que es onActivityResult
                startActivityForResult(new Intent(MainActivity.this, RetornoOLDActivity.class), DIRECCIONES);
            }
        });

        btnCrearDirLauncher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RetornoOLDActivity.class);
                crearDirLauncher.launch(intent);
            }
        });
    }

    /**
     * Crearemos los ActivityResultLaunchers
     */
    private void inicializaActivitiesResultLaunchers() {
        crearDirLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            if (result.getData() != null) {
                                Direccion dir = (Direccion) result.getData().getExtras().getSerializable("DIR");
                                Toast.makeText(MainActivity.this, dir.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(MainActivity.this, "CANCELADO", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        crearCocheLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                }
        );
    }

    private void inicializaVistas() {
        txtPassword = findViewById(R.id.txtPasswordMain);
        txtEmail = findViewById(R.id.txtEmailMain);
        btnEnviar = findViewById(R.id.btnEnviarMain);
        btnCrearDirOld = findViewById(R.id.btnCrearDirOldMain);
        btnCrearDirLauncher = findViewById(R.id.btnCrearDirLauncherMain);
    }


    /**
     * Se dispara al reactivar este Activity despues de lanzar una nueva Activity con startActivityForResult()
     *
     * @param requestCode -> Es la constante que me indica que Activity me está retornando información
     * @param resultCode -> Es el Código de resultado de como ha terminado la Actividad
     * @param data -> intent que he puesto en el setResult()
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == DIRECCIONES) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Direccion dir = (Direccion) data.getExtras().getSerializable("DIR");
                    Toast.makeText(this, dir.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(this, "Entrada de Dirección Cancelada", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == COCHES) {
            if (resultCode == RESULT_OK ) {

            }
        }
    }
}