package sergio.betancourt.llamadaylocalizacion.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ShareActionProvider;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class Llamada extends ActionBarActivity {
    View view;
    ListView lista;
    JSONObject valor ;

    public static final String  TAG =
            Llamada.class.getSimpleName();
    JSONArray datosServidor;
    SharedPreferences agenda;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamada);

        lista = (ListView)findViewById(R.id.lista);



        obtenerTextoInternet();


    }

    private void obtenerTextoInternet() {
        if(isNetworkAvailable()){
            GetAPI getAPI = new GetAPI();
            getAPI.execute();



        }else{
           // Toast.makeText(this, "Error lalalala", Toast.LENGTH_LONG).show();

            try {
                agenda = getSharedPreferences("Contactos", Context.MODE_PRIVATE);
                String nombre = agenda.getString("nombre", "");
                String telefono = agenda.getString("numero", "");

                if (nombre != null && telefono != null) {

                    Toast.makeText(getApplicationContext(), nombre + telefono, Toast.LENGTH_SHORT).show();


                    final HashMap<String, String> hashValor =
                            new HashMap<String, String>();

                    final ArrayList<HashMap<String, String>> valores =
                            new ArrayList<HashMap<String, String>>();

                    hashValor.put("nombre", nombre);
                    hashValor.put("numero", telefono);

                    valores.add(hashValor);

                    final String[] llaves = {"nombre", "numero"};
                    int[] ids = {android.R.id.text2, android.R.id.text1};

                    SimpleAdapter adaptador = new SimpleAdapter(this, valores,
                            android.R.layout.simple_list_item_2,
                            llaves, ids);

                    lista.setAdapter(adaptador);

                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView,
                                                View view, int i, long l) {
                            //  mostrarAlerta(i);

                            try {
                                Intent intent = new Intent(Intent.ACTION_CALL);
                                intent.setData(Uri.parse("tel:" + hashValor.get("numero")));
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Llamando a " + hashValor.get("numero"), Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "no puedes llamar", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }
            }
                catch (Exception e){}










                }



        }




    private boolean isNetworkAvailable() {
        boolean isAvailable = false;

        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        else{
            Toast.makeText(this, "Sin Conexión", Toast.LENGTH_LONG).show();
        }


        return isAvailable;
    }

    private class GetAPI extends AsyncTask<Object, Void, JSONArray>{


        @Override
        protected JSONArray doInBackground(Object... objects) {
            Log.d(TAG, "Response OK");

            int responseCode = -1;
            String resultado = "";
            JSONArray jsonResponse = null;

            try{
                URL apiURL =  new URL(
                        "http://codipaj.com/itchihuahuaii/eq5/SergioBetancourt/numeros.php");



                HttpURLConnection httpConnection = (HttpURLConnection)
                        apiURL.openConnection();
                httpConnection.connect();
                responseCode = httpConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK){
                    InputStream inputStream = httpConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(
                            new InputStreamReader(inputStream, "UTF-8"), 8);

                    StringBuilder sBuilder = new StringBuilder();

                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        //sBuilder.append(line + "\n");
                        sBuilder.append(line);
                    }

                    inputStream.close();
                    resultado = sBuilder.toString();
                    resultado = "[" + resultado + "]";
                    Log.d(TAG, resultado);
                    jsonResponse = new JSONArray(resultado);

                }else{
                    Log.i(TAG, "Error en el HTTP " + responseCode);
                }
            }
            catch (JSONException e){}
            catch (MalformedURLException e){}
            catch (IOException e){}
            catch (Exception e){}

            return jsonResponse;
        }

        @Override
        protected void onPostExecute(JSONArray respuesta) {
            try{
                enlistarDatos(respuesta);
            }
            catch (Exception e){}
        }

    }

    private void enlistarDatos(JSONArray datos) {
        if (datos == null){
            Toast.makeText(this, "Error en el servidor",
                    Toast.LENGTH_LONG).show();
        }
        else{
            datosServidor = datos;
            final ArrayList<HashMap<String,String>> valores =
                    new ArrayList<HashMap<String, String>>();

            try{

                SharedPreferences configuracion = getSharedPreferences("Contactos",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = configuracion.edit();


                for (int i =0; i< datos.length(); i++){
                    valor = datos.getJSONObject(i);

                    HashMap<String, String> hashValor =
                            new HashMap<String, String>();

                    hashValor.put("nombre", valor.getString("nombre"));
                    editor.putString("nombre", valor.getString("nombre"));

                    hashValor.put("numero",valor.getString("numero"));
                    editor.putString("numero",valor.getString("numero"));

                    valores.add(hashValor);
                    editor.commit();

                }



                final String[] llaves = {"nombre","numero"};
                int[] ids = {android.R.id.text2,android.R.id.text1};

                SimpleAdapter adaptador = new SimpleAdapter(this, valores,
                        android.R.layout.simple_list_item_2,
                        llaves, ids);

                lista.setAdapter(adaptador);

                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView,
                                            View view, int i, long l) {
                      //  mostrarAlerta(i);

                        try{
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse("tel:" + valor.getString("numero") ));
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"Llamando a " + valor.getString("numero") , Toast.LENGTH_LONG).show();
                        }catch(Exception e){
                            Toast.makeText(getApplicationContext(),"no puedes llamar",Toast.LENGTH_LONG).show();
                        }

                    }
                });

            }
            catch (JSONException e){}
        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.llamada, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
