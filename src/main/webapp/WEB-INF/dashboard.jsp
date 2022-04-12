<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Dashboard</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
</head>
<body>

	<div class="container">
		<nav class="d-flex justify-content-between align-items-center">
			<h1>Bienvenid@ ${user_session.first_name}</h1>
			<a href="/logout" class="btn btn-danger">Cerrar Sesión</a>
		</nav>
	</div>
	<div class="row">
		<h2>Eventos en tu estado</h2>
	</div>
	<div class="row">
		<h2>Eventos en otros estados</h2>
	</div>
	<div class="row">
		<h2>Crear evento</h2>
		<form:form action="/events/create" method="post" modelAttribute="event">
			<form:hidden path="planner" value="${user_session.id}" />
			<div class="form-group">
				<form:label path="name">Nombre:</form:label>
				<form:input path="name" class="form-control"/>
				<form:errors path="name" class="text-danger"/>
			</div>
			<div class="form-group">
				<form:label path="eventDate">Fecha:</form:label>
				<form:input path="eventDate" type="date" class="form-control"/>
				<form:errors path="eventDate" class="text-danger"/>
			</div>
			<div class="form-group">
				<form:label path="location">Locación:</form:label>
				<form:input path="location" class="form-control"/>
				<form:errors path="location" class="text-danger"/>
			</div>
			<div class="form-group">
				<form:label path="state">Estado:</form:label>
				<form:select path="state" class="form-control">
					<c:forEach var="state" items="${states}">
						<option value="${state}">${state}</option>
					</c:forEach>
				</form:select>
				<form:errors path="state" class="text-danger" />
			</div>
			<input type="submit" value="Crear Evento" class="btn btn-success">
		</form:form>
	</div>
</body>
</html>