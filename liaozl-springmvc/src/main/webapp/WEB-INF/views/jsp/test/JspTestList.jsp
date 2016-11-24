<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>JspTestList</title>
</head>
<body>
${type}
<br/>
<c:forEach items="${userList}" var="user">
    <c:out value="${user}"></c:out>
    <br/>
</c:forEach>
</body>
</html>