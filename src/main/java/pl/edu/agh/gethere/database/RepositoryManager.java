package pl.edu.agh.gethere.database;

import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.SimpleValueFactory;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import pl.edu.agh.gethere.model.Poi;
import pl.edu.agh.gethere.model.Triple;

import javax.ws.rs.GET;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 16.04.2016.
 */
public class RepositoryManager {

    public final static String GETHERE_URL = "http://gethere.agh.edu.pl/#";

    public final static String TYPE_IRI = GETHERE_URL+ "isTypeOf";
    public final static String NAME_IRI = GETHERE_URL+ "hasName";
    public final static String CITY_IRI = GETHERE_URL+ "isInCity";
    public final static String STREET_IRI = GETHERE_URL+ "isOnStreet";
    public final static String NUMBER_IRI = GETHERE_URL+ "hasNumber";
    public final static String COORDINATES_IRI = GETHERE_URL+ "hasCoordinates";

    final static Logger logger = Logger.getLogger(RepositoryManager.class);

    private Repository repository;
    private RepositoryConnection connection;
    private RepositoryConfigurator repositoryConfigurator;

    public RepositoryManager() {
        try {
            this.repositoryConfigurator = new RepositoryConfigurator();
            this.repository = new HTTPRepository(repositoryConfigurator.getSesameServer(), repositoryConfigurator.getRepositoryID());
            this.repository.initialize();
            this.connection = repository.getConnection();
            logger.info("Successfully connected to Sesame Repository");
        } catch (RepositoryException e) {
            logger.error(e.getClass().getSimpleName() + " Cannot connect to sesame repository");
            e.printStackTrace();
        }
    }

    public void tearDown() {
        try {
            this.connection.close();
            this.repository.shutDown();
            logger.info("Successfully closed connection to Sesame Repository");
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

    public void addStatement(Triple triple) {
        ValueFactory factory = SimpleValueFactory.getInstance();
        IRI subject = factory.createIRI(triple.getSubject());
        IRI predicate = factory.createIRI(triple.getPredicate());
        if (triple.getObject().contains(GETHERE_URL)) {
            IRI object = factory.createIRI(triple.getObject());
            connection.add(subject, predicate, object);
        } else {
            Literal object = factory.createLiteral(triple.getObject());
            connection.add(subject, predicate, object);
        }
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

    public List<Poi> getKeywordPois(String keyword) {
        String poisQuery = "SELECT ?s ?p ?o  WHERE { ?s ?p ?o .FILTER regex(str(?o), \"" + keyword + "\") .}";

        TupleQuery poisTupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, poisQuery);
        TupleQueryResult result = poisTupleQuery.evaluate();

        List<String> poiIds = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String subject = bindingSet.getValue("s").stringValue();
            poiIds.add(subject);
        }
        List<Poi> pois = new ArrayList<>();
        for (String poiId : poiIds) {
            String query = "SELECT ?s ?p ?o WHERE { <" + poiId + "> ?p ?o . }";
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
            TupleQueryResult poiResult = tupleQuery.evaluate();

            pois.add(createPoi(poiResult));
        }
        return pois;
    }

    private Poi createPoi(TupleQueryResult result) {
        String name = null;
        String type = null;
        String city = null;
        String street = null;
        String number = null;
        String coordinates = null;
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String predicate = bindingSet.getValue("p").stringValue();
            String object = bindingSet.getValue("o").stringValue();
            if (predicate.equals(NAME_IRI)) {
                name = object;
            } else if (predicate.equals(TYPE_IRI)) {
                type = object;
            } else if (predicate.equals(CITY_IRI)) {
                city = object;
            } else if (predicate.equals(STREET_IRI)) {
                street = object;
            } else if (predicate.equals(NUMBER_IRI)) {
                number = object;
            } else if (predicate.equals(COORDINATES_IRI)) {
                coordinates = object;
            }
        }
        return new Poi(name, type, city, street, number, coordinates);
    }

    public Repository getRepository() {
        return this.repository;
    }

    public RepositoryConnection getConnection() {
        return this.connection;
    }
}
