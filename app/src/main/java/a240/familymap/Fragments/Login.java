package a240.familymap.Fragments;

import android.content.Context;
//import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import a240.familymap.Activities.AppData;
import a240.familymap.R;
import a240.familymap.Tasks.LoginTask;
import a240.familymap.Tasks.SyncTask;
import a240.familymap.Tasks.RegisterTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends android.support.v4.app.Fragment implements LoginTask.Context, SyncTask.Context, RegisterTask.Context
{
    private EditText mServerPortInput, mServerHostInput, mUserNameInput, mPasswordInput, mFirstNameInput, mLastNameInput, mEmailInput;
    //private RadioButton mMaleRadio, mFemaleRadio;
    private RadioGroup mGenderRadio;
    private Button mSignInButton, mRegisterButton;

    // Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
   /* private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

    // Rename and change types of parameters
    /*private String mParam1;
    private String mParam2;*/

    private OnFragmentInteractionListener mListener;

    public Login()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Login.
     */
    public static Login newInstance(/*String param1, String param2*/)
    {
       Login fragment = new Login();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/

        AppData appData = AppData.getInstance();

        appData.setLifeStoryLine(AppData.COLOR_GREEN_ARGB);
        appData.setSpouseLineColor(AppData.COLOR_BLUE_ARGB);
        appData.setFamilyTreeLineColor(AppData.COLOR_BLACK_ARGB);
        appData.setShowFamilyTreeLines(true);
        appData.setShowLifeStoryLine(true);
        appData.setShowSpouseLines(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mServerPortInput = /*(EditText)*/ v.findViewById(R.id.serverPortInput);
        mServerPortInput.addTextChangedListener(manageLoginButtons);

        mServerHostInput = /*(EditText)*/ v.findViewById(R.id.serverHostInput);
        mServerHostInput.addTextChangedListener(manageLoginButtons);

        mUserNameInput = /*(EditText)*/ v.findViewById(R.id.userNameInput);
        mUserNameInput.addTextChangedListener(manageLoginButtons);

        mPasswordInput = /*(EditText)*/ v.findViewById(R.id.passwordInput);
        mPasswordInput.addTextChangedListener(manageLoginButtons);

        mFirstNameInput = /*(EditText)*/ v.findViewById(R.id.firstNameInput);
        mFirstNameInput.addTextChangedListener(manageLoginButtons);

        mLastNameInput = /*(EditText)*/ v.findViewById(R.id.lastNameInput);
        mLastNameInput.addTextChangedListener(manageLoginButtons);

        mEmailInput = /*(EditText)*/ v.findViewById(R.id.emailInput);
        mEmailInput.addTextChangedListener(manageLoginButtons);

        mGenderRadio = /*(RadioGroup)*/ v.findViewById(R.id.genderRadio);
        mGenderRadio.setOnCheckedChangeListener(ManageLoginButtons);

        mSignInButton = /*(Button)*/ v.findViewById(R.id.signInButton);
        mSignInButton.setEnabled(false);
        mSignInButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signIn();
            }
        });

        mRegisterButton = /*(Button)*/ v.findViewById(R.id.registerButton);
        mRegisterButton.setEnabled(false);
        mRegisterButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                register();
            }
        });

        return v;
    }

    private void register()
    {
        //grab info from host, port, username, password, firstname, lastname, email, gender and send through server proxy to login to server.
        String serverHostString = mServerHostInput.getText().toString();
        int portInt = Integer.valueOf(mServerPortInput.getText().toString());
        String userNameString = mUserNameInput.getText().toString();
        String passwordString = mPasswordInput.getText().toString();
        String firstNameString = mFirstNameInput.getText().toString();
        String lastNameString = mLastNameInput.getText().toString();
        String emailString = mEmailInput.getText().toString();
        int genderInt = mGenderRadio.getCheckedRadioButtonId();
        String genderString = genderInt == R.id.maleRadio ? getString(R.string.maleConst) : getString(R.string.femaleConst);

        RegisterTask registerTask = new RegisterTask(this);

        registerTask.execute(serverHostString, String.valueOf(portInt), userNameString, passwordString, firstNameString, lastNameString, emailString, genderString);
    }
    private void signIn()
    {
        //grab info from host, port, username, password and send through server proxy to login to server.
        String serverHostString = mServerHostInput.getText().toString();
        int portInt = Integer.valueOf(mServerPortInput.getText().toString());
        String userNameString = mUserNameInput.getText().toString();
        String passwordString = mPasswordInput.getText().toString();

        LoginTask loginTask = new LoginTask(this);

        loginTask.execute(serverHostString, String.valueOf(portInt), userNameString, passwordString);
        }


    @Override
    public void onLoginComplete(List<String> responseStrings)
    {
        if(responseStrings == null)
        {
            Toast.makeText(this.getContext(), R.string.signInFailedText, Toast.LENGTH_SHORT).show();
        }
        else
        {
            AppData appData = AppData.getInstance();

            String serverHostString = mServerHostInput.getText().toString();
            int portInt = Integer.valueOf(mServerPortInput.getText().toString());
            String authToken = responseStrings.get(0);
            String personID = responseStrings.get(2);
                //get userModel
            appData.setServerHost(serverHostString);
            appData.setPort(portInt);
            appData.setAuthToken(authToken);
            appData.setUserPersonID(personID);

            Toast.makeText(this.getContext(), "Login successful", Toast.LENGTH_SHORT).show();

            SyncTask syncTask = new SyncTask(this);
            //eventtask
            //syncTask.execute(true/*serverHostString, String.valueOf(portInt), authToken, personID*/);
            syncTask.execute(false);
            //eventtask.execute();

        }
    }

    @Override
    public void onSyncComplete(/*List<PersonModel> people*/Boolean success)
    {
        if(success.equals(false)/*people == null*/)
        {
            Toast.makeText(this.getContext(), R.string.signInFailedText, Toast.LENGTH_SHORT).show();
        }

        else
        {
            syncComplete();
        }
    }

    private boolean dataForSignin()
    {
        try
        {
            int portInt = Integer.valueOf(mServerPortInput.getText().toString());
        }
        catch(NumberFormatException ex)
        {
            return false;
        }

        String serverHostString = mServerHostInput.getText().toString();
        String userNameString = mUserNameInput.getText().toString();
        String passwordString = mPasswordInput.getText().toString();
        if(serverHostString.isEmpty() || userNameString.isEmpty() || passwordString.isEmpty())
        {
            return false;
        }

        return true;
    }

    private RadioGroup.OnCheckedChangeListener ManageLoginButtons = new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i)
        {
            if(dataForRegister())
            {
                mRegisterButton.setEnabled(true);
                mSignInButton.setEnabled(false);
            }
            else if(dataForSignin())
            {
                mRegisterButton.setEnabled(false);
                String firstNameString = mFirstNameInput.getText().toString();
                String lastNameString = mLastNameInput.getText().toString();
                String emailString = mEmailInput.getText().toString();
                if(!firstNameString.isEmpty() || !lastNameString.isEmpty() || !emailString.isEmpty())
                {
                    mSignInButton.setEnabled(false);
                }
                else
                {
                    mSignInButton.setEnabled(true);
                }
            }
            else
            {
                mRegisterButton.setEnabled(false);
                mSignInButton.setEnabled(false);
            }
        }
    };

    private TextWatcher manageLoginButtons = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        @Override
        public void afterTextChanged(Editable editable)
        {
            if(dataForRegister())
            {
                mRegisterButton.setEnabled(true);
                mSignInButton.setEnabled(false);
            }
            else if(dataForSignin())
            {
                mRegisterButton.setEnabled(false);
                String firstNameString = mFirstNameInput.getText().toString();
                String lastNameString = mLastNameInput.getText().toString();
                String emailNameString = mEmailInput.getText().toString();

                if(!firstNameString.isEmpty() || !lastNameString.isEmpty() || !emailNameString.isEmpty())
                {
                    mSignInButton.setEnabled(false);
                }
                else
                {
                    mSignInButton.setEnabled(true);
                }
            }
            else
            {
                mRegisterButton.setEnabled(false);
                mSignInButton.setEnabled(false);
            }
        }
    };

    private boolean dataForRegister()
    {
        String firstNameString = mFirstNameInput.getText().toString();
        String lastNameString = mLastNameInput.getText().toString();
        String emailString = mEmailInput.getText().toString();
        int genderInt = mGenderRadio.getCheckedRadioButtonId();

        if(!dataForSignin() || firstNameString.isEmpty() || lastNameString.isEmpty() || emailString.isEmpty() || genderInt == -1)
        {
            return false;
        }

        return true;
    }

    public void syncComplete()/*onButtonPressed(Uri uri)*/
    {
        if (mListener != null)
        {
            mListener.transferToTLMap();
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRegisterComplete(/*List<String> responseStrings*/Boolean success)
    {
        if(!success/*responseStrings == null*/)
        {
            Toast.makeText(this.getContext(), "Registration Failed!", Toast.LENGTH_SHORT).show();
        }

        //will change this later to load all data and switch to map fragment.
        else
        {
            /*
            String firstName = responseStrings.get(2);
            String lastName = responseStrings.get(3);

            Toast.makeText(this.getContext(), firstName + " " + lastName, Toast.LENGTH_LONG).show();*/
            Toast.makeText(this.getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
            SyncTask syncTask = new SyncTask(this);

            syncTask.execute(false);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        //void onFragmentInteraction(Uri uri);
        void transferToTLMap();
    }
}
