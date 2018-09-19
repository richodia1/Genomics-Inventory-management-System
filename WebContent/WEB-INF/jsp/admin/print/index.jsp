<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Printers</title>
</head>
<body>
<div style="margin-bottom: 20px"><s:form>
	<!--<s:submit name="redirect-action:update!input" value="New printer" theme="simple" />-->
<s:submit name="redirect-action:update!input"   value="New system printer" theme="simple" />
<!-- <s:submit name="redirect-action:update!newNetwork" value="New network printer" theme="simple" />-->
</s:form></div>

<s:if test="results.size>0">
	<table class="data-listing">
		<colgroup>
			<col width="200" />
			<col width="200" />
			<col width="200" />
			<col />
			<col width="100" />
		</colgroup>
		<thead>
			<tr>
				<td>Name</td>
				<td>Location</td>
				<td>Paper</td>
				<td>Authorized IPs</td>
				<td>Action</td>
			</tr>
		</thead>
		<tbody>
			<s:iterator value="results">
				<tr>
					<s:url id="editUrl" namespace="/admin/print" action="update!input">
						<s:param name="id" value="id" />
					</s:url>
					
					<s:url id="deleteUrl" namespace="/admin/print" action="delete">
						<s:param name="id" value="id" />
					</s:url>
					<td class="identifying"><s:a href="%{editUrl}">
						<s:property value="name" />
					</s:a></td>
					<td><s:property value="location" /></td>
					<td><s:property value="labelInfo.name" /></td>
					<td><s:property value="allowedIPaddresses" /></td>
					<td>
					<s:a href="%{deleteUrl}" onclick="if(confirm('Delete anyway?')){ return true;}else{return false;}">
						Delete
					</s:a>
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
<s:else>
	<p>No printers registered.</p>
</s:else>
</body>
</html>