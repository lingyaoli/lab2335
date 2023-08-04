package algonquin.cst2335.li000808;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import algonquin.cst2335.li000808.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    protected ActivityMainBinding binding;
    protected String cityName;
    protected RequestQueue queue = null;
    Bitmap image;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView( binding.getRoot() );

        binding.forecastButton.setOnClickListener(click -> {
            cityName = binding.cityTextField.getText().toString();
            String url = null;
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" + URLEncoder.encode(cityName,"UTF-8") + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        try {
                            JSONObject coord = response.getJSONObject( "coord" );
                            JSONArray weatherArray = response.getJSONArray ( "weather" );
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");
                            JSONObject mainObject = response.getJSONObject( "main" );
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");
                            int vis = response.getInt("visibility");
                            String name = response.getString( "name" );
                            JSONObject pos0 = weatherArray.getJSONObject( 0 );
                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if (file.exists())
                            {
                                image = BitmapFactory.decodeFile(pathname);
                            }
                            else {
                                ImageRequest imgReq = new ImageRequest("http://openweathermap.org/img/w/" + iconName + ".png", new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try {
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput( iconName + ".png", Activity.MODE_PRIVATE));
                                        } catch (Exception e) {
                                        }
                                    }
                                }, 1024, 1024, ImageView.ScaleType.CENTER, null,
                                        (error ) -> {       });
                                queue.add(imgReq);
                            }
                            binding.temp.setText("The current temperature is " + current);
                            binding.temp.setVisibility(View.VISIBLE);
                            binding.minTemp.setText("The min temperature is " + min);
                            binding.minTemp.setVisibility(View.VISIBLE);
                            binding.maxTemp.setText("The max temperature is " + max);
                            binding.maxTemp.setVisibility(View.VISIBLE);
                            binding.humidity.setText("The humidity is " + humidity);
                            binding.humidity.setVisibility(View.VISIBLE);
                            binding.description.setText(description);
                            binding.description.setVisibility(View.VISIBLE);
                            binding.icon.setImageBitmap(image);
                            binding.icon.setVisibility(View.VISIBLE);

                            //binding.textView1.setVisibility(View.INVISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> { } );
            queue.add(request);
        });
    }
}