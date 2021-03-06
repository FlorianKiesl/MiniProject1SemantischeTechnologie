package model;

import org.apache.jena.datatypes.xsd.XSDDatatype;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.apache.jena.rdf.model.ResourceFactory.createTypedLiteral;

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
            dataset.removeNamedModel("DavidRiederich");
            //dataset.removeNamedModel("David");
            dataset.getDefaultModel().removeAll(); // delete everything from default model
            dataset.commit();
        } finally {
            dataset.end();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date newBirthdate = null;
        try {
            newBirthdate = sdf.parse("2014-11-12");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Person p1 = new Person("David Riederich", Gender.MALE, null, "Austria", "Linz", "Reuchlinstraße", "4020", "JKU", "SomeOrg");
        Person p2 = new Person("Florian Kiesl", Gender.MALE, newBirthdate , "Austra", "Linz", "Hauptstraße", "4030", "JKU", "SomeOrg");
        Person p3 = new Person("Christian Kern", Gender.MALE, newBirthdate, "Austria", "Wien", "Hauptstraße", "1010", "JKU", "");
        Person p4 = new Person("Max Mustermann", Gender.MALE, null, "Austria", "Wels", "Musterstraße", "4600", "JKU", "");

        insertPerson(p1);
        insertPerson(p2);
        insertPerson(p3);
        insertPerson(p4);

        List<Person> persons = filterPersons(Gender.MALE, "straße", "", "W");

        //getCompanies();

        //Person up1 = new Person("David Riederich", Gender.MALE, null, "Austria", "Linz", "Reuchlinstraße11", "4020", "JKU", "SomeOrg");

        //updatePerson(p1, up1);
        //deletePerson(up1);
       // deletePerson(p2);

        //getPersons();
        WikiRDFQuery.getCountries();
    }

    public static void insertPerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();

            String nsPerson = "http://www.example/person#";
            String nsOrg = "http://www.example/org#";
            String nsRDF = RDF.getURI();
            String nsFoaf = FOAF.getURI();
            String nsVcard = VCARD.getURI();

            model.setNsPrefix("person", nsPerson);
            model.setNsPrefix("org", nsOrg);
            model.setNsPrefix("rdf", nsRDF);
            model.setNsPrefix("foaf", nsFoaf);
            model.setNsPrefix("vcard", nsVcard);

            Resource person = ResourceFactory.createResource(nsPerson + p.getName().replaceAll("\\s+",""));
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

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
        }
    }

    //TODO: org-update/delete
    public static void updatePerson(Person oldP, Person newP) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);

        String personName = newP.getName().replaceAll("\\s+","");

        String deleteString = "";
        if(oldP.getBirthdate() != null)
            deleteString += "person:" + personName + " vcard:BDAY \"" + oldP.getBirthdate().toString() + "\". ";
        if(!oldP.getGender().toString().equals(newP.getGender().toString()))
            deleteString += "person:" + personName + " foaf:gender \"" + oldP.getGender().toString() + "\". ";
        if(!oldP.getCountry().equals(newP.getCountry()))
            deleteString += "person:" + personName + " vcard:Country \"" + oldP.getCountry() + "\". ";
        if(!oldP.getCity().equals(newP.getCity()))
            deleteString += "person:" + personName + " vcard:Locality \"" + oldP.getCity() + "\". ";
        if(!oldP.getZip().equals(newP.getZip()))
            deleteString += "person:" + personName + " vcard:Pcode \"" + oldP.getZip() + "\". ";
        if(!oldP.getAddress().equals(newP.getAddress()))
            deleteString += "person:" + personName + " vcard:Street \"" + oldP.getAddress() + "\". ";
        if(!oldP.getEmployer().equals(newP.getEmployer()))
            deleteString += "person:" + personName + " foaf:employer org:" + oldP.getEmployer() + ". ";
        if(!oldP.getOwnsOrg().equals(newP.getOwnsOrg()))
            deleteString += "person:" + personName + " foaf:ownsOrg org:" + oldP.getOwnsOrg() + ". ";

        try {
            String inputString =
                    "PREFIX org: <http://www.example/org>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX person: <http://www.example/person#>\n" +
                            "PREFIX org: <http://www.example/org#>\n" +
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

    public static void deletePerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);

        try {
            String inputString =
                    "PREFIX org: <http://www.example/org>\n" +
                            "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                            "PREFIX person: <http://www.example/person#>\n" +
                            "PREFIX org: <http://www.example/org#>\n" +
                            "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                            "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                            "DELETE\n" +
                            "{ ?person ?p ?v }\n" +
                            "WHERE\n" +
                            "{ ?person foaf:name ?name .\n" +
                            "  FILTER ( ?name = \"" + p.getName() + "\" )\n" +
                            "  ?person ?p ?v\n" +
                            "}";

            UpdateRequest update = UpdateFactory.create(inputString);
            UpdateAction.execute(update, dataset);

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
            archivePerson(p);
        }
    }

    private static void archivePerson(Person p) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getNamedModel(p.getName().replaceAll("\\s+",""));

            String nsPerson = "http://www.example/person#";
            String nsOrg = "http://www.example/org#";
            String nsRDF = RDF.getURI();
            String nsFoaf = FOAF.getURI();
            String nsVcard = VCARD.getURI();

            model.setNsPrefix("person", nsPerson);
            model.setNsPrefix("org", nsOrg);
            model.setNsPrefix("rdf", nsRDF);
            model.setNsPrefix("foaf", nsFoaf);
            model.setNsPrefix("vcard", nsVcard);

            Resource person = ResourceFactory.createResource(nsPerson + p.getName().replaceAll("\\s+",""));
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

            RDFDataMgr.write(System.out, dataset, Lang.TRIG);

            dataset.commit();
        } finally {
            dataset.end();
            dataset.close();
        }
    }

    public static List<Person> filterPersons(Gender gender, String street, String zip, String city) {
        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());
        String nsPerson = "http://www.example/person";
        String nsOrg = "http://www.example/org";
        String nsRDF = RDF.getURI();
        String nsFoaf = FOAF.getURI();
        String nsVcard = VCARD.getURI();
        List<Person> listPersonen = new ArrayList<Person>();

        String filter = "";

        if (gender != null)
            filter += "?a  foaf:gender  ?gender\n FILTER ( ?gender = \"" + gender.toString() + "\" ) ";
        if (!street.isEmpty())
            filter += "?a  vcard:Street  ?street\n FILTER ( regex(lcase(str(?street)) , lcase(\"" + street + "\"))) ";
        if (!zip.isEmpty())
            filter += "?a  vcard:Pcode ?zip\n FILTER ( regex(lcase(str(?zip)) , lcase(\"" + zip + "\"))) ";
        if (!city.isEmpty())
            filter += "?a  vcard:Locality ?city\n FILTER ( regex(lcase(str(?city)) , lcase(\"" + city + "\"))) ";

        try {
            dataset.begin(ReadWrite.READ);

            Model model = dataset.getDefaultModel();

            String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX vcard: <http://www.w3.org/2001/vcard-rdf/3.0#>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "SELECT ?a " +
                    "WHERE {" +
                    "?a  rdf:type  foaf:Person. " +
                    filter +
                    "}";


//            String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
//                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
//                    "SELECT ?a WHERE {?a rdf:type foaf:Person; " +
//                    "foaf:name ?name."  +
//                    "FILTER (regex(lcase(str(?name)) , lcase(\"" + p.getName() + "\")))." +
//                    "OPTIONAL{" +
//                        "?a foaf:Street ?street." +
//                        "FILTER (regex(lcase(str(?street)) , lcase(\"" + p.getAddress() + "\")))." +
//                        "}" +
//                    "}";
            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect();
            //ResultSetFormatter.out(rs);

            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                listPersonen.add(getPersonItem(model, qs));
            }


        } catch (Exception exc) {
            System.out.println(exc.getMessage());
        } finally {
            dataset.end();
            dataset.close();
        }

        return listPersonen;
    }

    public static  Person getPerson(String name) {

        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());

        Person personItem = new Person();

        try {
            dataset.begin(ReadWrite.READ);
            Model model = dataset.getDefaultModel();
            String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "PREFIX : <http://example.org#/> " +
                    "SELECT * WHERE {?a rdf:type foaf:Person;" +
                                    "foaf:name ?n; " +
                                    "FILTER(?n = \"" + name + "\").}";
            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect();
            //ResultSetFormatter.out(rs) ;
            QuerySolution qs = rs.next();
            if (qs != null){
                personItem = getPersonItem(model, qs);
            }


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

            String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "SELECT * WHERE {?a rdf:type foaf:Person}" ;

            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect() ;
            //ResultSetFormatter.out(rs) ;

            Person pListItem;

            while (rs.hasNext()){
                QuerySolution qs = rs.next();
                listPersonen.add(getPersonItem(model, qs));
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

    private static Person getPersonItem(Model model, QuerySolution qs) throws ParseException {
        String nsPerson = "http://www.example/person#";
        String nsOrg = "http://www.example/org#";
        String nsRDF = RDF.getURI();
        String nsFoaf = FOAF.getURI();
        String nsVcard = VCARD.getURI();


        Resource personResource = qs.getResource("a");

        Person pListItem = new Person();
        try {

            Statement stmt = personResource.getProperty(model.getProperty(nsFoaf + "name"));
            if (stmt != null) {
                pListItem.setName(stmt.getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsFoaf + "gender"));
            if (stmt != null) {
                String gender = stmt.getString();
                if (gender.compareToIgnoreCase("male") == 0) {
                    pListItem.setGender(Gender.MALE);
                } else {
                    pListItem.setGender(Gender.FEMALE);
                }
            }

            stmt = personResource.getProperty(model.getProperty(nsVcard + "BDAY"));
            if (stmt != null) {
                DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                Date birthday = df.parse(stmt.getString());
                pListItem.setBirthdate(birthday);
            }

            stmt = personResource.getProperty(model.getProperty(nsVcard + "Country"));
            if (stmt != null) {
                pListItem.setCountry(stmt.getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsVcard + "Locality"));
            if (stmt != null) {
                pListItem.setCity(stmt.getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsVcard + "Street"));
            if (stmt != null) {
                pListItem.setAddress(stmt.getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsVcard + "Pcode"));
            if (stmt != null) {
                pListItem.setZip(stmt.getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsFoaf + "employer"));
            if (stmt != null) {
                pListItem.setEmployer(stmt.getResource().getProperty(model.getProperty(nsVcard + "Orgname")).getString());
            }

            stmt = personResource.getProperty(model.getProperty(nsFoaf + "ownsOrg"));
            if (stmt != null) {
                pListItem.setOwnsOrg(stmt.getResource().getProperty(model.getProperty(nsVcard + "Orgname")).getString());
            }
        } catch (Exception exc){
            throw exc;
        }

        return pListItem;
    }

    public static List<String> getCompanies(){

        dataset = TDBFactory.assembleDataset(
                MainTestRDF.class.getResource("tdb-assembler.ttl").getPath());
        String nsPerson = "http://www.example/person";
        String nsOrg = "http://www.example/org";
        String nsRDF = RDF.getURI();
        String nsFoaf = FOAF.getURI();
        String nsVcard = VCARD.getURI();

        List<String> list = new ArrayList<String>();
        try {
            dataset.begin(ReadWrite.READ);

            Model model = dataset.getDefaultModel();

            String query = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n" +
                    "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                    "SELECT DISTINCT ?e WHERE {?a rdf:type foaf:Person; " +
                        "foaf:employer ?e.}";

            QueryExecution qExec = QueryExecutionFactory.create(query, dataset);
            ResultSet rs = qExec.execSelect() ;
            //ResultSetFormatter.out(rs) ;
            while (rs.hasNext()) {
                QuerySolution qs = rs.next();
                Resource employer = qs.getResource("e");
                list.add(employer.getProperty(model.getProperty(nsVcard + "Orgname")).getString());
            }


        }
        catch (Exception exc){
            System.out.print(exc);
        }

        finally {
            dataset.end();
            dataset.close();
        }

        return list;
    }
}