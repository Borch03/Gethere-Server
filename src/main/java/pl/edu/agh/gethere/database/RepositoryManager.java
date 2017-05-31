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
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.sail.nativerdf.NativeStore;
import org.springframework.stereotype.Component;
import pl.edu.agh.gethere.model.Poi;
import pl.edu.agh.gethere.model.Triple;
import pl.edu.agh.gethere.model.User;
import pl.edu.agh.gethere.util.TupleParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 16.04.2016.
 */

@Component
public abstract class RepositoryManager {

    public final static String GETHERE_URL = "http://gethere.agh.edu.pl/#";
    public final static String TYPE_PREDICATE = GETHERE_URL + "isTypeOf";
    public final static String SUBCLASS_PREDICATE = GETHERE_URL + "isSubclassOf";
    public final static String SESAME_FILE = "/tmp/sesame/";

    final static Logger logger = Logger.getLogger(RepositoryManager.class);

    protected Repository repository;
    protected RepositoryConnection connection;
    protected RepositoryConfigurator repositoryConfigurator;

    public RepositoryManager() {
        try {
//            this.repositoryConfigurator = new RepositoryConfigurator();
//            this.repository = new HTTPRepository(repositoryConfigurator.getSesameServer(), repositoryConfigurator.getRepositoryID());
            File dataDir = new File(SESAME_FILE);
            repository = new SailRepository(new NativeStore(dataDir));
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

    public Repository getRepository() {
        return this.repository;
    }

    public RepositoryConnection getConnection() {
        return this.connection;
    }
}
