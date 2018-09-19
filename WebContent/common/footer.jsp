<%@ include file="/common/taglibs.jsp"%>
<p>&nbsp;</p>
<hr size="1" />
<div style="float:right;font-size:9px;">Genebank Inventory - Ver. 2(3950:20110909)</div>
<s:action name="user/notification-quick" namespace="/" executeResult="true" ignoreContextParams="true" />

<%-- Render footer notificaiton messages, usually success messages --%>
<s:if test="notificationMessages!=null && notificationMessages.size>0">
	<div id="notificationMessages"><s:iterator value="notificationMessages">
		<div class="notificationMessage"><s:property escape="false" /></div>
	</s:iterator></div>
</s:if>

<div id="ajax-indicator" style="display: none;">
	<b>Please wait...</b> <img src="<c:url value="/img/ajax-ind-1.gif" />" />
</div>