package meomobile.it.internetconnection;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements BlankFragment.OnFragmentInteractionListener {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = (TextView) findViewById(R.id.text1);

        // Il bottone 1 carica il contenuto di una pagina web
        Button b = (Button) findViewById(R.id.button);

        assert b != null;
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionTask ct = new ConnectionTask();
                ct.execute();
            }
        });

        // Il bottone 2 è utilizzato come esempio per caricare un fragment
        Button b2 = (Button) findViewById(R.id.button2);

        assert b2 != null;
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new BlankFragment()).commit();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /*
        Classe privata che fa la connessione
        I tre tipi di dati tra le <> corrispondono a:
          - il vaolre che si passa quando si chiama la execute e che viene letto come parametro di doInBackground
          - il tipo di dato che si passa al metodo che fa l'avanzmento
          - il tipo di dato che doInBackground passa come risultato e viene letto da onPostExecute
     */
    private class ConnectionTask extends AsyncTask<Object, String, StringBuffer> {
        @Override
        protected StringBuffer doInBackground(Object[] params) {
            StringBuffer buffer = new StringBuffer();
            try {
                // Prendo la lista dal database
                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http").authority("fabiobiscaro.altervista.org").appendPath("chat")
                        .appendPath("messaggi.html");
                URL url = new URL(builder.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                int riga = 1;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line+"\n");
                }

            } catch (Exception e) {
                System.out.println("Errore: ");
                e.printStackTrace();
            }

            return buffer;
        }

        @Override
        protected void onPostExecute(StringBuffer buffer) {
            // Aggiorno la grafica con il contenuto della pagina web
            tv.setText(buffer);
            super.onPostExecute(buffer);
        }
    }
}
