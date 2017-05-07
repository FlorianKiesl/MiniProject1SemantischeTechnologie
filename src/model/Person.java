package model;

public class Person {

    private String name;
    private Gender gender;
    private int age;
    private String country;
    private String city;
    private String address;
    private int zip;
    private String employer;

    public  Person(){
        this("", Gender.MALE, 0, "", "", "", 0, "");
    }

    public Person(String name, Gender gender, int age, String country, String city, String address, int zip, String employer) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.country = country;
        this.city = city;
        this.address = address;
        this.zip = zip;
        this.employer = employer;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }
}
