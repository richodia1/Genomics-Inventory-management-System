<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Search results</title>
</head>
<body>
<s:if test="paged!=null && paged.pageSize>0">
	<s:push value="paged">
		<s:include value="/WEB-INF/jsp/paging.jsp">
			<s:param name="href" value="%{'tag=' + tag}" />
		</s:include>
	</s:push>


	<s:iterator value="paged.results">
		<div style="margin: 5px 0px; padding: 5px;">
		<s:if test="top instanceof org.iita.inventory.model.OrderTag">
			<s:push value="entity">
				<s:include value="/WEB-INF/jsp/request/short.jsp" />
			</s:push>
		</s:if>
		
		<s:else>
			<s:property value="top" /> <s:property value="top.class" />
		</s:else>
		<div style="font-size: 9px; margin: 3px 10px;">
		<s:iterator value="entity.tags">
			<s:include value="/WEB-INF/jsp/tags/tag-readonly.jsp" />
		</s:iterator>
		</div>
		</div>
	</s:iterator>

</s:if>



</body>
</html>