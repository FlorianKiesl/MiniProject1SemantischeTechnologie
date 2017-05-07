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


        boolean modifyPerson = request.getParameter("changePerson") != null;
        boolean addPerson = request.getParameter("addPerson") != null;
        String idModifyPerson = "";

        if (addPerson){
            Person personItem = new Person();
            personItem.setName(request.getParameter("name"));
            String gender = request.getParameter("gender");
            if(gender == "m"){
                personItem.setGender(Gender.MALE);
            } else {
                personItem.setGender(Gender.FEMALE);
            }
            MainTestRDF.insertPerson(personItem);

            out.println("Person: " + request.getParameter("name") + " wurde hinzugefügt!");
        }

        if (modifyPerson){
            idModifyPerson = request.getParameter("changePerson");
        }

    %>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">name:</div>
                <div class="divTableCell"><input type="text" <% if(modifyPerson){out.println("disabled");}  %> name="name" value="<% out.println(idModifyPerson); %>"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Gender:</div>
                <div class="divTableCell"><select>
                    <option value="m" name="gender">M</option>
                    <option value="w" name="gender">W</option>
                </select></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Date of Birth:</div>
                <div class="divTableCell"><input type="date" name="dateofbirth"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Street:</div>
                <div class="divTableCell"><input type="text" name="street"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">ZIP Code:</div>
                <div class="divTableCell"><input type="text" name="zipcode"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">City:</div>
                <div class="divTableCell"><input type="text" name="zipcode"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Country:</div>
                <div class="divTableCell"><input type="text" name="zipcode"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Employer:</div>
                <div class="divTableCell"><input type="text" name="employer"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">&nbsp;</div>
                <div class="divTableCell">&nbsp;</div>
            </div>
        </div>
    </div>

    <%
        if (!modifyPerson){
            out.println("<input type=\"submit\" name=\"addPerson\" value=\"Add\">");
        }
        else {
            out.println("<input type=\"button\" value =\"Modify\" onclick=\"location.href='listPersons.jsp'\" >");
        }
    %>

    <button type="button" onclick="location.href='index.jsp'">Home</button>

</form>



</body>
</html>
