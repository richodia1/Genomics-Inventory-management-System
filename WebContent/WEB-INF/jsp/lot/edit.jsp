<%@ include file="/common/taglibs.jsp"%>

<s:if test="lot!=null">
	<s:set name="title" value="%{'Lot: ' + lot.item.name  + (lot.location ? ' in ' + lot.location.pathNames : '')}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Scan barcode'}" />
</s:else>

<html>
<head>
<title><s:property value="#title" /></title>
</head>
<body>
	<%@ include file="edit-form.jsp" %>
</body>
</html>