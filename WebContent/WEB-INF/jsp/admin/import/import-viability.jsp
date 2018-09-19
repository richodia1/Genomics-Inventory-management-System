<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Import XLS data</title>
</head>
<body>
<h2>Viability Items Import</h2>
<s:form method="post" enctype="multipart/form-data" action="xlsviability-import!upload">
	<table class="inputform">
		<colgroup>
			<col width="200px" />
			<col />
		</colgroup>
		<tr>
			<td>XLS File:</td>
			<td><s:file name="uploads" accept="*.xls" /> <em>Format: ItemName | LotID | Barcode | Germ. 6d | Viability</em></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Upload" /></td>
		</tr>
	</table>
</s:form>

<s:if test="lots!=null">
	<h1>Viability in XLS file</h1>
	
	<s:set name="lots" value="lots" />
	<s:include value="viability-listing.jsp" />
	<!--
	<s:form method="post" action="xlslot-import!save">
		<s:submit value="Import to Database" />
	</s:form>-->
</s:if>
<s:if test="failedLotMappings!=null">
	<h1>Viability(s) import from XLS file with errors</h1>
	<p>Below is a list of viability record(s) that could not be found in the system! Kindly fix them before further import.</p>
	<s:push value="failedLotMappings">
		<s:include value="viability-missing.jsp" />
	</s:push>
</s:if>

</body>
</html>