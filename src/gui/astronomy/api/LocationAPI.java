package gui.astronomy.api;

/**
 * This is used to change a string location to a set of coordinates
 * It uses the geoCode api from google along with the gson library which is used to parse the json returned from the api call
 */

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LocationAPI {
    private static final String geoCodeKey = "AIzaSyAvEbQPIqVaMg6G2m9Kncu5e6q5NS1Oc2k";

    public LocationAPI() {
    }

    public static LatLong getCoords(String location) {
        StringBuilder jsonBuilder = new StringBuilder();

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + location.replace(' ', '+') + "&key=" + "AIzaSyAvEbQPIqVaMg6G2m9Kncu5e6q5NS1Oc2k");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while((line = br.readLine()) != null) {
                jsonBuilder.append(line);
            }

            br.close();
            Gson e = new Gson();
            JsonLocation jsonLocation = (JsonLocation)e.fromJson(jsonBuilder.toString(), JsonLocation.class);
            return new LatLong(jsonLocation.getLat(), jsonLocation.getLng());
        } catch (Exception var8) {
            System.err.println(var8 + ": " + var8.getMessage());
            return null;
        }
    }
}
