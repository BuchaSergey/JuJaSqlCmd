<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>SQLCmd</title>
</head>
<body>
<a href="/help">Help</a><br>
<a href="/connect">Connect</a><br>
<a href="/clear">Clear</a><br>
<a href="/create">Create</a><br>
<a href="/exit">Exit</a><br>


    <!--c:forEach items="${menuItem}" var="item">
        <a href="/sqlcmd/projects?category=${item.id}"><c:out value="${item.name}"/></a>
    <!--/c:forEach -->
</body>
</html>
