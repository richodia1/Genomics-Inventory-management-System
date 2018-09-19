<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Search results</title>
</head>
<body>
<div style="margin: 5px 0px 20px 5px;"><s:form method="POST" namespace="/selection">
	<s:submit action="index!clearAll" value="Clear selection" theme="simple" />
	<span style="margin-left: 100px;">Lots in selection: <b><s:property value="selectionService.selectedList.selectedLots.size" /></b></span>
</s:form></div>


<s:if test="results.size > 0">
	<%@ include file="/WEB-INF/jsp/browse/list.jsp"%>
</s:if>
<s:else>
	<s:if test="q!=null">
		<p>There are no matches to your query: <b><s:property value="q" /></b></p>
	</s:if>
	<s:else>
		<p>Use the search box above to search for lots.</p>
	</s:else>
</s:else>


</body>
</html>