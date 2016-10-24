package pl.edu.agh.gethere.model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

/**
 * Created by Dominik on 18.06.2016.
 */

@XmlRootElement(namespace = "pl.edu.agh.gethere.poi")
public class Poi {

    private String id;
    private String name;
    private String type;
    private String coordinates;
    private HashMap<String, String> additionalInfo;

    public Poi(String id, String name, String type, String coordinates, HashMap<String, String> additionalInfo) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.coordinates = coordinates;
        this.additionalInfo = additionalInfo;
    }

    public Poi() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public HashMap<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(HashMap<String, String> additionalInfo) {
        this.additionalInfo = additionalInfo;
    }
}
