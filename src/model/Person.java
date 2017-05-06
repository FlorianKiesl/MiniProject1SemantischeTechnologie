package model;

import java.util.Date;

/**
 * Created by Florian on 30/04/2017.
 */
public class Person {

    private enum Gender {
        M,
        W
    };

    private String name;

    private String address;

    private String country;

    private Gender gender;

    private String zipCode;

    private String city;

    private Date birthdate;

    private String employer;

    public String getName() {

        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
    }

    public Gender getGender() {
        return gender;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getEmployer() {
        return employer;
    }

    public void setName(String name) {

        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public Person(String pName){
        this(pName,"","", Gender.W, "","",null,"");
    }

    public Person(String name, String address, String country,
                  Gender gender, String zipCode, String city, Date birthdate, String employer) {
        this.name = name;
        this.address = address;
        this.country = country;
        this.gender = gender;
        this.zipCode = zipCode;
        this.city = city;
        this.birthdate = birthdate;
        this.employer = employer;
    }
}
