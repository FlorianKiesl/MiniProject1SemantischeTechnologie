<%@ page import="model.MainTestRDF" %>
<%@ page import="model.Person" %>
<%@ page import="model.Gender" %>
<%@ page import="model.WikiRDFQuery" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Florian
  Date: 30/04/2017
  Time: 11:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<head>
    <link rel="stylesheet" href="main.css">
    <title>Title</title>
</head>
<body>

<form action="add_modifyPerson.jsp" method="post">
    <%

        Map<String,String> countryList = WikiRDFQuery.getCountries();
        List<String> companyList = MainTestRDF.getCompanies();

        boolean viewPerson = request.getParameter("changePerson") != null;
        boolean addPerson = request.getParameter("addPerson") != null;
        boolean modifyPerson = request.getParameter("modifyPerson") != null;

        Person personItem = new Person();
        Person personBeforeModifying = new Person();
        Person personAfterModifying = new Person();
        if (addPerson){
            personItem = generatePersonItemFromForm(request);
            MainTestRDF.insertPerson(personItem);
            personItem = new Person();
            out.println("Person: " + request.getParameter("name") + " wurde hinzugefügt!");
        }

        if(modifyPerson){
            personBeforeModifying = MainTestRDF.getPerson(request.getParameter("name"));
            personAfterModifying = generatePersonItemFromForm(request);
            MainTestRDF.updatePerson(personBeforeModifying, personAfterModifying);

        }

        if (viewPerson){
            personBeforeModifying = MainTestRDF.getPerson(request.getParameter("changePerson"));
            out.print(personItem.getName());
        }



    %>

    <%!
        private Person generatePersonItemFromForm(HttpServletRequest request){
            Person pers = new Person();
            pers.setName(request.getParameter("name"));
            String gender = request.getParameter("gender");
            if(gender.compareToIgnoreCase("m") == 0){
                pers.setGender(Gender.MALE);
            } else {
                pers.setGender(Gender.FEMALE);
            }
            pers.setAddress(request.getParameter("street"));
            pers.setCity(request.getParameter("city"));
            pers.setCountry(request.getParameter("country"));
            pers.setEmployer(request.getParameter("employer"));
            pers.setZip(request.getParameter("zipcode"));
            pers.setOwnsOrg(request.getParameter("OwnsCompany"));
            String birthString = request.getParameter("dateofbirth").toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try{
                pers.setBirthdate(sdf.parse(request.getParameter("dateofbirth").toString()));
            } catch (ParseException exc) {
                System.out.println(exc.getMessage());
            }

            return pers;
        }
    %>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">name:</div>
                <div class="divTableCell">
                    <input type="text" <% if(viewPerson){out.print("disabled");}  %> name="name" value="<% out.print(personBeforeModifying.getName()); %>">
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Gender:</div>
                <div class="divTableCell"><select name="gender">
                    <option value="m" <% if(personBeforeModifying.getGender() == Gender.MALE){out.print("selected");}  %> >M</option>
                    <option value="w" <% if(personBeforeModifying.getGender() == Gender.FEMALE){out.print("selected");}  %>>W</option>
                </select></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Date of Birth:</div>
                <div class="divTableCell"><input type="date" name="dateofbirth" value="<% SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); out.print(sdf.format(personBeforeModifying.getBirthdate())); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Street:</div>
                <div class="divTableCell"><input type="text" name="street" value="<% out.print(personBeforeModifying.getAddress()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">ZIP Code:</div>
                <div class="divTableCell"><input type="text" name="zipcode" value="<% out.print(personBeforeModifying.getZip()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">City:</div>
                <div class="divTableCell">
                    <input type="text" name="city" value="<% out.print(personBeforeModifying.getCity()); %>">
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Country:</div>
                <div class="divTableCell">
                    <select name="country">
                    <%
                        for (Map.Entry<String, String> country : countryList.entrySet()){
                            if(personBeforeModifying.getCountry().compareTo(country.getKey()) == 0){
                                out.println("<option value=\"" + country.getKey()  + "\" selected>" + country.getValue() + "</option>");
                            } else {
                                out.println("<option value=\"" + country.getKey()  + "\">" + country.getValue() + "</option>");
                            }

                        }
                    %>

                    </select>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Employer:</div>
                <div class="divTableCell">
                    <input type="text" list="existingCompanies" name="employer" value="<% out.print(personBeforeModifying.getEmployer()); %>">
                    <datalist id="existingCompanies">
                        <%
                            for (String company: companyList){
                                out.print("<option value=\"" + company  + "\">");
                            }
                        %>
                    </datalist>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">OwnsCompany:</div>
                <div class="divTableCell">
                    <input type="text" list="ownsCompanies" name="OwnsCompany" value="<% out.print(personBeforeModifying.getOwnsOrg()); %>">
                    <datalist id="ownsCompanies">
                        <%
                            for (String company: companyList){
                                out.print("<option value=\"" + company  + "\">");
                            }
                        %>
                    </datalist>
                </div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">&nbsp;</div>
                <div class="divTableCell">&nbsp;</div>
            </div>
        </div>
    </div>

    <%
        if (!viewPerson){
            out.print("<input type=\"submit\" name=\"addPerson\" value=\"Add\">");
        }
        else {
            out.print("<input type=\"submit\" name=\"modifyPerson\" value =\"Modify\" >");
        }
    %>

    <button type="button" onclick="location.href='index.jsp'">Home</button>

</form>



</body>
</html>
