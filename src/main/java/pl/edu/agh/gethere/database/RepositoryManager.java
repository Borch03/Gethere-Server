package pl.edu.agh.gethere.database;

import org.apache.log4j.Logger;
import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

/**
 * Created by Dominik on 16.04.2016.
 */
public class RepositoryManager {

    final static Logger logger = Logger.getLogger(RepositoryConfigurator.class);

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

    public Repository getRepository() {
        return this.repository;
    }

    public RepositoryConnection getConnection() {
        return this.connection;
    }
}
