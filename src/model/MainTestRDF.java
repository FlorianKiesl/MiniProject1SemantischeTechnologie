package model;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Florian on 25/04/2017.
 */
public class MainTestRDF {
    private static Dataset dataset;

    public static void main(String[] args) {
        Person p1 = new Person("David", Gender.MALE, 22, "Austria", "Linz", "Reuchlinstraße", 4020, "JKU");
        Person p2 = new Person("Florian", Gender.MALE, 25, "Austria", "Linz", "Hauptstraße", 4020, "JKU");

        insertPerson(p1);
        insertPerson(p2);

        /*updatePerson("Max", "age", "35", "34");
        updatePerson("Max", "address", "Hauptstrasse", "Musterstrasse");*/
    }

    // todo: addresse in land, ort und plz
    // todo: employer
    public static void insertPerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE); // START TRANSACTION
        try {
            dataset.getDefaultModel().removeAll(); // delete everything from default model
            dataset.commit();
        } finally {
            dataset.end();
        }

        dataset.begin(ReadWrite.WRITE);
        try {
            String inputString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "INSERT DATA { :" + p.getEmployer() + " a :Employer . };" +
                    "INSERT DATA {" +
                        ":" + p.getName() + "  a :Person; " +
                        ":gender \"" + p.getGender().toString() + "\"@en; :age " + p.getAge() + ";" +
                        ":country \"" + p.getCountry() + "\";" +
                        ":city \"" + p.getCity() + "\";" +
                        ":zip \"" + p.getZip() + "\";" +
                        ":address \"" + p.getAddress() + "\";" +
                        ":employer :" + p.getEmployer() + "." +
                    "} ";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
        }
    }

    private static void updatePerson(String name, String att, String newValue, String oldValue) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

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
            dataset.close();
        }
    }

    private static void deletePerson(String name) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

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
            dataset.close();
        }
    }

    public static void filterPersons(Person p){



    }

    public static List<Person> getPersons(){


        return new ArrayList<Person>();
    }
}