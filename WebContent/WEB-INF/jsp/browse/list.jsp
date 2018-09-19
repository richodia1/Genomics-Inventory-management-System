<%@ include file="/common/taglibs.jsp"%>

<s:if test="results!=null">
	<s:push value="results">
		<s:include value="/WEB-INF/jsp/lot/tabular-list.jsp" />
	</s:push>
</s:if>