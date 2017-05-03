package model;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

/**
 * Created by Florian on 25/04/2017.
 */
public class MainTestRDF {
    private static Dataset dataset;

    public static void main(String[] args) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE); // START TRANSACTION
        try {
            dataset.getDefaultModel().removeAll(); // delete everything from default model
            dataset.commit();
        } finally {
            dataset.end();
        }

        insertPerson("David", "male", 23, "Reuchlinstrasse");
        insertPerson("Max", "male", 34, "Musterstrasse");
        insertPerson("Lisa", "female", 21, "Luststrasse");

        updatePerson("Max", "age", "35", "34");
        updatePerson("Max", "address", "Hauptstrasse", "Musterstrasse");

        dataset.close(); //CLOSE DB-CONNECTION
    }

    // todo: addresse in land, ort und plz
    // todo: employer
    private static void insertPerson(String name, String gender, int age, String address) {
        dataset.begin(ReadWrite.WRITE);
        try {
            String inputString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "INSERT DATA {" +
                    ":" + name + "  a :Person; " +
                    ":gender \"" + gender + "\"@en; :age " + age + "; " +
                    ":address \"" + address + "\".} ";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            dataset.commit();
        } finally {
            dataset.end();
        }
    }

    private static void updatePerson(String name, String att, String newValue, String oldValue) {
        dataset.begin(ReadWrite.WRITE);
        try {
            String inputString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "DELETE DATA {" +
                    ":" + name + " :" + att + " " + (att.equals("age") ? Integer.parseInt(oldValue) : ("\"" + oldValue + "\"")) + ".}; " +
                    "INSERT DATA {" +
                    ":" + name + " :" + att + " " + (att.equals("age") ? Integer.parseInt(newValue) : ("\"" + newValue + "\"")) + ".} ";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
        }
    }

    private static void deletePerson(String name) {
        dataset.begin(ReadWrite.WRITE);
        try {
            String inputString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "DELETE { ?person ?property ?value } \n" +
                    "WHERE \n" +
                    " { ?person ?property ?value ; <http://example.org> \"" + name + "\" } ";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
        }
    }
}