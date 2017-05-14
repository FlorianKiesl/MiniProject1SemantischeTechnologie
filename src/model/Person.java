package model;

import java.util.Date;

public class Person {

    private String name;
    private Gender gender;
    private Date birthdate;
    private String country;
    private String city;
    private String address;
    private String zip;
    private String employer;
    private String ownsOrg;

    public  Person(){
        this("", Gender.MALE, new Date(), "", "", "", "", "", "");
    }

    public Person(String name, Gender gender, Date birthdate, String country, String city, String address, String zip, String employer, String ownsOrg) {
        this.name = name;
        this.gender = gender;
        this.birthdate = birthdate;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zip = zip;
        this.employer = employer;
        this.ownsOrg = ownsOrg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getOwnsOrg () { return ownsOrg; }

    public void setOwnsOrg (String ownsOrg) { this.ownsOrg = ownsOrg; }
}
