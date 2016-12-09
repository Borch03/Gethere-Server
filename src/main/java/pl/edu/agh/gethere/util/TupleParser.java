package pl.edu.agh.gethere.util;

import org.apache.log4j.Logger;
import org.openrdf.query.BindingSet;
import org.openrdf.query.TupleQueryResult;
import pl.edu.agh.gethere.model.Poi;

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
}
