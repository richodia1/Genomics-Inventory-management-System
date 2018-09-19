<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Scale selection</title>
</head>
<body>
<div class="actionMessage">Please select your scale.</div>
<s:form method="post" action="scale!select">
	<s:iterator value="scales">
		<div style="margin: 5px 0px;">
			<input type="radio" name="myScale" value="<s:property value="id" />" id="pi_<s:property value="id" />" /> 
			<label for="pi_<s:property value="id" />"><s:property value="host" /> at <s:property value="port" /></label>
		</div>
	</s:iterator>
	<div class="button-bar"><s:submit name="Select scale" /></div>
</s:form>
</body>
</html>