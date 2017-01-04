package pl.edu.agh.gethere.database;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import pl.edu.agh.gethere.model.Triple;
import pl.edu.agh.gethere.model.User;
import pl.edu.agh.gethere.util.TupleParser;

/**
 * Created by SG0222581 on 1/4/2017.
 */
public class UserRepositoryManager extends RepositoryManager {

    public UserRepositoryManager() {
        super();
    }

    public User getUserByName(String username) {
        if (username == null) {
            return null;
        }
        StringBuilder userIdQuery = new StringBuilder();
        userIdQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        userIdQuery.append("SELECT ?id ?p ?o WHERE { \n");
        userIdQuery.append("?id gethere:isTypeOf gethere:User . \n");
        userIdQuery.append("?id gethere:hasName ?o .FILTER regex(str(?o), \"" + username + "\") . }");
        TupleQuery tupleUserIdQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, userIdQuery.toString());
        TupleQueryResult result = tupleUserIdQuery.evaluate();
        BindingSet bindingSet = result.next();
        String userId = bindingSet.getValue("id").stringValue();

        StringBuilder userQuery = new StringBuilder();
        userQuery.append("SELECT ?s ?p ?o WHERE { \n");
        userQuery.append("<" + userId + "> ?p ?o . \n");
        userQuery.append("?s ?p ?o .FILTER regex(str(?p), \"^" + GETHERE_URL + "\") . }");
        TupleQuery tupleUserQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, userQuery.toString());
        TupleQueryResult userResult = tupleUserQuery.evaluate();
        String id = userId.replace(GETHERE_URL, "");
        TupleParser tupleParser = new TupleParser();
        return tupleParser.parseUser(userResult, id);
    }

    public boolean checkIfUserExists(String username) {
        StringBuilder usernameQuery = new StringBuilder();
        usernameQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        usernameQuery.append("SELECT ?id ?p ?o WHERE { \n");
        usernameQuery.append("?id gethere:isTypeOf gethere:User . \n");
        usernameQuery.append("?id gethere:hasName ?o .FILTER regex(str(?o), \"" + username + "\") . }");
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, usernameQuery.toString());
        TupleQueryResult result = tupleQuery.evaluate();
        return result.hasNext();
    }

    public boolean isUserAuthenticated(String name, String password) {
        StringBuilder authQuery = new StringBuilder();
        authQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        authQuery.append("SELECT ?s ?p ?o WHERE { \n");
        authQuery.append("?id gethere:isTypeOf gethere:User . \n");
        authQuery.append("?id gethere:hasName ?o .FILTER regex(str(?o), \"" + name + "\") . \n");
        authQuery.append("?id gethere:hasPassword ?o .FILTER regex(str(?o), \"" + password + "\") . }");
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, authQuery.toString());
        TupleQueryResult result = tupleQuery.evaluate();
        return result.hasNext();
    }

    public void addUser(User user) {
        String userIri = GETHERE_URL + "User";
        String userIdIri = GETHERE_URL + user.getId();
        Triple userTriple = new Triple(userIdIri, TYPE_PREDICATE, userIri);
        addStatement(userTriple);
        Triple usernameTriple = new Triple(userIdIri, GETHERE_URL + "hasName", user.getUsername());
        addStatement(usernameTriple);
        Triple passwordTriple = new Triple(userIdIri, GETHERE_URL + "hasPassword", user.getPassword());
        addStatement(passwordTriple);
        Triple emailTriple = new Triple(userIdIri, GETHERE_URL + "hasEmail", user.getEmail());
        addStatement(emailTriple);
        Triple roleTriple = new Triple(userIdIri, GETHERE_URL + "hasRole", user.getRole().toString());
        addStatement(roleTriple);
    }

}
