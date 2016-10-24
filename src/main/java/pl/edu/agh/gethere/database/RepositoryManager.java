package pl.edu.agh.gethere.database;

import org.apache.log4j.Logger;
import org.openrdf.model.IRI;
import org.openrdf.model.Literal;
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
import pl.edu.agh.gethere.util.TupleParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 16.04.2016.
 */
public class RepositoryManager {

    public final static String GETHERE_URL = "http://gethere.agh.edu.pl/#";
    public final static String ADDITIONAL_INFO_SUBJECT_IRI = "http://gethere.agh.edu.pl/#additionalInfo";
    public final static String ADDITIONAL_INFO_PREDICATE_IRI = "http://gethere.agh.edu.pl/#hasValue";

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

    public void addAdditionalInfoDefinition(String definition) {
        ValueFactory factory = SimpleValueFactory.getInstance();
        IRI subject = factory.createIRI(ADDITIONAL_INFO_SUBJECT_IRI);
        IRI predicate = factory.createIRI(ADDITIONAL_INFO_PREDICATE_IRI);
        Literal object = factory.createLiteral(definition);
        connection.add(subject, predicate, object);
    }

    public List<String> getAdditionalInfoDefinitions() {
        String query = "SELECT ?o WHERE { <" + ADDITIONAL_INFO_SUBJECT_IRI + "> ?p ?o . }";
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, query);
        TupleQueryResult result = tupleQuery.evaluate();

        List<String> additionalInfoDefinitions = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String object = bindingSet.getValue("o").stringValue();
            additionalInfoDefinitions.add(object);
        }
        return additionalInfoDefinitions;
    }

    public List<Poi> getKeywordPois(String keyword) {
        StringBuilder poiListQuery = new StringBuilder();
        poiListQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        poiListQuery.append("SELECT ?s ?p ?o  WHERE { ?s ?p ?o .FILTER regex(str(?o), \"" + keyword + "\") .}");

        TupleQuery poisTupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, poiListQuery.toString());
        TupleQueryResult result = poisTupleQuery.evaluate();

        TupleParser tupleParser = new TupleParser();
        List<String> poiIds = tupleParser.parsePoiList(result);

        List<Poi> pois = new ArrayList<>();
        for (String poiId : poiIds) {
            StringBuilder poiQuery = new StringBuilder();
            poiQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
            poiQuery.append("SELECT ?s ?p ?o WHERE { <" + poiId + "> ?p ?o . }");
            TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, poiQuery.toString());
            TupleQueryResult poiResult = tupleQuery.evaluate();
            String id = poiId.replace(GETHERE_URL, "");
            if (tupleParser.parsePoi(poiResult, id) != null) {
                pois.add(tupleParser.parsePoi(poiResult, id));
            } else {
                logger.warn("POI with ID: " + id + " is incomplete. POI has been ignored.");
            }
        }
        return pois;
    }

    public Repository getRepository() {
        return this.repository;
    }

    public RepositoryConnection getConnection() {
        return this.connection;
    }
}
