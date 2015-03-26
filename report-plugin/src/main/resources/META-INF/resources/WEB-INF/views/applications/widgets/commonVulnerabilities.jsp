<%@ page language="java" errorPage="/error.jsp" pageEncoding="UTF-8" contentType="text/html;charset=utf-8" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/page" prefix="page"%>
<%@ taglib uri="/WEB-INF/jscachebust.tld" prefix="cbs"%>

<c:set var="datePattern"><fmt:message key="date.format"/></c:set>
<head>
    <style>
        .report-text {
            text-align: center;
            margin-top: 115px;
            font-size: 17px;
            font-weight:bold;
            line-height:20px;
        }
    </style>
</head>

<div class="span6 ng-scope" ng-controller="CommonVulnerabilitiesController">

    <h4 class="ng-binding">{{ heading }}</h4>
    <div class="commVulns">

    </div>

</div>
