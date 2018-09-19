<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Browse inventory</title>
<script type="text/javascript" src="<c:url value='/script/table.js'/>"></script>
</head>
<body>
<div style="height: 30px;"><b>Location path:</b> <s:iterator value="locationPath">
	<a style="margin-right: 20px;" href="<s:url action="index"><s:param name="locid" value="id" /></s:url>"><s:property value="name" /></a>
</s:iterator></div>

<s:if test="results.size > 0">
	<s:form method="POST" namespace="/browse" onsubmit="javascript: return confirm('Are you sure you want to store new inventory data?');">
		<s:hidden name="locid" value="%{locid}" />
		<div style="margin: 5px 0px 20px 5px;"><s:submit action="inventorize!save" value="Save" theme="simple" /> <span style="margin-left: 30px; color: Red">You
		can now use arrow keys, Enter and Backspace to navigate!</span></div>

		<table class="data-listing" id="inventorytable">
			<colgroup>
				<col width="120" />
				<col width="130" />
				<col />
				<col />
				<col width="80" />
				<col width="130" />
				<col width="130" />
				<col width="110" />
			</colgroup>
			<thead>
				<tr>
					<td class="ar">Quantity</td>
					<td>Item</td>
					<td />
					<td />
					<td>Type</td>
					<td>Location</td>
					<td>Container</td>
					<td>Last modified</td>
				</tr>
			</thead>
			<tbody>
				<s:set name="lotItemId" value="null" />
				<s:iterator value="results" status="status">
					<s:if test="#lotItemId==null || #lotItemId!=item.id">
						<tr>
							<td colspan="2">
							<h3><s:property value="item.name" /></h3>
							</td>
							<td style="vertical-align: middle;" colspan="6">Introduction date: <s:textfield name="results[%{#status.index}].introductionDate" value="%{introductionDate}" theme="simple" /></td>
						</tr>
						<s:set name="lotItemId" value="item.id" />
					</s:if>
					<tr>
						<td class="ar"><s:textfield name="results[%{#status.index}].quantity" value="%{quantity}" theme="simple" cssStyle="width: 50px;" /> <s:property
							value="scale" /></td>
						<td><s:if test="id!=null">
							<s:url id="editUrl" namespace="/lot" action="index" includeParams="none">
								<s:param name="id" value="id" />
							</s:url>
							<b><s:a href="%{editUrl}">
								<s:property value="item.name" />
							</s:a></b>
						</s:if> <s:else>
							<s:property value="item.name" />
						</s:else></td>
						<td>(<s:textfield name="results[%{#status.index}].line" value="%{line}" cssStyle="width: 30px;" />)
							<s:if test="%{top instanceof org.iita.inventory.model.InVitroLot}">
							<s:textfield name="results[%{#status.index}].regenerationDate" value="%{regenerationDate}" theme="simple" cssStyle="width: 100px;" />
							VF:<s:checkbox name="results[%{#status.index}].virusFree" value="%{virusFree}" theme="simple"  />
						</s:if></td>
						<td class="ar"></td>
						<td><s:property value="item.itemType.name" /></td>
						<td><s:url namespace="/browse" action="index" id="locationUrl">
							<s:param name="locid" value="location.id" />
						</s:url><s:a href="%{locationUrl}">
							<s:property value="location.name" />
						</s:a></td>
						<td><s:property value="container.name" /></td>
						<td><s:date name="dateLastModified" format="%{getText('date.format')}" /></td>
					</tr>
				</s:iterator>
			</tbody>
		</table>
	</s:form>
	<script type="text/javascript">
	new IITA.TableNav($("inventorytable"));
</script>
</s:if>
<s:else>
	<p>No existing lots in this location.</p>
</s:else>

</body>
</html>