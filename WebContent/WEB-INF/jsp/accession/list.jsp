<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Accessions</title>
</head>
<body>
<s:url id="addNew" namespace="/accession" action="crud!edit" />
<s:a href="%{addNew}">Add new</s:a>

<s:if test="accessions.size > 0">
	<table style="width: 100%">
		<thead>
			<tr>
				<td>Crop</td>
				<td>Prefix</td>
				<td>Accession Identifier</td>
				<td>Name</td>
				<td>Toolbox</td>
		</thead>
		<tbody>
			<s:iterator value="accessions">
				<tr id="row_<s:property value="id"/>">
					<td><s:property value="crop.name" /></td>
					<td><s:property value="prefix" /></td>
					<td><s:property value="accessionIdentifier" /></td>
					<td><s:property value="name" /></td>
					<td><s:url id="removeUrl" namespace="/accession" action="crud!remove">
						<s:param name="id" value="id" />
					</s:url> <s:a href="%{removeUrl}" theme="ajax" targets="accessions">Remove</s:a> <s:url id="editUrl" namespace="/accession" action="crud!edit">
						<s:param name="id" value="id" />
					</s:url> <s:a href="%{editUrl}" theme="ajax" targets="accessions">Edit</s:a></td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</s:if>
</body>
</html>