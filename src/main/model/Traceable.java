package model;

//This interface is used to trace the exposure of COVID-19 in school
public interface Traceable {
    // EFFECTS: Declare a positive exposure for the implemented object
    void declarePositive();

    // EFFECTS: Declare a negative exposure for the implemented object
    void declareNegative();

    // EFFECTS: return the Covid-19 status of this object
    boolean getCovidStatus();
}
