<%@ include file="/common/taglibs.jsp"%>

<s:if test="lot!=null">
	<s:set name="title" value="%{lot.item.name + (lot.location ? ' in ' + lot.location.pathNames : '')}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Scan barcode'}" />
</s:else>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="#title" /></title>
</head>
<body>
<div style="margin: 0px 0px 20px 0px;"><s:form namespace="/lot" action="index" method="get">
	Barcode: <s:textfield label="Barcode" name="barcode" cssClass="autofocus" value="" />
	<s:submit value="Scan" />
</s:form></div>

<p>Use barcode scanner to scan the barcode on the container.</p>

</body>
</html>