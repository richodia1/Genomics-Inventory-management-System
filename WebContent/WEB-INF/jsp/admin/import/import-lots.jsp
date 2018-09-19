<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Import XLS data</title>
</head>
<body>
<h2>Lot Items Import</h2>
<s:form method="post" enctype="multipart/form-data" action="xlslot-import!upload">
	<table class="inputform">
		<colgroup>
			<col width="200px" />
			<col />
		</colgroup>
		<tr>
			<td>XLS File:</td>
			<td><s:file name="uploads" accept="*.xls" /> <em>Format: AccName | Prefix | Location</em></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Upload" /></td>
		</tr>
	</table>
</s:form>

<s:if test="lots!=null">
	<h1>Lots in XLS file</h1>
	
	<s:set name="lots" value="lots" />
	<s:include value="lot-listing.jsp" />
	<!--
	<s:form method="post" action="xlslot-import!save">
		<s:submit value="Import to Database" />
	</s:form>-->
</s:if>
<s:if test="failedLotMappings!=null">
	<h1>Lot(s) import from XLS file with errors</h1>
	<p>Below is a list of lot record(s) that could not be found in the system! Kindly fix them manually.</p>
	<s:push value="failedLotMappings">
		<s:include value="lot-missing.jsp" />
	</s:push>
</s:if>

</body>
</html>