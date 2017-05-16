<%@ page import="java.util.List" %>
<%@ page import="model.Person" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.MainTestRDF" %>
<%@ page import="sun.applet.Main" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="model.Gender" %>
<%@ page import="model.WikiRDFQuery" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Florian
  Date: 30/04/2017
  Time: 11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="main.css">
    <title>$Title$</title>
</head>
<body>

<%

    boolean filter = request.getParameter("filter") != null;
    boolean deletefilter = request.getParameter("filter") != null;
    boolean delete = request.getParameter("deletePerson") != null;
    List<Person> listPerson = new ArrayList<Person>();
    Gender gender = null;
    String street = "";
    String plz = "";
    String city= "";
    if (filter) {
        String g = request.getParameter("filterGender");

        if(g.compareToIgnoreCase("m") == 0){
            gender = Gender.MALE;
        } else if (g.compareToIgnoreCase("w") == 0) {
            gender = Gender.FEMALE;
        }
        street = request.getParameter("filterStreet");
        plz = request.getParameter("filterZipcode");
        city = request.getParameter("filterCity");

        listPerson = MainTestRDF.filterPersons(gender,street,plz,city);
    }
    else if (delete){
        System.out.print("delete");
        listPerson = MainTestRDF.getPersons();

        for (Person personItem:listPerson) {
            if (personItem.getName().compareTo(request.getParameter("deletePerson")) == 0){
                MainTestRDF.deletePerson(personItem);
            }
        }

        listPerson = MainTestRDF.getPersons();
    }
    else
    {
        listPerson = MainTestRDF.getPersons();
    }

%>

<form action="listPersons.jsp" method="post">
    <div class="divTable" style="width: 50%;">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Gender:</div>
                <div class="divTableCell"><select name="filterGender">
                    <option value="n">None</option>
                    <option value="m">M</option>
                    <option value="w">W</option>
                </select></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Street:</div>
                <div class="divTableCell"><input type="text" name="filterStreet"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">ZIP Code:</div>
                <div class="divTableCell"><input type="text" name="filterZipcode"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">City:</div>
                <div class="divTableCell"><input type="text" name="filterCity"></div>
            </div>
        </div>
    </div>
    <input type="submit" name="filter" value="Apply Filter">
    <input type="submit" name="deletefilter" value="Delete Filter">
    <div>Filter:
        <%
            if(gender != null){ out.print("Gender: " + gender.toString() + ", "); }
            if (street != "") { out.print("Street: " + street + ", ");}
            if (plz != "") {out.print("ZipCode: " + plz + ", ");}
            if (city != "") {out.print("City: " + city + ", ");}
        %>
    </div>
</form>


<div class="divTable">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">Name</div>
            <div class="divTableHead">Gender</div>
            <div class="divTableHead">Date of Birth</div>
            <div class="divTableHead">Address</div>
            <div class="divTableHead">MotherTongues(Possible)</div>
            <div class="divTableHead">Employer</div>
            <div class="divTableHead">OwnsOrg</div>
            <div class="divTableHead">Modify/Delete</div>
        </div>
    </div>

    <div class="divTableBody">
        <%
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            for (Person item:listPerson
                 ) {
                Map.Entry<String, List<String>> country = WikiRDFQuery.getCountry(item.getCountry());
                String languages = "";
                for(String langItem: country.getValue()){
                    languages = languages + langItem + ", ";
                }
                languages = languages.substring(0, languages.lastIndexOf(","));

                out.println("<div class=\"divTableRow\">" +
                            "<div class=\"divTableCell\">" + item.getName() + "</div>" +
                            "<div class=\"divTableCell\">" + item.getGender().toString() + "</div>" +
                            "<div class=\"divTableCell\">" + sdf.format(item.getBirthdate()) + "</div>" +
                        //ToDo: Countryname display not the link.
                            "<div class=\"divTableCell\">"+ item.getAddress() + ", " + item.getZip() + " " + item.getCity() + ", " +

                                country.getKey() +

                            "</div>" +
                            "<div class=\"divTableCell\">" +
                                languages +
                            "</div>" +
                            "<div class=\"divTableCell\">" + item.getEmployer() + "</div>" +
                            "<div class=\"divTableCell\">" + item.getOwnsOrg() + "</div>" +
                            "<div class=\"divTableCell\">" +
                                //"<button type=\"button\" name=\"viewPerson\" value=\"" + item.getName() + "\" onclick=\"location.href='add_modifyPerson.jsp?viewPerson=" + item.getName() + "'\">View</button>" +
                                "<button type=\"button\" name=\"changePerson\" value=\"" + item.getName() + "\" onclick=\"location.href='add_modifyPerson.jsp?changePerson=" + item.getName() + "'\">Modify</button>" +
                                "<button type=\"button\" name=\"deletePerson\" value=\"" + item.getName() + "\" onclick=\"location.href='listPersons.jsp?deletePerson=" + item.getName() + "'\">Delete</button>" +
                            "</div>" +
                        "</div>");
            }
        %>

    </div>
</div>

<button type="button" onclick="location.href='index.jsp'">Home</button>
</body>
</html>
