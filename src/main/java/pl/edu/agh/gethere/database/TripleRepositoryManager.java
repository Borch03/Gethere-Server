package pl.edu.agh.gethere.database;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import pl.edu.agh.gethere.model.Poi;
import pl.edu.agh.gethere.model.Triple;
import pl.edu.agh.gethere.util.TupleParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by SG0222581 on 1/4/2017.
 */
public class TripleRepositoryManager extends RepositoryManager {

    public TripleRepositoryManager() {
        super();
    }

    public List<Triple> getAllTriples() {
        String query = "SELECT DISTINCT ?s ?p ?o WHERE { ?s ?p ?o }";
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
        TupleQueryResult result = tupleQuery.evaluate();

        List<Triple> triples = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String subject = bindingSet.getValue("s").stringValue();
            String predicate = bindingSet.getValue("p").stringValue();
            String object = bindingSet.getValue("o").stringValue();
            triples.add(new Triple(subject, predicate, object));
        }
        return triples;
    }

    public List<Poi> filterPois(Map<String, String> filters) {
        StringBuilder filterPoiQuery = new StringBuilder();
        filterPoiQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        filterPoiQuery.append("SELECT ?s ?p ?o WHERE { \n");
        String type = filters.get("type");
        filters.remove("type");
        String openTime = null;
        if (filters.containsKey("openTime")) {
            openTime = filters.get("openTime");
            filterPoiQuery.append("?s gethere:hasOpeningHours ?openTime . \n");
            filters.remove("openTime");
        }
        String radius = null;
        String location = null;
        if (filters.containsKey("radius") && filters.containsKey("location")) {
            radius = filters.get("radius");
            filters.remove("radius");
            location = filters.get("location");
            filters.remove("location");
        }
        for (Map.Entry<String, String> entry : filters.entrySet()) {
            filterPoiQuery.append("?s gethere:has" + entry.getKey() + "Info \"" + entry.getValue() + "\" . \n");
        }
        filterPoiQuery.append("?s gethere:isTypeOf gethere:" + type + " . }");
        TupleQuery poisTupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, filterPoiQuery.toString());
        TupleQueryResult result = poisTupleQuery.evaluate();

        List<Poi> listOfPois = parseQueryResultToPoiList(result);
        listOfPois = filterByCoordinates(listOfPois, location, radius);
        listOfPois = filterByTime(listOfPois, openTime);
        return listOfPois;
    }

    public List<Poi> getKeywordPois(String keyword) {
        StringBuilder poiListQuery = new StringBuilder();
        poiListQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        poiListQuery.append("SELECT ?s ?p ?o WHERE { \n");
        poiListQuery.append("?s gethere:isTypeOf ?type . \n");
        poiListQuery.append("?type gethere:isSubclassOf gethere:Location . \n");
        poiListQuery.append("?s ?p ?o .FILTER regex(str(?o), \"" + keyword + "\") . }");

        TupleQuery poisTupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, poiListQuery.toString());
        TupleQueryResult result = poisTupleQuery.evaluate();

        return parseQueryResultToPoiList(result);
    }

    private List<Poi> parseQueryResultToPoiList(TupleQueryResult result) {
        TupleParser tupleParser = new TupleParser();
        List<String> poiIds = tupleParser.parsePoiList(result);

        List<Poi> pois = new ArrayList<>();
        for (String poiId : poiIds) {
            StringBuilder poiQuery = new StringBuilder();
            poiQuery.append("SELECT ?s ?p ?o WHERE { \n");
            poiQuery.append("<" + poiId + "> ?p ?o . \n");
            poiQuery.append("?s ?p ?o .FILTER regex(str(?p), \"^" + GETHERE_URL + "\") . }");
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, poiQuery.toString());
            TupleQueryResult poiResult = tupleQuery.evaluate();
            String id = poiId.replace(GETHERE_URL, "");
            Poi poi = tupleParser.parsePoi(poiResult, id);
            if (poi != null) {
                pois.add(poi);
            } else {
                logger.warn("POI with ID: " + id + " is incomplete. POI has been ignored.");
            }
        }
        return pois;
    }

    private List<Poi> filterByTime(List<Poi> listOfPois, String openTimeString) {
        if (openTimeString != null) {
            long openTime = Long.valueOf(openTimeString);
            for (Poi poi : listOfPois) {
                long openingHour = Long.valueOf(poi.getOpeningHours().substring(0, poi.getOpeningHours().indexOf(";")));
                long closingHour = Long.valueOf(poi.getOpeningHours().substring(
                        poi.getOpeningHours().indexOf(";") + 1, poi.getOpeningHours().length()));
                if (closingHour < openingHour) {
                    closingHour += 24 * 3600 * 1000;
                }
                if (!((openTime >= openingHour) && (openTime <= closingHour)) ||
                        !(((openTime + 24 * 3600 * 1000) >= openingHour) && ((openTime + 24 * 3600 * 1000) <= closingHour))) {
                    listOfPois.remove(poi);
                }
            }
        }
        return listOfPois;
    }

    private List<Poi> filterByCoordinates(List<Poi> listOfPois, String locationString, String radiusString) {
        if ((radiusString != null) && (locationString != null)) {
            double radius = Double.valueOf(radiusString);
            double locationLatitude = Double.valueOf(locationString.substring(0, locationString.indexOf(";")));
            double locationLongitude = Double.valueOf(locationString.substring(
                    locationString.indexOf(";") + 1, locationString.length()));
            for (Poi poi : listOfPois) {
                double poiLatitude = Double.valueOf(poi.getCoordinates().substring(0, poi.getCoordinates().indexOf(";")));
                double poiLongitude = Double.valueOf(poi.getCoordinates().substring(
                        poi.getCoordinates().indexOf(";") + 1, poi.getCoordinates().length()));
                double distance = getDistanceInKilometers(locationLatitude, locationLongitude, poiLatitude, poiLongitude);
                if (distance > radius) {
                    listOfPois.remove(poi);
                }
            }
        }
        return listOfPois;
    }

    private double getDistanceInKilometers(double latitude1, double longitude1,double latitude2, double longitude2) {
        double R = 6371;
        double dLat = deg2rad(latitude2 - latitude1);
        double dLon = deg2rad(longitude2 - longitude1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

}
