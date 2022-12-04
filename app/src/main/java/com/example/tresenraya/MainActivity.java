package com.example.tresenraya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationBarView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridView gridViewTablero;
    Boolean turnoJugador;
    int[] posiciones = new int[9];
    int turno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] arraySpinner = new String[] {"Facil", "Avanzado", "JcJ"};

        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Bundle parametros = this.getIntent().getExtras();

        if(parametros != null)
            spinner.setSelection(parametros.getInt("nivel"));

        gridViewTablero = findViewById(R.id.gridViewTablero);
        gridViewTablero.setNumColumns(3);

        ImageView restartButton = (ImageView) findViewById(R.id.restartButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nivel = spinner.getSelectedItemPosition();

                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("nivel", nivel);
                startActivity(intent);

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                for(int i = 0; i < 9; i++)
                    posiciones[i] = R.drawable.invisible;

                gridViewTablero.setAdapter(new GridAdapter(getApplicationContext(), posiciones));
                String selectedItem = parent.getItemAtPosition(position).toString();

                if(selectedItem.equals("Facil"))
                    juegoFacil(gridViewTablero);

                else if(selectedItem.equals("Avanzado"))
                    juegoAvanzado(gridViewTablero);

                else
                    juegoDosJugadores(gridViewTablero);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void juegoFacil(GridView tablero) {
        TextView textTurno = (TextView) findViewById(R.id.turno);
        turnoJugador = true;
        textTurno.setText("Juegas tú");

        tablero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(turnoJugador) {
                    if(posiciones[position] == R.drawable.invisible) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.jugador);
                        posiciones[position] = R.drawable.jugador;

                        if(comprobarVictoria(R.drawable.jugador)) {
                            tablero.setOnItemClickListener(null);
                            textTurno.setText("Has ganado la partida!");

                        }

                        else {
                            turnoJugador = false;
                            textTurno.setText("Juega la IA");

                            if(casillasVacias()) {
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        IAFacil(tablero);

                                        if(comprobarVictoria(R.drawable.android)) {
                                            tablero.setOnItemClickListener(null);
                                            textTurno.setText("La IA gana la partida");

                                        }

                                        else {
                                            turnoJugador = true;
                                            textTurno.setText("Juegas tú");

                                        }

                                    }
                                };

                                Handler h = new Handler();
                                h.postDelayed(r, 3000);

                            }

                            else {
                                tablero.setOnItemClickListener(null);
                                textTurno.setText("Empate");

                            }

                        }

                    }

                }

            }
        });

    }

    private void IAFacil(GridView tablero) {
        int random;

        do {
            random = new Random().nextInt(9);

        } while(posiciones[random] != R.drawable.invisible);

        posiciones[random] = R.drawable.android;
        tablero.setAdapter(new GridAdapter(getApplicationContext(), posiciones));

    }

    private void juegoAvanzado(GridView tablero) {
        TextView textTurno = (TextView) findViewById(R.id.turno);
        turnoJugador = true;
        textTurno.setText("Juegas tú");

        tablero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(turnoJugador) {
                    if(posiciones[position] == R.drawable.invisible) {
                        ImageView imageView = (ImageView) view;
                        imageView.setImageResource(R.drawable.jugador);
                        posiciones[position] = R.drawable.jugador;

                        if(comprobarVictoria(R.drawable.jugador)) {
                            tablero.setOnItemClickListener(null);
                            textTurno.setText("Has ganado la partida!");

                        }

                        else {
                            turnoJugador = false;
                            textTurno.setText("Juega la IA");

                            if(casillasVacias()) {
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        IADificil(tablero);

                                        if(comprobarVictoria(R.drawable.android)) {
                                            tablero.setOnItemClickListener(null);
                                            textTurno.setText("La IA gana la partida");

                                        }

                                        else {
                                            turnoJugador = true;
                                            textTurno.setText("Juegas tú");

                                        }

                                    }
                                };

                                Handler h = new Handler();
                                h.postDelayed(r, 3000);

                            }

                            else {
                                tablero.setOnItemClickListener(null);
                                textTurno.setText("Empate");

                            }

                        }

                    }

                }

            }
        });

    }

    private void IADificil(GridView tablero) {
        int intentarVictoria, intentarBloqueo;

        if((intentarVictoria = victoriaOBloqueo(R.drawable.android)) != -1)
            posiciones[intentarVictoria] = R.drawable.android;

        else if((intentarBloqueo = victoriaOBloqueo(R.drawable.jugador)) != -1)
            posiciones[intentarBloqueo] = R.drawable.android;

        else if(posiciones[4] == R.drawable.invisible)
            posiciones[4] = R.drawable.android;

        else {
            int random;

            do {
                random = new Random().nextInt(9);

            } while(posiciones[random] != R.drawable.invisible);

            posiciones[random] = R.drawable.android;

        }

        tablero.setAdapter(new GridAdapter(getApplicationContext(), posiciones));

    }

    private void juegoDosJugadores(GridView tablero) {
        TextView textTurno = (TextView) findViewById(R.id.turno);
        textTurno.setText("Juega el jugador 1");
        turno = 0;

        tablero.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ImageView imageView = (ImageView) view;

                if(posiciones[position] == R.drawable.invisible) {
                    if(turno % 2 == 0) {
                        imageView.setImageResource(R.drawable.jugador1);
                        posiciones[position] = R.drawable.jugador1;

                        turno++;
                        textTurno.setText("Juega el jugador 2");

                        if(comprobarVictoria(R.drawable.jugador1)) {
                            tablero.setOnItemClickListener(null);
                            textTurno.setText("Gana el jugador 1");

                        }

                    }

                    else {
                        imageView.setImageResource(R.drawable.jugador2);
                        posiciones[position] = R.drawable.jugador2;

                        turno++;
                        textTurno.setText("Juega el jugador 1");

                        if(comprobarVictoria(R.drawable.jugador2)) {
                            tablero.setOnItemClickListener(null);
                            textTurno.setText("Gana el jugador 2");

                        }

                    }

                }

                if(!casillasVacias()) {
                    tablero.setOnItemClickListener(null);
                    textTurno.setText("Empate");

                }

            }
        });

    }

    private int victoriaOBloqueo(int turno) {
        int posicion = -1;
        int i = 0;
        int colocados = 0;
        //Victoria recoge la linea en la que debe jugar
        int victoria = -1;

        int jugador = 0, rival = 0;

        if(turno == R.drawable.android) {
            jugador = R.drawable.android;
            rival = R.drawable.jugador;

        }

        else if(turno == R.drawable.jugador){
            jugador = R.drawable.jugador;
            rival = R.drawable.android;

        }

        //Comprueba las horizontales
        while(i < 9 && victoria == -1) {
            if(i % 3 == 0) {
                if(colocados == 2)
                    victoria = i - 3;

                else
                    colocados = 0;

            }

            if(posiciones[i] == jugador)
                colocados++;

            else if(posiciones[i] == rival)
                colocados--;

            i++;

        }

        //Busca la posicion vacía dentro de la horizontal en la que debe jugar
        if(victoria != -1) {
            for(int j = victoria; j < victoria + 3; j++)
                if(posiciones[j] == R.drawable.invisible)
                    return j;

        }

        i = 0;

        //Comprueba las verticales
        while(i < 3 && victoria == -1) {
            colocados = 0;
            for(int j = i; j < i + 7; j += 3) {
                if(posiciones[j] == jugador)
                    colocados++;

                else if(posiciones[j] == rival)
                    colocados--;

            }

            if(colocados == 2)
                victoria = i;

            i++;

        }

        //Busca la posicion vacía dentro de la vertical en la que debe jugar
        if(victoria != -1) {
            for(int j = victoria; j < victoria + 7; j += 3)
                if(posiciones[j] == R.drawable.invisible)
                    return j;

        }

        colocados = 0;

        //Comprueba la diagonal
        for(int j = 0; j < 9; j += 4) {
            if(posiciones[j] == R.drawable.invisible)
                posicion = j;

            if(posiciones[j] == jugador)
                colocados++;

            else if(posiciones[j] == rival)
                colocados--;

        }

        if(colocados == 2)
            return posicion;

        colocados = 0;

        //Comprueba la diagonal inversa
        for(int j = 2; j < 7; j += 2) {
            if(posiciones[j] == R.drawable.invisible)
                posicion = j;

            if(posiciones[j] == jugador)
                colocados++;

            else if(posiciones[j] == rival)
                colocados--;

        }

        if(colocados == 2)
            return posicion;

        return -1;
    }

    private Boolean casillasVacias() {
        Boolean casillasVacias = false;
        int i = 0;

        while(i < 9 && !casillasVacias) {
            if(posiciones[i] == R.drawable.invisible)
                casillasVacias = true;

            i++;

        }

        return casillasVacias;
    }

    private Boolean comprobarVictoria(int jugador) {
        Boolean victoria = false;
        int i = 0;

        //Comprueba las horizontales
        while(i < 7 && !victoria) {
             if(posiciones[i] == jugador && posiciones[i + 1] == jugador && posiciones[i + 2] == jugador)
                 victoria = true;

             i += 3;

        }

        i = 0;

        //Comprueba las verticales
        while(i < 3 && !victoria) {
            if(posiciones[i] == jugador && posiciones[i + 3] == jugador && posiciones[i + 6] == jugador)
                victoria = true;

            i += 1;

        }

        i = 0;

        //Comprueba la diagonal
        if(posiciones[i] == jugador && posiciones[i + 4] == jugador && posiciones[i + 8] == jugador)
            victoria = true;

        i = 2;

        //Comprueba la diagonal inversa
        if(posiciones[i] == jugador && posiciones[i + 2] == jugador && posiciones[i + 4] == jugador)
            victoria = true;

        return victoria;
    }

}