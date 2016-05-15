package pl.edu.agh.gethere.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by Dominik on 14.05.2016.
 */

@XmlRootElement(namespace = "pl.edu.agh.gethere.triple")
public class Triple {

    private String subject;
    private String predicate;
    private String object;

    public Triple(String subject, String predicate, String object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }
}
