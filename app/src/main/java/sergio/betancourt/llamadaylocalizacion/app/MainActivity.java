package sergio.betancourt.llamadaylocalizacion.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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


public class MainActivity extends ActionBarActivity {

    Button btnLlamar, btnLocalizar;
    EditText edtTelefono;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLlamar = (Button) findViewById(R.id.btnLlamar);
        btnLocalizar = (Button) findViewById(R.id.btnLocalizar);




        btnLlamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(),Llamada.class );
                startActivity(intent);




            }
        });

        btnLocalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarLocalizar();
            }
        });

    }





        public void Idioma (View v) {
            try {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:x"));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "no puedes llamar", Toast.LENGTH_SHORT).show();
            }
        }



    private void cambiarLocalizar(){
        Intent intent = new Intent(this,Localizar.class);
        startActivity(intent);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
