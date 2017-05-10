<%@ page import="model.MainTestRDF" %>
<%@ page import="model.Person" %>
<%@ page import="model.Gender" %><%--
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


        boolean viewPerson = request.getParameter("changePerson") != null;
        boolean addPerson = request.getParameter("addPerson") != null;
        boolean modifyPerson = request.getParameter("modifyPerson") != null;

        Person personItem = new Person();
        if (addPerson){
            personItem = generatePersonItemFromForm(request);
            MainTestRDF.insertPerson(personItem);
            personItem = new Person();
            out.println("Person: " + request.getParameter("name") + " wurde hinzugefügt!");
        }

        if(modifyPerson){

            personItem = generatePersonItemFromForm(request);
                //UpdatePerson-Method Request MainTestRDF
        }

        if (viewPerson){
            personItem = MainTestRDF.getPerson(request.getParameter("changePerson"));
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
            int zip = Integer.parseInt(request.getParameter("zipcode"));

            pers.setZip(zip);

            return pers;
        }
    %>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">name:</div>
                <div class="divTableCell"><input type="text" <% if(viewPerson){out.println("disabled");}  %> name="name" value="<% out.println(personItem.getName()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Gender:</div>
                <div class="divTableCell"><select name="gender">
                    <option value="m" <% if(personItem.getGender() == Gender.MALE){out.println("selected");}  %> >M</option>
                    <option value="w" <% if(personItem.getGender() == Gender.FEMALE){out.println("selected");}  %>>W</option>
                </select></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Date of Birth:</div>
                <div class="divTableCell"><input type="date" name="dateofbirth"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Street:</div>
                <div class="divTableCell"><input type="text" name="street" value="<% out.println(personItem.getAddress()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">ZIP Code:</div>
                <div class="divTableCell"><input type="text" name="zipcode" value="<% out.println(personItem.getZip()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">City:</div>
                <div class="divTableCell"><input type="text" name="city" value="<% out.println(personItem.getCity()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Country:</div>
                <div class="divTableCell"><input type="text" name="country" value="<% out.println(personItem.getCountry()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Employer:</div>
                <div class="divTableCell"><input type="text" name="employer" value="<% out.println(personItem.getEmployer()); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">&nbsp;</div>
                <div class="divTableCell">&nbsp;</div>
            </div>
        </div>
    </div>

    <%
        if (!viewPerson){
            out.println("<input type=\"submit\" name=\"addPerson\" value=\"Add\">");
        }
        else {
            out.println("<input type=\"button\" value =\"Modify\" onclick=\"location.href='add_modifyPerson.jsp?modifyPerson=" + personItem.getName() + "'\" >");
        }
    %>

    <button type="button" onclick="location.href='index.jsp'">Home</button>

</form>



</body>
</html>
