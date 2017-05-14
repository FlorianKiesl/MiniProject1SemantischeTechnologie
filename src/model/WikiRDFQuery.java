package model;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.util.*;

/**
 * Created by Florian on 14/05/2017.
 */
public class WikiRDFQuery {

    public static Map<String, String> getCountries(){
        File queryFile = new File(WikiRDFQuery.class.getResource("wikidataquery.rq").getPath());
        Query query = QueryFactory.read(queryFile.getPath());
        Map<String, String> countryList = new LinkedHashMap<String, String>();

        try (QueryExecution qexec =
                     QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query)) {
            ResultSet results = qexec.execSelect() ;
            while (results.hasNext()){
                QuerySolution qs = results.next();
                Resource countryResource = qs.getResource("country");
                countryList.put( countryResource.getURI() , qs.getLiteral("countryLabel").getString());
            }
        }

        return countryList;
    }
}
