package pl.edu.agh.gethere.database;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Created by Dominik on 16.04.2016.
 */
public class RepositoryConfigurator {

    public static final String PROPERTIES_FILE = "sesame.properties";
    final static Logger logger = Logger.getLogger(RepositoryConfigurator.class);

    private String sesameServer;
    private String repositoryID;

    public RepositoryConfigurator() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Properties prop = new Properties();
        InputStream input = null;
        try {
            prop.load(new FileReader(PROPERTIES_FILE));
            sesameServer = prop.getProperty("sesameServer");
            repositoryID = prop.getProperty("repositoryID");
            logger.info("Successfully get sesame properties");
        } catch (IOException e) {
            logger.error(e.getClass().getSimpleName() + " Cannot load sesame properties");
            e.printStackTrace();
        }
    }

    public String getSesameServer() {
        return sesameServer;
    }

    public String getRepositoryID() {
        return repositoryID;
    }

    public void setSesameServer(String str) {
        setProperty("sesameServer", str);
    }

    public void setRepositoryId(String str) {
        setProperty("repositoryID", str);
    }

    private void setProperty(String name, String value) {
        Properties prop = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        OutputStream output = null;

        try {
            URL url = loader.getResource("db.properties");
            output = new FileOutputStream(new File(url.toURI()));
            // set the properties value
            prop.setProperty(name, value);
            // save properties to project root folder
            prop.store(output, null);
        } catch (IOException e) {
            logger.error(e.getClass().getSimpleName() + " Cannot open sesame properties");
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    logger.error(e.getClass().getSimpleName() + " Cannot close properties file");
                    e.printStackTrace();
                }
            }
        }
    }
}
