<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <style>
        div {
            display: inline-grid;
            font-size: 16px;
            font-style: normal;
            margin: 0 30px 0 0
        }

        label {
            margin-bottom: 5px;
        }

        button {
            margin: 7px 10px 0 0;
            font-size: 14px;
            padding: 2px 4px;
        }

        .filter {
            background: lightblue;
            padding: 10px;
            width: 700px;
            border: 1px solid black;
            border-radius: 7px;
        }
    </style>
    <form class="filter">
        <input type="hidden" name="action" value="filter">
        <div>
            <label for="startDate">from date (included)</label>
            <input name="startDate" id="startDate" type="date">
        </div>
        <div>
            <label for="endDate">to date (included)</label>
            <input name="endDate" id="endDate" type="date">
        </div>
        <div>
            <label for="startTime">from time (included)</label>
            <input name="startTime" id="startTime" type="time">
        </div>
        <div>
            <label for="endTime">to time (included)</label>
            <input name="endTime" id="endTime" type="time">
        </div>
        <br>
        <button type="submit">Filter</button>
        <form action="meals" method="get">
            <button type="submit">Cancel</button>
        </form>
    </form>
    <br>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</section>
</body>
</html>