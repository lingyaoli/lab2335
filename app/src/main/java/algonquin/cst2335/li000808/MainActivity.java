package algonquin.cst2335.li000808;

import androidx.appcompat.app.AppCompatActivity;
import algonquin.cst2335.li000808.databinding.ActivityMainBinding;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    protected String cityName;
    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String stringURL = null;
            try {
                stringURL = "https://api.openweathermap.org/data/2.5/weather?q="
                        + URLEncoder.encode(cityName, "UTF-8")
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {

                try {
                    JSONObject coord = response.getJSONObject("coord");
                    double lon = coord.getDouble("lon");
                    double lat = coord.getDouble("lat");

                    JSONArray weatherArray = response.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    String description = weatherObject.getString("description");
                    String iconName = weatherObject.getString("icon");

                    JSONObject mainObject = response.getJSONObject("main");
                    double currentTemperature = mainObject.getDouble("temp");
                    double minTemperature = mainObject.getDouble("temp_min");
                    double maxTemperature = mainObject.getDouble("temp_max");
                    int humidity = mainObject.getInt("humidity");

                }catch (JSONException e){
                    e.printStackTrace();
                }
                    },
                    (error) -> {
                    });
            queue.add(request);
        });
    }
}