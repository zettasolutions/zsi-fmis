package com.zetta.afcs;

public class User {
    public String FirstName;
    public String LastName;
    public String Birthday;
    public String Username;
    public String Password;
    public Boolean IsAuthenticated;
    public String Position;
    public String Driver;
    public String Pao;


    public User() {
    }

    public void setFirstName(String firstName) {
        this.FirstName = firstName;
    }

    public String getFirstName() {
        return this.FirstName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getLastName() {
        return this.LastName;
    }

    public void setBirthday(String birthday) {
        this.Birthday = birthday;
    }

    public String getBirthday() {
        return this.Birthday;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getUsername() {
        return this.Username;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getPassword() {
        return this.Password;
    }

    public void setIsAuthenticated(Boolean isAuthenticated) { this.IsAuthenticated = isAuthenticated; }
    public Boolean getIsAuthenticated() {
        return this.IsAuthenticated;
    }

    public void setPosition(String position) {
        this.Position = position;
    }
    public String getPosition() {
        return this.Position;
    }

    public void setDriver(String driver) {
        this.Driver = driver;
    }
    public String getDriver() {
        return this.Driver;
    }

    public void setPao(String pao) {
        this.Pao = pao;
    }
    public String getPao() {
        return this.Pao;
    }
}
