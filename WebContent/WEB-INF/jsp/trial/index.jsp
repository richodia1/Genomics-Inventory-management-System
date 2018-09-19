<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Trials</title>
</head>
<body iita:help="inventory/trials">
<s:if test="paged!=null">
	<s:push value="paged">
		<s:include value="/WEB-INF/jsp/paging.jsp" />
	</s:push>
	
	<s:push value="paged.results">
		<s:include value="tabular-trial.jsp" />
	</s:push>
</s:if>
</body>
</html>