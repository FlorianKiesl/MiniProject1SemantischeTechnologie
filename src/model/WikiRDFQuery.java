package model;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Resource;

import javax.xml.crypto.dsig.keyinfo.KeyValue;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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

    public static Map.Entry<String, List<String>> getCountry(String uri) {
        File queryFile = new File(WikiRDFQuery.class.getResource("wikidataqueryCountry.rq").getPath());
        String replacedQuery = "";
        String countryLabel = "";
        List<String> languageLabelList = new ArrayList<String>();
        try {
            replacedQuery = new String(Files.readAllBytes(queryFile.toPath()));
            String countryCode = uri.substring(uri.lastIndexOf("/") + 1);
            replacedQuery = replacedQuery.replace("{1}", "wd:" + countryCode);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Query query = QueryFactory.create(replacedQuery);
            try (QueryExecution qexec =
                         QueryExecutionFactory.sparqlService("https://query.wikidata.org/sparql", query)) {
                ResultSet results = qexec.execSelect() ;
                while (results.hasNext()){
                    QuerySolution qs = results.next();
                    Resource countryResource = qs.getResource("country");
                    countryLabel = qs.getLiteral("countryLabel").getString();
                    languageLabelList.add(qs.getLiteral("languageLabel").getString());
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        } catch (Exception exc){
            System.out.print(exc.getMessage());
        }

        return new AbstractMap.SimpleEntry<String, List<String>>(countryLabel, languageLabelList);
    }
}
