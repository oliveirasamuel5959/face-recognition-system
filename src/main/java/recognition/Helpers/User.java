package recognition.Helpers;

public class User {

    private String userFirstName;
    private String userLastName;
    private String userEmail;
    private String userJobId;
    private Integer roleLevel;

    public User() {

    }

    public User(String userFirstName, String userLastName, String userJobId, String userEmail, Integer roleLevel) {
        this.userFirstName = userFirstName;
        this.userLastName = userLastName;
        this.userJobId = userJobId;
        this.userEmail =userEmail;
        this.roleLevel = roleLevel;
    }

    public String getUserFirstName() {
        return this.userFirstName;
    }

    public String getUserLastName() {
        return this.userLastName;
    }

    public String getUserJobId() {
        return this.userJobId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Integer getRoleLevel() {
        return roleLevel;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public void setUserJobId(String userJobId) {
        this.userJobId = userJobId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setRoleLevel(Integer roleLevel) {
        this.roleLevel = roleLevel;
    }

    public Boolean checkEmptyFields() {
        if(this.userFirstName.isEmpty() || this.userLastName.isEmpty() || this.userJobId.isEmpty() || this.roleLevel == 0 || this.userEmail.isEmpty())
            return false;
        else
            return true;
    }

}
