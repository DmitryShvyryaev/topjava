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
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
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
    <jsp:useBean id="filter" type="java.util.Map" scope="request"/>
    <form class="filter" action="<c:url value="/meals"/>">
        <input type="hidden" name="action" value="filter">
        <div>
            <label for="startDate">from date (included)</label>
            <input name="startDate" id="startDate" type="date" value=${filter.get("startDate")}>
        </div>
        <div>
            <label for="endDate">to date (included)</label>
            <input name="endDate" id="endDate" type="date" value=${filter.get("endDate")}>
        </div>
        <div>
            <label for="startTime">from time (included)</label>
            <input name="startTime" id="startTime" type="time" value=${filter.get("startTime")}>
        </div>
        <div>
            <label for="endTime">to time (included)</label>
            <input name="endTime" id="endTime" type="time" value=${filter.get("endTime")}>
        </div>
        <br>
        <button type="submit">Filter</button>
        <button id="cancel">Cancel</button>
    </form>

    <script>
        $('#cancel').on('click', function() {
            $("form").find('input[type=date], input[type=time]').val('');
        });
    </script>

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