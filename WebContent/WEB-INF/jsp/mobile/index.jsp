<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Genebank Mobile</title>
</head>
<body>

<h1>Mobile Inventory</h1>
<s:form namespace="/mobile" action="lot" method="GET">
	<s:include value="scanbarcode.jsp" />
</s:form>

<h2>Tools</h2>
<table class="data-listing">
	<tr>
		<td>Necrosis</td>
		<td><a href="updates.jspx?type=NECROSIS" title="Necrosis/Contamination checks">List all</a> <a href="invitro/monitor!addNecro.jspx" title="Necrosis check">Add new</a></td>
	</tr>
	<tr>
		<td>Contamination</td>
		<td><a href="updates.jspx?type=CONTAMINATION" title="Necrosis/Contamination checks">List all</a> <a href="invitro/monitor!addConta.jspx"
			title="Contamination check">Add new</a></td>
	</tr>
</table>
</body>
</html>