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

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getZip() {
        return zip;
    }

    public String getEmployer() {
        return employer;
    }
}
