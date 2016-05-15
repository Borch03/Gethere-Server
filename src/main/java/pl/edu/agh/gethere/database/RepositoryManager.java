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
import pl.edu.agh.gethere.model.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 16.04.2016.
 */
public class RepositoryManager {

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
        Literal object = factory.createLiteral(triple.getObject());

        connection.add(subject, predicate, object);
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

    public Repository getRepository() {
        return this.repository;
    }

    public RepositoryConnection getConnection() {
        return this.connection;
    }
}
