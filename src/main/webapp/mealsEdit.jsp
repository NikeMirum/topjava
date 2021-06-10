<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Edit meal</h2>
<form method="POST" action="meals" name="AddOrEditMeal">
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <dl>
        DateTime: <input type="datetime-local" name="dateTime" value="<c:out value="${meal.dateTime}" />"/> <br/>
    </dl>
    <dl>
        Description: <input type="text" name="description" value="<c:out value="${meal.description}" />"/> <br/>
    </dl>
    <dl>
        Calories : <input type="number" name="calories" value="<c:out value="${meal.calories}" />"/> <br/>
    </dl>
    <input type="submit" value="Save"/>
    <input type="button" value="Cancel" onclick="window.history.back()"/>
</form>
</body>
</html>