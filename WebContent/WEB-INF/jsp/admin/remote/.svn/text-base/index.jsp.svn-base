<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Remote orders</title>
</head>
<body>
<h1>Remote stuff</h1>
<ul>
	<s:iterator value="orders" status="status">
		<li><s:property value="id" /> <s:date name="dateCreated" format="dd/MM/yyyy" /> Last modified: <s:date name="dateModified" format="dd/MM/yyyy" /><br />
		<s:property value="lastName" />, <s:property value="firstName" /><br />
		<s:property value="email" /><br />
		<s:property value="instituteName" />, <s:property value="instituteType" /><br />
		<s:property value="postalCode" /> <s:property value="town" />, <s:property value="country" /><br />
		<s:property value="intendedUse" /><br />
		<s:property value="projectDesc" /><br />
		</li>
	</s:iterator>
</ul>
</body>
</html>