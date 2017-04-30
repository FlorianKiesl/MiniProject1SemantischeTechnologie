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

%>

<p>Filter:</p>
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

<br>

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
                            "<div class=\"divTableCell\">  \n" +
                            "   <button type=\"submit\" value=\"modify\">Modify</button>\n" +
                            "   <button type=\"submit\" value=\"delete\">Delete</button>\n" +
                            "</div>" +
                        "</div>");
            }
        %>

        <div class="divTableRow">
            <div class="divTableCell">&nbsp;</div>
            <div class="divTableCell">&nbsp;</div>
            <div class="divTableCell">&nbsp;</div>
            <div class="divTableCell">&nbsp;</div>
            <div class="divTableCell">&nbsp;</div>
            <div class="divTableCell">
                <button type="submit" value="modify">Modify</button>
                <button type="submit" value="delete">Delete</button>
            </div>
        </div>
    </div>
</div>

<button type="button" onclick="location.href='index.jsp'">Back</button>
</body>
</html>
