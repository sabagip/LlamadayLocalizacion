package sergio.betancourt.llamadaylocalizacion.app;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;


public class Localizar extends Activity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener  {

    private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000 ;
    LocationClient mLocationClient;
    Location mCurrentLocation;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localizar);

        mLocationClient = new LocationClient(this, this, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
        Toast.makeText(this, "hola", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
    }

    public void obtenerPosicion(View v){
        if (servicesConnected())
            Toast.makeText(this, "Connectado", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "No Connectado", Toast.LENGTH_SHORT).show();

        if (mLocationClient.isConnected()) {
            mCurrentLocation = mLocationClient.getLastLocation();
            TextView coordenada;
            coordenada = (TextView) findViewById(R.id.lblCoordenada);
            coordenada.setText(mCurrentLocation.getLatitude() + "," + mCurrentLocation.getLongitude());
        }else{
            mLocationClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            //showErrorDialog(connectionResult.getErrorCode());
        }
    }

    private boolean servicesConnected() {
        int resultCode =
                GooglePlayServicesUtil.
                        isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {return true;}
        return false;

    }


}
