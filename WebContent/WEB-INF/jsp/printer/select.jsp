<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Printer selection</title>
</head>
<body>
<div class="actionMessage">Please select your printer.</div>
<s:form method="post" action="printer!select">
	<s:iterator value="printers">
		<div style="margin: 5px 0px;">
			<input type="radio" name="myPrinter" value="<s:property value="id" />" id="pi_<s:property value="id" />" /> 
			<label for="pi_<s:property value="id" />"><s:property value="name" /> at <s:property value="location" /></label>
		</div>
	</s:iterator>
	<div class="button-bar"><s:submit name="Select printer" /></div>
</s:form>
</body>
</html>