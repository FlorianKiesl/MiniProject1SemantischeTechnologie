<%@ page import="java.util.List" %>
<%@ page import="model.Person" %>
<%@ page import="java.util.ArrayList" %><%--
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
    List<Person> listPerson = new ArrayList<Person>();
    listPerson.add(new Person("Florian"));
    listPerson.add(new Person("David"));

    if(request.getParameter("deletePerson") != null){

        listPerson.remove(1);
    }

    if (request.getParameter("filter") != null){

    }
%>

<div class="divTable" style="width: 50%;">
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCellFirstColumn">Gender:</div>
            <div class="divTableCell"><select>
                <option value="m">M</option>
                <option value="w">W</option>
            </select></div>
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
    </div>
</div>
<input type="submit" name="filter" value="Apply Filter">
</form>

<br>
<br>
<p>Filter:</p>
<div class="divTable">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">Name</div>
            <div class="divTableHead">Gender</div>
            <div class="divTableHead">Date of Birth</div>
            <div class="divTableHead">Address</div>
            <div class="divTableHead">Employer</div>
            <div class="divTableHead">Modify/Delete</div>
        </div>
    </div>

    <div class="divTableBody">
        <%
            for (Person item:listPerson
                 ) {
                out.println("<div class=\"divTableRow\">" +
                            "<div class=\"divTableCell\">" + item.getName() + "</div>" +
                            "<div class=\"divTableCell\">&nbsp;</div>" +
                            "<div class=\"divTableCell\">&nbsp;</div>" +
                            "<div class=\"divTableCell\">&nbsp;</div>" +
                            "<div class=\"divTableCell\">&nbsp;</div>" +
                            "<div class=\"divTableCell\">" +
                                "<button type=\"button\" name=\"viewPerson\" value=\"" + item.getName() + "\" onclick=\"location.href='add_modifyPerson.jsp?viewPerson=" + item.getName() + "'\">View</button>" +
                                "<button type=\"button\" name=\"changePerson\" value=\"" + item.getName() + "\" onclick=\"location.href='add_modifyPerson.jsp?changePerson=" + item.getName() + "'\">Modify</button>" +
                                "<button type=\"button\" name=\"deletePerson\" value=\"" + item.getName() + "\" onclick=\"location.href='listPersons.jsp?deletePerson=" + item.getName() + "'\">Delete</button>" +
                            "</div>" +
                        "</div>");
            }
        %>

    </div>
</div>

<button type="button" onclick="location.href='index.jsp'">Back</button>
</body>
</html>