<%@ include file="/common/taglibs.jsp"%>
<s:if test="top instanceof org.iita.util.PagedResult">
	<div style="margin: 10px 0px; text-align: center;">Pages: <c:if test="${page>4}">
		<a href="?${param.href}" style="color: Blue; font-weight: bold;">«</a>
	</c:if><c:if test="${page>1}">
		<a href="?startAt=${(page-2)*pageSize}&${param.href}" style="color: Blue; font-weight: bold;">←</a>
	</c:if> <c:forEach var="i" begin="${page>3 ? page-3 : 1}" end="${pages-page>4 ? page+3 : pages}">
		<a href="?startAt=<c:out value="${(i-1)*pageSize}" />&${param.href}" <c:if test="${page==i}"> style="color: Red"</c:if>><c:out value="${i}" /></a>
	</c:forEach> <c:if test="${0+page<0+pages}">
		<a href="?startAt=${(page)*pageSize}&${param.href}" style="color: Blue; font-weight: bold;">→</a>
	</c:if><c:if test="${0+page<0+pages}">
		<a href="?startAt=${(pages-1)*pageSize}&${param.href}" style="color: Blue; font-weight: bold;">»</a>
	</c:if> <span style="margin-left: 10px;">Total: <s:property value="totalHits" /></span></div>
</s:if><s:else>
<c:if test="${0+param.page <= 0+param.pages}">
	<div style="margin: 0px 0px 2em; text-align: center;">Pages: <c:if test="${param.page>10}">
		<a href="?${param.href}" style="color: Blue; font-weight: bold;">«</a>
	</c:if><c:if test="${param.page>1}">
		<a href="?startAt=${(param.page-2)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;">←</a>
	</c:if> <c:forEach var="i" begin="${param.page>10 ? param.page-10 : 1}" end="${param.pages-param.page>10 ? param.page+9 : param.pages}">
		<a href="?startAt=<c:out value="${(i-1)*param.pageSize}" />&${param.href}" <c:if test="${param.page==i}"> style="color: Red"</c:if>><c:out value="${i}" /></a>
	</c:forEach> <c:if test="${0+param.page<0+param.pages}">
		<a href="?startAt=${(param.page)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;">→</a>
	</c:if><c:if test="${0+param.page<0+param.pages}">
		<a href="?startAt=${(param.pages-1)*param.pageSize}&${param.href}" style="color: Blue; font-weight: bold;">»</a>
	</c:if></div>
</c:if></s:else>