package model;

/**
 * Created by Florian on 30/04/2017.
 */
public class Person {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Person(String pName){
        this.name = pName;
    }

}
