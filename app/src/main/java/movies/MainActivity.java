package movies;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import dam.pgl.films.R;

public class MainActivity extends AppCompatActivity {
    public static final String endPointPeliculas = "http://api.themoviedb.org/3/discover/movie?api_key=1865f43a0549ca50d341dd9ab8b29f49&language=es";
    private static final String MOVIE_BASE_URL="https://image.tmdb.org/t/p/w185";
    private ArrayList<Film> listMovies=new ArrayList();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);
        new ObtenerPeliculasAsync().execute(endPointPeliculas);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(getApplicationContext(), ResumenActivity.class);
                intent.putExtra("id", String.valueOf (listMovies.get(position).getId() ) );
                intent.putExtra("titulo", listMovies.get(position).getTitle() );
                intent.putExtra("imagen", listMovies.get(position).getPoster_path() );
                intent.putExtra("sinopsis", listMovies.get(position).getOverview() );

                Log.d("test", "Pasando id " + listMovies.get(position).getId() );

                startActivity(intent);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.mnuConfiguracion:
                intent = new Intent(getApplicationContext(), ConfigurationActivity.class);
                startActivity(intent);
                return true;
            case R.id.mnuAcerca:
                intent = new Intent(getApplicationContext(), aboutmeActivity.class);
                startActivity(intent);
                return true;
            case R.id.mnuSalir:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    class ObtenerPeliculasAsync extends AsyncTask<String, Integer, String> {
        ProgressDialog progress;

        protected void onPreExecute() {
            super.onPreExecute();
            // Mostrar progress bar.
            progress = new ProgressDialog(MainActivity.this);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage("Obteniendo peliculas ...");
            progress.setCancelable(false);
            progress.setMax(100);
            progress.setProgress(0);
            progress.show();
        }

        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            Log.d("test", "entrando");
            try {
                URL urlObj = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = reader.readLine()) != null) result.append(line);

                Log.d("test", "respuesta: " + result.toString());

            } catch (Exception e) {
                Log.d("test", "error2: " + e.toString());
            }
            return result.toString();
        }

        protected void onProgressUpdate(Integer... a) {
            super.onProgressUpdate(a);
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject resp=new JSONObject(result);
                JSONArray films=resp.getJSONArray("results");
                for(int i=0;i<films.length();i++){
                    JSONObject film=films.getJSONObject(i);
                    //txtFilms.append("\n"+film.getString("original_title"));
                    listMovies.add(new Film(
                            film.getInt("id"),
                            film.getString("title"),
                            film.getString("backdrop_path"),
                            film.getString("poster_path"),
                            film.getString("original_title"),
                            film.getString("overview"),
                            film.getDouble("popularity"),
                            film.getString("release_date")));
                }
                Thread.sleep(1000);
            }catch (InterruptedException i) {
                i.printStackTrace();
            }catch(JSONException j){
                j.printStackTrace();
            }
            progress.dismiss();
            FilmAdapter adapter=new FilmAdapter(getApplicationContext(), listMovies);
            listView.setAdapter(adapter);
        }
    }

    class FilmAdapter extends BaseAdapter {
        Context context;
        ArrayList<Film> arrayList;

        public FilmAdapter(Context context, ArrayList<Film> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        public int getCount() {
            return arrayList.size();
        }

        public Film getItem(int position) {
            return arrayList.get(position);
        }

        public long getItemId(int i) {
            return i;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView ==  null)
                convertView = LayoutInflater.from(context).inflate(R.layout.list_films, parent, false);
            TextView date = convertView.findViewById(R.id.tvFecha);
            date.setText(arrayList.get(position).getRelease_date());
            TextView name =convertView.findViewById(R.id.tvTitle);
            name.setText(arrayList.get(position).getTitle());
            TextView description = convertView.findViewById(R.id.tvDescripcion);
            description.setText(arrayList.get(position).getOverview().substring(0,100) + " ... ");
            ImageView imagen = convertView.findViewById(R.id.list_image);
            Picasso.get().load(MOVIE_BASE_URL + arrayList.get(position).getBackdrop_path()).into(imagen);
            imagen.setScaleType(ImageView.ScaleType.FIT_XY);
            return convertView;
        }
    }
}




