<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 6/4/2020
  Time: 10:20 PM
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<center>
    <h1>User Management</h1>
    <h2>
        <a href="./users">List All User</a>
    </h2>
</center>
<div align="center">
    <form method="post">
        <table border="1" cellpadding="5">
            <caption>
                <h2>Edit User</h2>
            </caption>
            <c:if test="${user!=null}">
                <input type="hidden" name="id" value="<c:out value='${user.getId()}'/>" />
            </c:if>
            <tr>
                <th>User Name :</th>
                <td><input type="text" name="name" size="45" value="<c:out value='${user.getName()}'/>"></td>
            </tr>
            <tr>
                <th>Email User:</th>
                <td>
                    <input type="email" name="email" size="45" value="<c:out value='${user.getEmail()}'/>">
                </td>
            </tr>
            <tr>
                <th>Country</th>
                <td>
                    <input type="text" name="country" value="<c:out value='${user.getCountry()}'/>"/>
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" value="Save">
                </td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
