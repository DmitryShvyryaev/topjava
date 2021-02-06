<%--
  Created by IntelliJ IDEA.
  User: Inver
  Date: 05.02.2021
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>

<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<style>
    table {
        border: 2px solid black;
        border-collapse: collapse;
    }
    table th, table td {
        border: 1px solid black;
        padding: 4px 6px;
        min-width: 100px;
        font-size: 16px;
    }
    table th {
        background-color: darkseagreen;
    }
</style>
<table>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <tbody id="mainTable">

    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="color: ${meal.excess ? "red" : "green"}">
            <td>${f:formatDateTime(meal.dateTime)}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=getById&id=${meal.id}">Update</a></td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>

    </tbody>
</table>

</body>
</html>
