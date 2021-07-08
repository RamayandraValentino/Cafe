package a1822500097.mahasiswa.atmaluhur.id.ac.cafe;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<HashMap<String, String>> makananlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makananlist = new ArrayList<>();
        lv = (ListView) findViewById(R.id.listviewmakanan);
        new AmbilDataMakanan().execute();
    }

    private class AmbilDataMakanan extends AsyncTask<Void,Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Loading Data...", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0)
        {
            HttpHandler sh = new HttpHandler();

            String url = "http://192.168.43.158/cafe/data_makanan.php";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Data dari PHP : " + jsonStr);
            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObj = new JSONObject (jsonStr);
                    JSONArray data_makanan = jsonObj.getJSONArray("makanan");

                    for (int i = 0; i < data_makanan.length(); i++)
                    {
                        JSONObject c = data_makanan.getJSONObject(i);

                        HashMap<String, String> detail_makanan = new HashMap<>();
                        detail_makanan.put("id", c.getString("id"));
                        detail_makanan.put("nama_menu", c.getString("nama_menu"));
                        detail_makanan.put("harga", c.getString("harga"));
                        detail_makanan.put("keterangan", c.getString("keterangan"));

                        makananlist.add(detail_makanan);
                    }
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Gagal Kirim Data : " + e.getMessage());
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(getApplicationContext(),
                                    "Gagal Kirim Data : " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Data Kosong", Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(MainActivity.this, makananlist,
                    R.layout.list_item_makanan, new String[] {"nama_menu", "harga", "keterangan"},
                    new int[]{R.id.txtnmmakanan, R.id.txtharga, R.id.txtketerangan});
            lv.setAdapter(adapter);
        }
    }
}
