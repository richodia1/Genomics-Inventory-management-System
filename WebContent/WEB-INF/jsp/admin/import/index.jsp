<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Import CSV data</title>
</head>
<body>
<div style="margin: 0px 0px 20px 0px;">
<s:form action="import" method="POST" enctype="multipart/form-data" theme="simple">
	<table class="inputform">
		<colgroup>
			<col width="180" />
			<col />
		</colgroup>
		<tr>
			<td class="tdLabel"><label>Importer:</label></td>
			<td><s:select name="importer"
				list="#{'org.iita.inventory.importer.SeedLotImporter':'Seed lots','org.iita.inventory.importer.MusaImporter':'Musa','org.iita.inventory.importer.CassavaImporter':'Cassava'}" />
			</td>
		</tr>
		<tr>
			<td class="tdLabel"><label>File:</label></td>
			<td><s:file name="upload" /></td>
		</tr>
		<tr>
			<td class="tdLable"><label>VM Charset:</label></td>
			<td><s:property value="@java.nio.charset.Charset@defaultCharset()" /></td>
		</tr>
		<tr>
			<td class="tdLable"><label>File Charset:</label></td>
			<td><s:select name="charset" value="%{@java.nio.charset.Charset@forName(charset)}" list="@java.nio.charset.Charset@availableCharsets()" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Test import" action="import!test" /> <s:submit value="Import data"
				onclick="javascript: return window.confirm('Are you sure you want to import data to Inventory System?');" /></td>
		</tr>
	</table>
</s:form></div>

<s:if test="upload.size>0">
	<p>Files uploaded:</p>
	<ul>
		<s:iterator value="upload" status="status">
			<li><b><s:property value="uploadFileName[#status.index]" /></b> <s:property value="length()" /> bytes</li>
		</s:iterator>
	</ul>
</s:if>
<s:else>
	<p>Upload a CSV file first.</p>
</s:else>

<s:if test="importedLots!=null">
	<s:set name="results" value="importedLots" />
	<%@ include file="/WEB-INF/jsp/browse/list.jsp"%>
</s:if>
</body>
</html>