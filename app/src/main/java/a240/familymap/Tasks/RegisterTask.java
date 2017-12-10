package a240.familymap.Tasks;

import android.os.AsyncTask;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import a240.familymap.Activities.AppData;
import a240.familymap.Requests.RegisterRequest;
import a240.familymap.Responses.RegisterResponse;
import a240.familymap.ServerProxy;

/**
 * Created by Alex on 11/17/2017.
 */

public class RegisterTask extends AsyncTask<String, Void, /*List<String>*/Boolean>
{

    public interface Context
    {
        void onRegisterComplete(/*List<String> responseStrings*/Boolean success);
    }

    private Context context;

    public RegisterTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected /*List<String>*/Boolean doInBackground(String... strings)
    {
        ServerProxy serverProxy = new ServerProxy();

        String serverHost = strings[0];
        int serverPort = Integer.valueOf(strings[1]);
        String userName = strings[2];
        String password = strings[3];
        String firstName = strings[4];
        String lastName = strings[5];
        String email = strings[6];
        String gender = strings[7];

        try
        {
            RegisterResponse registerResponse = serverProxy.register(new URL("http", serverHost, serverPort, "user/register"), new RegisterRequest(userName, password, email, firstName, lastName, gender));

            if(registerResponse == null)
            {
                return false;
            }

            String errorMessage = registerResponse.getErrorMessage();

            if(errorMessage == null)
            {
                AppData appData = AppData.getInstance();

                appData.setUserPersonID(registerResponse.getPersonID());
                appData.setAuthToken(registerResponse.getAuthToken());
                appData.setServerHost(serverHost);
                appData.setPort(serverPort);
                return true;
                /*
                ArrayList<String> responseStrings = new ArrayList<>();

                responseStrings.add(userName);
                responseStrings.add(password);
                responseStrings.add(firstName);
                responseStrings.add(lastName);
                responseStrings.add(email);
                responseStrings.add(gender);

                return responseStrings;
                */
            }
            else
            {
                return false;
            }
        }
        catch(MalformedURLException ex)
        {
            Log.e("RegisterTask", ex.getMessage(), ex);

            return false;
        }
    }

    protected void onPostExecute(/*List<String> responseStrings*/Boolean success)
    {
        context.onRegisterComplete(/*responseStrings*/success);
    }
}
