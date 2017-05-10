package model;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.resultset.ResultsFormat;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        Person p1 = new Person("David", Gender.MALE, null, "Austria", "Linz", "Reuchlinstraße", "4020", "JKU", "SomeOrg");
        Person p2 = new Person("Florian", Gender.MALE, new Date(), "Austria", "Linz", "Hauptstraße", "4020", "JKU", "SomeOrg");
        Person p3 = new Person("Christian", Gender.MALE, new Date(), "Austria", "Linz", "Hauptstraße", "4020", "JKU", "");
        Person p4 = new Person("Max", Gender.MALE, new Date(), "Austria", "Linz", "Musterstraße", "4020", "JKU", "");

        insertPerson(p1);
        //insertPerson(p2);
        //insertPerson(p3);
        //insertPerson(p4);

        Person up1 = new Person("David", Gender.MALE, null, "USA", "Linz", "Reuchlinstraße", "4020", "JKU", "");

        updatePerson(p1, up1);
        deletePerson(up1);
    }

    // todo: unternehmer-eigentümer: Person p, Org-name
    // todo: Date: null, alles andere leer-string
    public static void insertPerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();

            String nsPerson = "http://www.example/person";
            String nsOrg = "http://www.example/org";
            String nsRDF = RDF.getURI();
            String nsFoaf = FOAF.getURI();
            String nsVcard = VCARD.getURI();

            model.setNsPrefix("person", nsPerson);
            model.setNsPrefix("org", nsOrg);
            model.setNsPrefix("rdf", nsRDF);
            model.setNsPrefix("foaf", nsFoaf);
            model.setNsPrefix("vcard", nsVcard);

            Resource person = ResourceFactory.createResource(nsPerson + p.getName());
            model.add(person, RDF.type, FOAF.Person);
            model.add(person, FOAF.name, p.getName());
            model.add(person, FOAF.gender, p.getGender().toString());
            if (!(p.getBirthdate() == null))
                model.add(person, VCARD.BDAY, p.getBirthdate().toString());
            if (!p.getAddress().equals(""))
                model.add(person, VCARD.Street, p.getAddress());
            if (!p.getZip().equals(""))
                model.add(person, VCARD.Pcode, p.getZip());
            if (!p.getCity().equals(""))
                model.add(person, VCARD.Locality, p.getCity());
            if (!p.getCountry().equals(""))
                model.add(person, VCARD.Country, p.getCountry());

            if(!p.getEmployer().equals("")) {
                Resource employer = ResourceFactory.createResource(nsOrg + p.getEmployer());
                model.add(employer, VCARD.Orgname, p.getEmployer());
                Property employerProp = model.createProperty(nsFoaf + "employer");
                model.add(person, employerProp, employer);
            }
            if(!p.getOwnsOrg().equals("")) {
                Resource ownsOrg = ResourceFactory.createResource(nsOrg + p.getOwnsOrg());
                model.add(ownsOrg, VCARD.Orgname, p.getOwnsOrg());
                Property ownsOrgProp = model.createProperty(nsFoaf + "ownsOrg");
                model.add(person, ownsOrgProp, ownsOrg);
            }

            System.out.println(model.toString());

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
        }
    }

    //TODO: org-update/delete
    private static void updatePerson(Person oldP, Person newP) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);

        String deleteString = "";
        if(oldP.getBirthdate() != newP.getBirthdate())
            deleteString += "person:" + newP.getName() + " vcard:BDAY \"" + oldP.getBirthdate().toString() + "\". ";
        if(!oldP.getGender().toString().equals(newP.getGender().toString()))
            deleteString += "person:" + newP.getName() + " foaf:gender \"" + oldP.getGender().toString() + "\". ";
        if(!oldP.getCountry().equals(newP.getCountry()))
            deleteString += "person:" + newP.getName() + " vcard:Country \"" + oldP.getCountry() + "\". ";
        if(!oldP.getCity().equals(newP.getCity()))
            deleteString += "person:" + newP.getName() + " vcard:Locality \"" + oldP.getCity() + "\". ";
        if(!oldP.getZip().equals(newP.getZip()))
            deleteString += "person:" + newP.getName() + " vcard:Pcode \"" + oldP.getZip() + "\". ";
        if(!oldP.getAddress().equals(newP.getAddress()))
            deleteString += "person:" + newP.getName() + " vcard:Street \"" + oldP.getAddress() + "\". ";
        if(!oldP.getEmployer().equals(newP.getEmployer()))
            deleteString += "person:" + newP.getName() + " foaf:employer org:" + oldP.getEmployer() + ". ";
        if(!oldP.getOwnsOrg().equals(newP.getOwnsOrg()))
            deleteString += "person:" + newP.getName() + " foaf:ownsOrg org:" + oldP.getOwnsOrg() + ". ";

        try {
            String inputString =
                    "PREFIX org: <http://www.example/org>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX person: <http://www.example/person>\n" +
                            "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                            "DELETE DATA {" + deleteString + "}";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
        }

        insertPerson(newP);
    }

    private static void deletePerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);

        String deleteString = "person:" + p.getName() + " foaf:name \"" + p.getName() + "\". ";
        if(p.getBirthdate() != null)
            deleteString += "person:" + p.getName() + " vcard:BDAY \"" + p.getBirthdate().toString() + "\". ";
        if(!p.getGender().toString().equals(""))
            deleteString += "person:" + p.getName() + " foaf:gender \"" + p.getGender().toString() + "\". ";
        if(!p.getCountry().equals(""))
            deleteString += "person:" + p.getName() + " vcard:Country \"" + p.getCountry() + "\". ";
        if(!p.getCity().equals(""))
            deleteString += "person:" + p.getName() + " vcard:Locality \"" + p.getCity() + "\". ";
        if(!p.getZip().equals(""))
            deleteString += "person:" + p.getName() + " vcard:Pcode \"" + p.getZip() + "\". ";
        if(!p.getAddress().equals(""))
            deleteString += "person:" + p.getName() + " vcard:Street \"" + p.getAddress() + "\". ";
        if(!p.getEmployer().equals(""))
            deleteString += "person:" + p.getName() + " foaf:employer org:" + p.getEmployer() + ". ";
        if(!p.getOwnsOrg().equals(""))
            deleteString += "person:" + p.getName() + " foaf:ownsOrg org:" + p.getOwnsOrg() + ". ";

        try {
            String inputString =
                    "PREFIX org: <http://www.example/org>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX person: <http://www.example/person>\n" +
                            "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                            "DELETE DATA {" + deleteString + "}";

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

    public static  Person getPerson(String name) {

        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());
        Person personItem = new Person();

        try {
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "SELECT * WHERE {?a a :Person;" +
                                    "FILTER(?a = :" + name + ").}";
            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect();
            QuerySolution qs = rs.next();


            personItem = new Person();
            personItem.setName(qs.getResource("a").getLocalName());
            String gender = qs.getResource("a").getProperty(model.getProperty("http://example.org/gender")).getString();
            if(gender.compareToIgnoreCase("male") == 0){
                personItem.setGender(Gender.MALE);
            } else {
                personItem.setGender(Gender.FEMALE);
            }
            //personItem.setAge(qs.getResource("a").getProperty(model.getProperty("http://example.org/age")).getInt());
            personItem.setCity(qs.getResource("a").getProperty(model.getProperty("http://example.org/city")).getString());
            personItem.setAddress(qs.getResource("a").getProperty(model.getProperty("http://example.org/address")).getString());
            //personItem.setZip(qs.getResource("a").getProperty(model.getProperty("http://example.org/zip")).getInt());
            personItem.setCountry(qs.getResource("a").getProperty(model.getProperty("http://example.org/country")).getString());


            ResultSetFormatter.out(rs) ;

        } catch (Exception exc) {
            System.out.print(exc);
        } finally {
            dataset.end();
            dataset.close();
        }
        return  personItem;
    }

    public static List<Person> getPersons(){

        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());
        List<Person> listPersonen = new ArrayList<Person>();
        try{
            dataset.begin(ReadWrite.READ);

            Model model = dataset.getDefaultModel();

            String query = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org/> " +
                    "SELECT * WHERE {?a a :Person;" +
                    ":age ?b;" +
                    ":address ?c;" +
                    ":zip ?d;" +
                    ":city ?e;" +
                    ":address ?f;" +
                    ":employer ?g;" +
                    ":gender ?h. }";
            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect() ;
            //ResultSetFormatter.out(rs) ;

            Person pListItem;

            while (rs.hasNext()){
                QuerySolution qs = rs.next();
                System.out.print(qs.getResource("a").getLocalName() + " " + qs.getLiteral("b").getString());
                System.out.print(qs.getResource("a").getProperty(model.getProperty("http://example.org/age")).getString());


                //System.out.print(qs.getResource("a").listProperties());

                pListItem = new Person();
                pListItem.setName(qs.getResource("a").getLocalName());
                String gender = qs.getResource("a").getProperty(model.getProperty("http://example.org/gender")).getString();
                if(gender.compareToIgnoreCase("male") == 0){
                    pListItem.setGender(Gender.MALE);
                } else {
                    pListItem.setGender(Gender.FEMALE);
                }

                //pListItem.setAge(qs.getResource("a").getProperty(model.getProperty("http://example.org/age")).getInt());
                pListItem.setCity(qs.getResource("a").getProperty(model.getProperty("http://example.org/city")).getString());
                pListItem.setAddress(qs.getResource("a").getProperty(model.getProperty("http://example.org/address")).getString());
                //pListItem.setZip(qs.getResource("a").getProperty(model.getProperty("http://example.org/zip")).getInt());
                pListItem.setCountry(qs.getResource("a").getProperty(model.getProperty("http://example.org/country")).getString());

                listPersonen.add(pListItem);
            }




        } catch (Exception exc){
            System.out.print(exc);
        }

        finally {
            dataset.end();
            dataset.close();
        }

        return listPersonen;
    }
}