<%--
  Created by IntelliJ IDEA.
  User: Inver
  Date: 05.02.2021
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<jsp:useBean id="meal" scope="request" type="ru.javawebinar.topjava.model.Meal"/>

<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${meal.id == 0 ? "Add meal" : "Edit meal"}</h2>
<style>
    .button {
        background: #eee;
        color: #000;
        text-decoration: none;
        border: 1px solid #777;
        border-radius: 3px;
        padding: 3px 6px;
        line-height: 16px;
        font-size: 14px;
        font-weight: 400;
        font-family: Arial;
        appearance: push-button;
    }

    form input, form select {
        margin: 3px 0 14px 0;
        padding: 2px 6px;
        font-size: 14px;
    }
</style>

<form method="POST" action="<c:url value="/meals"/>">
    <div>DateTime</div>
    <label>
        <input style="min-width: 300px" required type="datetime-local" placeholder="Введите дату:"
               name="date" value="${meal.dateTime}"/>
    </label>
    <br>

    <div>Description</div>
    <label>
        <input style="min-width: 300px" required type="text" placeholder="Введите описание:" minlength="1"
               maxlength="50"
               name="description" value="${meal.description}"/>
    </label>
    <br>

    <input type="hidden" value="${meal.id}" name="id"/>

    <div>Calories</div>
    <label>
        <input style="min-width: 300px" required type="number" name="calories" min="1" max="10000"
               placeholder="Введите количество калорий:" value="${meal.calories}"/>
    </label>
    <br>

    <input type="submit" value="Save" class="button">
    <a href="<c:url value="/meals"/>" class="button">Cancel</a>
</form>

</body>
</html>
