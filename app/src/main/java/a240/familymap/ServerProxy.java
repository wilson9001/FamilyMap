package a240.familymap;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import a240.familymap.Requests.LoginRequest;
import a240.familymap.Requests.PersonRequest;
import a240.familymap.Requests.RegisterRequest;
import a240.familymap.Responses.EventResponse;
import a240.familymap.Responses.LoginResponse;
import a240.familymap.Responses.PersonResponse;
import a240.familymap.Responses.RegisterResponse;

/**
 * Created by Alex on 11/17/2017.
 */

public class ServerProxy
{
    public LoginResponse login(URL url, LoginRequest loginRequest)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(postMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(new Gson().toJson(loginRequest));
            outputStreamWriter.close();
            //connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                //return responseBodyData;
                JsonParser jsonParser = new JsonParser();

                JsonObject obj = jsonParser.parse(responseBodyData).getAsJsonObject();

                System.out.println(obj.toString());

                return new Gson().fromJson(obj, LoginResponse.class);

                //return new Gson().fromJson(responseBodyData, LoginResponse.class);
            }

            else
            {
                return null;
            }

            /*Gson gson = new Gson();

            String requestJSON = gson.toJson(loginRequest);

            byte[] outputBytes = requestJSON.getBytes("UTF-8");
            OutputStream outStream = connection.getOutputStream();
            outStream.write(outputBytes);
            outStream.close();

            //connection.
            connection.connect();

            /*if(connection.getResponseCode() == HttpURLConnection.HTTP_OK)
            {
                // InputStream responseStream = connection.getInputStream();

                String authToken = connection.getHeaderField()
            }*/

            //return gson.fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), LoginResponse.class);
        }
        catch(Exception ex)
        {
            Log.e("ServerProxy-login", ex.getMessage(), ex);
            return null;
        }
    }

    public PersonResponse getPeople(URL url, String authToken)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(getMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);

           // Gson gson = new Gson();

            /*String requestJSON = gson.toJson(personRequest);

            byte[] outputBytes = requestJSON.getBytes("UTF-8");
            OutputStream outStream = connection.getOutputStream();
            outStream.write(outputBytes);
            outStream.close();*/

            //connection.
            connection.connect();

            Gson gson = new Gson();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                //return responseBodyData;

                PersonResponse personResponse = gson.fromJson(responseBodyData, PersonResponse.class);

               /* JsonParser jsonParser = new JsonParser();

                JsonObject obj = jsonParser.parse(responseBodyData).getAsJsonObject();

                PersonResponse personResponse = gson.fromJson(obj, PersonResponse.class);*/

                return personResponse;

                //return new Gson().fromJson(responseBodyData, PersonResponse.class);
            }

            else
            {
                return null;
            }
            //String requestJSON = gson.toJson(loginRequest);

            //byte[] outputBytes = requestJSON.getBytes("UTF-8");
            //OutputStream outStream = connection.getOutputStream();
            //outStream.write(outputBytes);
            //outStream.close();

            //return new Gson().fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), PersonResponse.class);
        }
        catch(Exception ex)
        {
            Log.e("ServerProxy-getPeople", ex.getMessage(), ex);
            return null;
        }
    }

    public RegisterResponse register(URL url, RegisterRequest registerRequest)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(postMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.connect();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(connection.getOutputStream());
            outputStreamWriter.write(new Gson().toJson(registerRequest));
            outputStreamWriter.close();
            //connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                //return responseBodyData;
                JsonParser jsonParser = new JsonParser();

                JsonObject obj = jsonParser.parse(responseBodyData).getAsJsonObject();

                return new Gson().fromJson(obj, RegisterResponse.class);
            }

            else
            {
                return null;
            }
            //return new Gson().fromJson(new BufferedReader(new InputStreamReader(connection.getInputStream())), RegisterResponse.class);
        }
        catch(Exception ex)
        {
            Log.e("ServerProxy-register", ex.getMessage(), ex);
            return null;
        }
    }

    public EventResponse getEvents(URL url, String authToken)
    {
        try
        {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(getMethod);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", authToken);

            connection.connect();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get response body input stream
                InputStream responseBody = connection.getInputStream();

                // Read response body bytes
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = responseBody.read(buffer)) != -1) {
                    baos.write(buffer, 0, length);
                }

                // Convert response body bytes to a string
                String responseBodyData = baos.toString();
                //return responseBodyData;

                JsonParser jsonParser = new JsonParser();

                JsonObject obj = jsonParser.parse(responseBodyData).getAsJsonObject();

                return new Gson().fromJson(obj, EventResponse.class);
            }

            else
            {
                return null;
            }
        }
        catch(Exception ex)
        {
            Log.e("ServerProxy-getEvents", ex.getMessage(), ex);
            return null;
        }
    }

    private static final String postMethod = "POST";
    private static final String getMethod = "GET";
}
