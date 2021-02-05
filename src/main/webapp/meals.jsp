<%--
  Created by IntelliJ IDEA.
  User: Inver
  Date: 05.02.2021
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>

<table border="2px" cellpadding="6px" style="border-collapse: collapse" class="table">
    <tr bgcolor="#5f9ea0">
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th></th>
        <th></th>
    </tr>
    <tbody id="mainTable">

    <jsp:useBean id="meals" scope="request" type="java.util.List"/>
    <jsp:useBean id="format" scope="request" type="java.util.List"/>
    <c:forEach var="meal" items="${meals}">
        <tr style="color: ${meal.excess ? "red" : "green"}">
            <td datatype="">${meal.dateTime}</td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td></td>
            <td></td>
        </tr>
    </c:forEach>

    </tbody>
</table>

</body>
</html>
