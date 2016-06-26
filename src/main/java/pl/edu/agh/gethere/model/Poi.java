package pl.edu.agh.gethere.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Dominik on 18.06.2016.
 */

@XmlRootElement(namespace = "pl.edu.agh.gethere.poi")
public class Poi {

    private String name;
    private String type;
    private String city;
    private String street;
    private String number;
    private String coordinates;

    public Poi(String name, String type, String city, String street, String number, String coordinates) {
        this.name = name;
        this.type = type;
        this.city = city;
        this.street = street;
        this.number = number;
        this.coordinates = coordinates;
    }

    public Poi() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }
}
