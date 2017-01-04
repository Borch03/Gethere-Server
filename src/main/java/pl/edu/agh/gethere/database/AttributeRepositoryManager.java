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
public class AttributeRepositoryManager extends RepositoryManager {

    public AttributeRepositoryManager() {
        super();
    }

    public List<String> getAttributeDefinitions() {
        StringBuilder attributeQuery = new StringBuilder();
        attributeQuery.append("PREFIX gethere: <" + GETHERE_URL + "> \n");
        attributeQuery.append("SELECT ?s WHERE { ?s gethere:isTypeOf gethere:Attribute . }");
        TupleQuery tupleQuery = connection.prepareTupleQuery(QueryLanguage.SPARQL, attributeQuery.toString());
        TupleQueryResult result = tupleQuery.evaluate();

        List<String> attributeDefinitions = new ArrayList<>();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            String object = bindingSet.getValue("s").stringValue();
            attributeDefinitions.add(object);
        }
        return attributeDefinitions;
    }

    public boolean checkIfAttributeExists(String attribute) {
        List<String> attributes = getAttributeDefinitions();
        return attributes.contains(attribute);
    }

    public void addAttributeDefinition(String definition) {
        String attributeIri = GETHERE_URL + "Attribute";
        Triple attributeDefinition = new Triple(GETHERE_URL + definition, TYPE_PREDICATE, attributeIri);
        addStatement(attributeDefinition);
    }
}
