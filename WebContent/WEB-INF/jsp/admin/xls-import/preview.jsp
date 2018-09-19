<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Preview data</title>
</head>
<body>
<s:include value="menu.jsp" />

<s:if test="results.size > 0">	
	<p>Total of <b><s:property value="results.size" /></b> entities found.</p>
	<s:form method="post" action="xls-import!store"><s:submit value="Store" /></s:form>
	<%@ include file="/WEB-INF/jsp/browse/list.jsp"%>	
</s:if>
<s:else>
	<p>No data found.</p>
</s:else>

</body>
</html>