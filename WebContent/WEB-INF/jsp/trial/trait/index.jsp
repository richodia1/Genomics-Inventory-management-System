<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Traits</title>
</head>
<body iita:help="inventory/trials">
<s:if test="paged!=null">
	<s:push value="paged">
		<s:include value="/WEB-INF/jsp/paging.jsp" />
	</s:push>
	
	<s:push value="paged.results">
		<s:include value="tabular-traits.jsp" />
	</s:push>
</s:if>
</body>
</html>