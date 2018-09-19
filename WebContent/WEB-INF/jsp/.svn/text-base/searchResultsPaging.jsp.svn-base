<%@ include file="/common/taglibs.jsp"%>

<div style="margin: 0px 0px 2em; text-align: center;"><s:text name="search.paging.title" />: <c:if test="${param.page>1}">
	<a href="<s:url includeParams="none" action="search" />?q=<c:out value="${param.queryString}" />&amp;start=${(param.page-2)*param.pageSize}"
		style="color: Blue; font-weight: bold;"><s:text name="search.paging.previous" /></a>
</c:if> <c:forEach var="i" begin="1" end="${param.pages}">

	<a href="<s:url includeParams="none" action="search" />?q=<c:out value="${param.queryString}" />&amp;start=<c:out value="${(i-1)*param.pageSize}" />"
		<c:if test="${param.page==i}"> style="color: Red"</c:if>><c:out value="${i}" /></a>

</c:forEach> <c:if test="${param.page<param.pages}">
	<a href="<s:url includeParams="none" action="search" />?q=<c:out value="${param.queryString}" />&amp;start=${(param.page)*param.pageSize}"
		style="color: Blue; font-weight: bold;"><s:text name="search.paging.next" /></a>
</c:if></div>