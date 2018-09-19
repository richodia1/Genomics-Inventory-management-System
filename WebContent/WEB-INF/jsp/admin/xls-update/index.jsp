<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>XLS Lot update</title>
</head>
<body>
<div style="margin: 0px 0px 20px 0px;"><s:form action="xls-update!test" method="post" enctype="multipart/form-data" theme="simple">
	<table class="inputform">
		<colgroup>
			<col width="180" />
			<col />
		</colgroup>
		<tr>
			<td class="tdLabel"><label>Importer:</label></td>
			<td><s:checkboxlist name="whatToUpdate"
				list="#{'quantity':'Quantity','scale':'Scale','weight':'Weight in g','weight100':'100 seed weight','germination':'Germination','moistureContent':'Moisture content','fieldLocation':'Field location','plantingDate':'Planting date','harvestDate':'Harvest date','germinationDate':'Germination date','yearProcessed':'Year processed','storageType':'Storage type','seedTested':'Seed tested','virusFree':'Virus free','conformity':'Conformity','referenceCode':'Reference Code'}" />
			</td>
		</tr>
		<tr>
			<td class="tdLabel"><label>File:</label></td>
			<td><s:file name="upload" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Test import" action="xls-update!test" /> <s:submit value="Import data" action="xls-update!update"
				onclick="javascript: return window.confirm('Are you sure you want to update Inventory data with supplied XLS data?');" /></td>
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
	<p>Upload an <b>Excel XLS</b> file first.</p>
</s:else>

<s:if test="lots!=null">
	<s:push value="lots">
		<s:include value="/WEB-INF/jsp/lot/tabular-list.jsp" />
	</s:push>
</s:if>
</body>
</html>