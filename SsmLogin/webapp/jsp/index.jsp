<%@ page language="java" contentType="text/html;charset=utf-8" import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html>
<html>
<head>
<title>中国创面治疗质量评估考核系统</title>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
</head>
<body>
<%=new Date()%>	
<p>${h1}</p>
<p>${h2}</p>
<p>${h3}</p>
</body>
</html>
