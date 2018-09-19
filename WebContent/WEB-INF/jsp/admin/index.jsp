<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Administrator Dashboard</title>
</head>
<body>
<p><a href="print/index.jspx">Printers</a></p>
<p><a href="import.jspx">Import</a></p>
<p><a href="xls-update.jspx">Update lot data using XLS file</a></p>
<p><a href="xls-import.jspx">Update or import data using XLS file</a></p>
<p><a href="<s:url namespace="/admin" action="xlslot-import" />">Import Missing Items</a></p>

<h2>Session</h2>
<p>Session timeout: <b>${pageContext.session.maxInactiveInterval} seconds</b></p>
<h2>Server locale and time zone settings</h2>
<p>Locale: <b><s:property value="@java.util.Locale@getDefault()" /></b></p>
<p>Timezone: <b><s:property value="@java.util.TimeZone@getDefault()" /></b></p>
<h2>User locale and time zone</h2>
<p>Locale: <b><s:property value="getLocale()" /></b></p>

</body>
</html>