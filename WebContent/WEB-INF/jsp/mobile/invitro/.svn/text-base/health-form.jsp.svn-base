<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>In-Vitro germplasm health: <s:property value="description.transactionType" /></title>
</head>
<body>

<s:set name="readonly" value="description.status==1" />

<s:if test="!readonly && description.id != null && description.status == 0">
	<s:form action="invitro/monitor!scanLot" method="POST">
		<s:include value="../scanbarcode.jsp" />
		<s:hidden name="id" value="%{description.id}" />
	</s:form>
	<s:if test="lot != null && false">
		<s:form action="invitro/monitor!updateItem" method="POST">
			<table class="inputform">
				<colgroup>
					<col width="20%" />
					<col width="30%" />
					<col width="20%" />
					<col width="30%" />
				</colgroup>
				<tr>
					<td class="tdLabel"><label>Item:</label></td>
					<td style="font-weight: bold;"><s:url id="lotUrl" namespace="/lot" action="index" includeParams="none">
						<s:param name="id" value="lot.lot.id" />
					</s:url> <s:a href="%{lotUrl}">
						<s:property value="lot.lot.item.name" />
					</s:a></td>
					<td class="tdLabel">Location:</td>
					<td><s:url id="locationUrl" namespace="/browse" action="index" includeParams="none">
						<s:param name="locid" value="lot.lot.location.id" />
					</s:url> <s:a href="%{locationUrl}">
						<s:property value="lot.lot.location.pathNames" />
					</s:a></td>
				</tr>
				<tr>
					<td class="tdLabel">Quantity:</td>
					<td><s:textfield name="lot.quantity" value="%{lot.quantity}" label="Quantity" cssClass="autofocus" cssStyle="width: 100px;" /></td>
					<td class="tdLabel">Current quantity:</td>
					<td style="font-weight: bold;"><s:property value="lot.lot.quantity" /> <s:property value="lot.lot.scale" /></td>
				</tr>
				<tr>
					<td />
					<td><s:submit value="Update lot" /> <s:reset value="Reset form" /></td>
				</tr>
			</table>
			<s:hidden name="id" value="%{description.id}" />
			<s:hidden name="itemid" value="%{lot.id}" />
			<s:hidden name="lot.lot.id" value="%{lot.lot.id}" />
		</s:form>
	</s:if>
</s:if>

<s:if test="description.id != null">
	<h2><s:property value="description.transactionType" /> for <s:property value="description.transactionSubtype" /></h2>
	<s:if test="description.lots.size == 0">
		<p>No lots have been added to the list for stock update.</p>
	</s:if>
	<s:else>
		<s:set name="totalQty" value="0" />
		<table class="data-listing">
			<thead>
				<tr>
					<td>Item</td>
					<td class="ar">Quantity</td>
					<s:if test="description.status==0"><td style="text-align: right;">Tools</td></s:if>
				</tr>
			</thead>
			<tbody>
				<s:iterator value="description.sortedLots" status="status">
					<tr <s:if test="#readonly==false and lot.quantity - quantity < 0">class="row-error"</s:if>>
						<td class="identifying"><a href="<s:url action="lot" />?id=<s:property value="lot.id" />"><s:property value="lot.item.name" /></a></td>
						<td class="ar"><s:property value="quantity" /> <s:set name="totalQty" value="%{#totalQty+quantity}" />
						<s:if test="description.status==0"> of <s:property value="lot.quantity" /></s:if></td>
						<s:if test="description.status==0">
						<td style="text-align: right">
							<s:url id="removeUrl" action="invitro/monitor!removeItem">
								<s:param name="id" value="%{description.id}" />
								<s:param name="itemid" value="%{id}" />
							</s:url>
							<s:a href="%{removeUrl}">Del</s:a>
						</td>
						</s:if>
					</tr>
				</s:iterator>
			</tbody>
			<tfoot>
				<tr>
					<td class="ar"><s:property value="description.lots.size" /> lots</td>
					<td class="ar"><s:property value="#totalQty" /> tube</td>
				</tr>
			</tfoot>
		</table>
	</s:else>

	<s:form action="invitro/monitor!saveDescription" method="POST">
		<s:hidden name="id" value="%{description.id}" />
		<s:hidden name="description.version" value="%{description.version}" />
		<s:if test="description.status == 0 && lots.size>0">
			<s:submit value="Commit changes" action="invitro/monitor!commit"
				onclick="javascript: return confirm('Are you sure you want to commit the changes?\nDouble check the list before commiting.');" />
		</s:if>
		<s:elseif test="description.status==0 && lots.size==0">
			<s:submit value="Delete bulk" action="invitro/monitor!deleteBulk" onclick="javascript: return confirm('Are you sure you want to delete the entire list?');" />
		</s:elseif>
		<s:elseif test="description.status == 1">
			<s:submit value="Revert changes" action="invitro/monitor!rollback" onclick="javascript: return confirm('Are you sure you want to REVERT the changes?');" />
		</s:elseif>
		<s:else>
			<authz:authorize ifAllGranted="ROLE_ADMIN">
				<s:submit value="Delete bulk" action="invitro/monitor!deleteBulk" onclick="javascript: return confirm('Are you sure you want to delete the entire list?');" />
			</authz:authorize>
		</s:else>
		<input type="button" onClick="javascript: window.location.href='<s:url action="updates" />?type=<s:property value="description.transactionSubtype" />'; return true;" value="To list" />
	</s:form>

	<h2>More information</h2>
</s:if>

<s:if test="readonly">
	<s:form action="invitro/monitor!saveDescription" method="POST">
		<table class="inputform">
			<tr>
				<td class="tdLabel"><s:text name="label.id" />:</td>
				<td><s:property value="description.id" /> @ <s:property value="description.version" /> Status: <s:property value="description.status" /></td>
			</tr>
			<tr style="font-weight: bold;">
				<td class="tdLabel"><s:text name="label.status" />:</td>
				<td><s:property value="%{getText('bulk.status.' + description.status)}" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="label.date" />:</td>
				<td><s:property value="description.date" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="label.title" />:</td>
				<td><s:property value="description.title" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="transaction.type" />:</td>
				<td><s:property value="description.transactionType" /> <b><s:property value="description.transactionSubtype" /></b></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="label.description" />:</td>
				<td><s:property value="description.description" /></td>
			</tr>
			<tr>
				<td />
				<td><s:submit value="Export to XLS" action="invitro/monitor!exportXLS" /> <authz:authorize ifAnyGranted="ROLE_ADMIN,ROLE_SUPERVISOR">
					<s:if test="description.status == 1">
						<s:submit value="Revert changes" action="invitro/monitor!rollback" onclick="javascript: return confirm('Are you sure you want to REVERT the changes?');" />
					</s:if>
				</authz:authorize> <input type="button" onClick="javascript: window.location.href='<s:url action="updates" />?type=<s:property value="description.transactionSubtype" />'; return true;" value="To list" /></td>
			</tr>
		</table>
		<s:hidden name="id" value="%{description.id}" />
		<s:hidden name="description.version" value="%{description.version}" />
	</s:form>
</s:if>
<s:else>
	<s:form action="invitro/monitor!saveDescription" method="POST">
		<table class="inputform">
			<tr>
				<td class="tdLabel"><s:text name="label.title" />:</td>
				<td><s:textfield name="description.title" value="%{description.title}" label="Title" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="label.date" />:</td>
				<td><s:textfield name="description.date" value="%{description.date}" label="Date" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="transaction.type" />:</td>
				<td><s:select label="Transaction type" name="description.transactionType" list="#{'OUT':'Outgoing','IN':'Incoming'}"
					value="%{description.transactionType}" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Subtype:</td>
				<td><s:select name="description.transactionSubtype" value="%{description.transactionSubtype}"
					list="#{'SYSTEM':'System', 'DISTRIBUTION':'Distribution', 'CONTAMINATION':'Contamination', 'NECROSIS':'Necrosis', 'DISCARD':'Discard', 'SUBCULTURING':'Subculturing', 'SUBCULTURED':'Subcultured', 'SAFEDUP':'Safe duplication', 'VIABILITYTEST':'Viability test'}"
					emptyOption="true" /></td>
			</tr>
			<tr>
				<td class="tdLabel"><s:text name="label.description" />:</td>
				<td><s:textarea name="description.description" value="%{description.description}" label="Description" /></td>
			</tr>
			<tr>
				<td />
				<td><s:if test="description.id==null">
					<s:submit value="Start editing" />
				</s:if><s:else>
					<s:submit value="Update description" />
					<s:submit value="Export to XLS" action="invitro/monitor!exportXLS" />
				</s:else> <s:if test="description.status == 0 && lots.size>0">
					<s:submit value="Commit changes" action="invitro/monitor!commit"
						onclick="javascript: return confirm('Are you sure you want to commit the changes?\nDouble check the list before commiting.');" />
				</s:if> <s:elseif test="lots.size==0">
					<s:submit value="Delete bulk" action="invitro/monitor!deleteBulk" onclick="javascript: return confirm('Are you sure you want to delete the entire list?');" />
				</s:elseif> <s:else>
					<authz:authorize ifAllGranted="ROLE_ADMIN">
						<s:submit value="Delete bulk" action="invitro/monitor!deleteBulk"
							onclick="javascript: return confirm('Are you sure you want to delete the entire list?');" />
					</authz:authorize>
				</s:else> <input type="button" onClick="javascript: window.location.href='<s:url action="updates" />?type=<s:property value="description.transactionSubtype" />'; return true;" value="To list" /></td>
			</tr>
		</table>
		<s:hidden name="id" value="%{description.id}" />
		<s:hidden name="description.version" value="%{description.version}" />
	</s:form>
</s:else>

</body>
</html>