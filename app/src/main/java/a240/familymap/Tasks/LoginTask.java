package a240.familymap.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a240.familymap.R;
import a240.familymap.Requests.LoginRequest;
import a240.familymap.Responses.LoginResponse;
import a240.familymap.ServerProxy;

/**
 * Created by Alex on 11/17/2017.
 */

public class LoginTask extends AsyncTask<String, Void, List<String>>
{
    public interface Context
    {
        void onLoginComplete(List<String> responseStrings);
    }

    private Context context;

    public LoginTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected List<String> doInBackground(String... strings)
    {
        ServerProxy serverProxy = new ServerProxy();

        String serverHost = strings[0];
        int serverPort = Integer.valueOf(strings[1]);
        String userName = strings[2];
        String password = strings[3];

        try
        {
            LoginResponse loginResponse = serverProxy.login(new URL("http", serverHost, serverPort, "user/login"), new LoginRequest(userName, password));

            if(loginResponse == null)
            {
                return null;
            }

            String errorMessage = loginResponse.getErrorMessage();

            if(errorMessage == null)
            {
                String authToken = loginResponse.getAuthToken();
                String personID = loginResponse.getPersonID();

                ArrayList<String> responseStrings = new ArrayList<>();

                responseStrings.add(authToken);
                responseStrings.add(userName);
                responseStrings.add(personID);

                return responseStrings;
            }
            else
            {
                return null;
            }
        }
        catch(MalformedURLException ex)
        {
            Log.e("LoginTask", ex.getMessage(), ex);

            return null;
        }
    }

    protected void onPostExecute(List<String> responseStrings)
    {
        context.onLoginComplete(responseStrings);
    }
}
