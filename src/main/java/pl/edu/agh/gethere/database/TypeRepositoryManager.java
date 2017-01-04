package pl.edu.agh.gethere.database;

import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import pl.edu.agh.gethere.model.Triple;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SG0222581 on 1/4/2017.
 */
public class TypeRepositoryManager extends RepositoryManager {

    public TypeRepositoryManager() {
        super();
    }

    public List<String> getTypes() {
        StringBuilder typeQuery = new StringBuilder();
        typeQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        typeQuery.append("SELECT ?s WHERE { ?s gethere:isSubclassOf gethere:Location . }");
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, typeQuery.toString());
        TupleQueryResult result = tupleQuery.evaluate();

        List<String> types = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String subject = bindingSet.getValue("s").stringValue();
            types.add(subject.replace(GETHERE_URL, ""));
        }
        return types;
    }

    public boolean checkIfTypeExists(String type) {
        List<String> types = getTypes();
        return types.contains(type);
    }

    public void addTypeDefinition(String definition) {
        String locationIri = GETHERE_URL + "Location";
        Triple type = new Triple(GETHERE_URL + definition, SUBCLASS_PREDICATE, locationIri);
        addStatement(type);
    }
}
