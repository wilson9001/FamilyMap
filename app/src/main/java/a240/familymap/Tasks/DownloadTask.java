package a240.familymap.Tasks;

import android.os.AsyncTask;
import java.net.URL;

import a240.familymap.ServerProxy;

/**
 * Created by Alex on 11/17/2017.
 */

public class DownloadTask extends AsyncTask<String, Void, String>
{
    public interface Context
    {
        void onDownloadComplete(String json);
    }

    private Context context;

    public DownloadTask(Context context)
    {
        this.context = context;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        ServerProxy serverProxy = new ServerProxy();

        String serverHost = strings[0];
        String serverPort = strings[1];
        String userName = strings[2];
        String password = strings[3];
        String firstName = strings[4];
        String lastName = strings[5];
        String email = strings[6];
        String gender = strings[7];

        //not finished. May delete this class.
        return null;
    }

    protected void onPostExecute(String json)
    {
        context.onDownloadComplete(json);
    }
}
