package a240.familymap.Requests;

/**
 * Request object for registering new users.
 * @author ajw9001
 */
public class RegisterRequest
{
    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender)
    {
        this.userName = userName;
        this. password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public RegisterRequest(){}

    private String userName, password, email, firstName, lastName, gender;

    /**
     * @return User name.
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @return Password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * @return Email.
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * @return First name.
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * @return Last name.
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * @return Gender.
     */
    public String getGender()
    {
        return gender;
    }
}
