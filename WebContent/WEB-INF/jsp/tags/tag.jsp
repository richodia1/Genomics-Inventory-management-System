<%@ include file="/common/taglibs.jsp"%>
<div id="tag_${id}" class="tag">
<a title="Browse all entities with tag '${tag}'" href="<s:url action="tag/browse" />?tag=<s:property value="tag" />"><s:property value="tag" /></a> <s:if test="percentage!=null">(<s:property value="percentage" /> %)</s:if>
</div>
<iita:authorize ifAnyGranted="ROLE_CGO">
<iita:inlineeditor id="tagForm" targetId="tag_${id}">
	<s:include value="tag-form.jsp" />
</iita:inlineeditor>
</iita:authorize>