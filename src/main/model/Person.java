package model;

import model.enums.Gender;
import org.json.JSONObject;
import persistence.Writable;

//The Person class holds general pieces of information of a real person
// e.g: id, name, age, gender and is COVID-19 positive
public abstract class Person implements Traceable, Writable {
    //One person can only have one id
    protected final int id;
    protected String name;
    protected int age;
    protected Gender gender;
    protected boolean isCovidPositive;

    // EFFECTS: init a Person with age, gender, id and status of COVID-19 Test
    public Person(int age, String name, Gender gender, int id, boolean isCovidPositive) {
        this.age = age;
        this.gender = gender;
        this.name = name;
        this.id = id;
        this.isCovidPositive = isCovidPositive;
    }


    public int getAge() {
        return age;
    }


    public Gender getGender() {
        return gender;
    }



    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // REQUIRES: name is not null
    // MODIFIES: this.name
    public void setName(String name) {
        this.name = name;
    }

    // MODIFIES: this
    // EFFECTS: set this person to be COVID-19 Positive
    public void declarePositive() {
        isCovidPositive = true;
    }

    // MODIFIES: this
    // EFFECTS: set this person to be COVID-19 negative
    public void declareNegative() {
        isCovidPositive = false;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{"
                + "Name=" + name
                + ", id=" + id
                + ", age=" + age
                + ", isCovidPositive=" + isCovidPositive
                + '}';
    }

    // EFFECTS:
    @Override
    public boolean getCovidStatus() {
        return isCovidPositive;
    }

    // REQUIRES: o is not a null object
    // EFFECTS: return true if two people have the same id, false otherwise
    @Override
    public boolean equals(Object o) {
        if (o instanceof Person) {
            Person person = (Person) o;
            return getId() == person.getId();
        }
        return false;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("id",  id);
        json.put("name",  name);
        json.put("age",  age);
        json.put("gender",  gender);
        json.put("isCovidPositive",  isCovidPositive);
        return json;
    }

}
