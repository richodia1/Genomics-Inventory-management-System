<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<title>Genebank Inventory</title>
</head>
<body>
<table style="width: 100%">
	<colgroup>
		<col width="50%" />
		<col width="50%" />
	</colgroup>
	<s:if test="!getUser().hasRole('ROLE_BREEDER')">
	<tr>
		<td>
		<h2>Common Inventory</h2>
		<ul>
			<li><a href="<c:url value="update/index.jspx" />">View inventory updates</a></li>
			<li><a href="<c:url value="update/update!input.jspx" />">Register <b>new inventory update</b></a></li>
		</ul>
		<ul>
			<li><a href="<s:url action="trial/index" />" title="List all trials">List trial data</a></li>
			<li><a href="<s:url action="trial/create" />" title="Create trial">Create trial</a></li>
			<iita:authorize ifAnyGranted="ROLE_ORDER">
			<li><a href="<s:url action="request/edit!create" />">Create new Germplasm request</a></li>
			</iita:authorize>
		</ul>
		</td>
		<td style="padding-left: 10px">
		<h2>Tissue culture</h2>
		<ul>
			<li><a href="update/index.jspx?type=NECROSIS&type=CONTAMINATION" title="Necrosis/Contamination checks">Review necrosis/contamination checks</a></li>
			<li><a href="update/invitro/monitor!addNecro.jspx" title="Necrosis check">Register necrosis</a></li>
			<li><a href="update/invitro/monitor!addConta.jspx" title="Contamination check">Register contamination</a></li>
		</ul>
		<ul>
			<li><a href="update/invitro/subculture!start.jspx" title="Begin subculturing">Begin new subculturing batch</a></li>
			<li><a href="update/index.jspx?type=SUBCULTURING&type=SUBCULTURED" title="View current subculture status">View current subculture status</a></li>
		</ul>
		</td>
	</tr>
	</s:if>
	<tr>
		<td>
		<h2>Items and Lots</h2>
		<ul>
			<li><a href="item/index.jspx" title="Location management">Browse items</a></li>
			<security:authorize ifAnyGranted="ROLE_MANAGER,ROLE_ADMIN,ROLE_USER">
				<s:if test="!getUser().hasRole('ROLE_BREEDER')">
					<li><a href="<s:url action="item/edit" />">Add new inventory item</a></li>
				</s:if>
				<s:else>
				<li><a href="<s:url action="request/index" />">Check Requests</a></li>
				<li><a href="<s:url action="request/edit!create" />">Make Request</a></li>
				</s:else>
			</security:authorize>
			<li><a href="browse/summary.jspx" title="Summary of lot stock per location">Summary overview</a></li>
			<li><a href="browse/variables.jspx" title="Overview of lot variables">Variables overview</a></li>
		</ul>
		</td>
		<td style="padding-left: 10px">
		<s:if test="!getUser().hasRole('ROLE_BREEDER')">
			<h2>Tools</h2>
			<ul>
				<security:authorize ifAnyGranted="ROLE_USER">
					<li><a href="location/" title="Location management">Register and rename locations</a></li>
				</security:authorize>
				<li><a href="update/migrate.jspx" title="Lot migration">Migrate lots</a></li>
				<li><a href="<s:url action="transaction/list" />" title="Lot migration">Transactions</a></li>
				<security:authorize ifAnyGranted="ROLE_MANAGER,ROLE_ADMIN">
					<li><a href="conf/itemtypes.jspx">Item types</a></li>
					<li><a href="conf/containers.jspx">Container types</a></li>
				</security:authorize>
			</ul>
		</s:if>
		</td>
	</tr>
	<s:if test="!getUser().hasRole('ROLE_BREEDER')">
	<tr>
		<td>
		<h2>Open <a href="<s:url action="trial/index" />" title="List all trials">trials</a></h2>
		<s:action name="trial/quicklist!openTrials" namespace="/" executeResult="true" />
		</td>
		<td style="padding-left: 10px">
		<h2>My trials</h2>
		<s:action name="trial/quicklist!myTrials" namespace="/" executeResult="true" /></td>
	</tr>
	</s:if>
</table>
</body>
</html>