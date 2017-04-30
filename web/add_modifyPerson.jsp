<%--
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

        out.println("test: " + request.getParameter("name") + request.getParameter("changePerson"));
        boolean modifyPerson = request.getParameter("changePerson") != null;
        out.println(modifyPerson);
    %>

    <div class="divTable">
        <div class="divTableBody">
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">name:</div>
                <div class="divTableCell"><input type="text" name="name"></div>
            </div>
            <div class="divTableRow">
                <div class="divTableCellFirstColumn">Gender:</div>
                <div class="divTableCell"><select>
                    <option value="m">M</option>
                    <option value="w">W</option>
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



</form>

<button type="button" onclick="location.href='index.jsp'">Back</button>
</body>
</html>
