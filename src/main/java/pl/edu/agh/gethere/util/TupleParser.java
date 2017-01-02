package pl.edu.agh.gethere.util;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import pl.edu.agh.gethere.model.Poi;
import pl.edu.agh.gethere.model.User;
import pl.edu.agh.gethere.model.UserRole;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dominik on 20.10.2016.
 */
public class TupleParser {

    public final static String GETHERE_URL = "http://gethere.agh.edu.pl/#";
    public final static String TYPE_PREDICATE = "isTypeOf";
    public final static String NAME_PREDICATE = "hasName";
    public final static String COORDINATES_PREDICATE = "hasCoordinates";
    public final static String EMAIL_PREDICATE = "hasEmail";
    public final static String ROLE_PREDICATE = "hasRole";

    final static Logger logger = Logger.getLogger(TupleParser.class);

    public List<String> parsePoiList(TupleQueryResult result) {
        List<String> poiIds = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String subject = bindingSet.getValue("s").stringValue();
            poiIds.add(subject);
        }
        return poiIds;
    }

    public Poi parsePoi(TupleQueryResult result, String id) {
        Poi poi = new Poi();
        poi.setId(id);
        HashMap<String, String> attributes = new HashMap<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String predicate = bindingSet.getValue("p").stringValue().replace(GETHERE_URL, "");
            String object = bindingSet.getValue("o").stringValue();
            if (predicate.equals(NAME_PREDICATE)) {
                poi.setName(object);
            } else if (predicate.equals(TYPE_PREDICATE)) {
                poi.setType(object.replace(GETHERE_URL, ""));
            } else if (predicate.equals(COORDINATES_PREDICATE)) {
                poi.setCoordinates(object);
            } else if (predicate.matches("has.*Info")) {
                attributes.put(predicate.replace("(^has)|(Info$)",""), object);
            } else {
                logger.warn("Unrecognized POI parameter: " + predicate);
            }
        }
        poi.setAttributes(attributes);
        return ((poi.getName() != null) && (poi.getType() != null) && (poi.getCoordinates() != null)) ? poi : null;
    }

    public User parseUser(TupleQueryResult result, String id) {
        User user = new User();
        user.setId(id);
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String predicate = bindingSet.getValue("p").stringValue().replace(GETHERE_URL, "");
            String object = bindingSet.getValue("o").stringValue();
            if (predicate.equals(NAME_PREDICATE)) {
                user.setUsername(object);
            } else if (predicate.equals(EMAIL_PREDICATE)) {
                user.setEmail(object);
            } else if (predicate.equals(ROLE_PREDICATE)) {
                if (object.equals(UserRole.ROLE_ADMIN.toString())) {
                    user.setRole(UserRole.ROLE_ADMIN);
                } else if (object.equals(UserRole.ROLE_USER.toString())) {
                    user.setRole(UserRole.ROLE_USER);
                }
            } else {
                logger.warn("Unrecognized User parameter: " + predicate);
            }
        }
        return ((user.getUsername() != null) && (user.getEmail() != null) && (user.getRole() != null)) ? user : null;

    }
}
