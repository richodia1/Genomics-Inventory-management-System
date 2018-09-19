<%@ include file="/common/taglibs.jsp"%>

<s:property value="state" />
<a href="<s:url action="request/order" />?id=<s:property value="id" />"><iita:text maxLength="60" value="%{title}" /></a>:
<s:property value="lastName" />,
<s:property value="firstName" />