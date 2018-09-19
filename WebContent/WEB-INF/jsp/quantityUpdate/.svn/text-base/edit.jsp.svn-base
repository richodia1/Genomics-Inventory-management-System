<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Inventory bulk update</title>
</head>
<body>

<s:set name="readonly" value="description.status==1 || description.status==100" />

<s:if test="readonly">
	<s:form namespace="/update" action="saveDescription" method="POST">
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
				<col width="200" />
				<col />
			</colgroup>
			<tr style="font-weight: bold;">
				<td class="label"><s:text name="label.status" />:</td>
				<td colspan="3" colspan="3"><s:property value="%{getText('bulk.status.' + description.status)}" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="label.date" />:</td>
				<td colspan="3"><s:property value="description.date" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="label.title" />:</td>
				<td colspan="3"><s:property value="description.title" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="label.description" />:</td>
				<td colspan="3"><s:property value="description.description" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="transaction.type" />:</td>
				<td colspan="3"><s:property value="description.transactionType" /> <b><s:property value="description.transactionSubtype" /></b></td>
			</tr>
			<s:if test="description.order!=null">
			<tr>
				<td class="label">Order:</td>
				<td>This distribution is linked to <a href="<s:url action="request/order" namespace="/" />?id=<s:property value="description.order.id" />"><s:property value="description.order.title" /></a></td>
			</tr>
			</s:if>
			<tr>
				<td class="label">Created:</td>
				<td><iita:date name="description.createdDate" format="dd/MM/yyyy" /> by <s:property value="description.createdBy" /></td>
				<td class="label">Updated:</td>
				<td><iita:date name="description.lastUpdated" format="dd/MM/yyyy" /> by <s:property value="description.lastUpdatedBy" /></td>
			</tr>
			<tr>
				<td />
				<td colspan="3"><iita:authorize ifAnyGranted="ROLE_ADMIN,ROLE_SUPERVISOR">
					<s:if test="description.status == 1">
						<s:submit value="Revert changes" action="update!rollback" />
					</s:if>
				</iita:authorize> <s:submit value="Export to XLS" action="update!exportXLS" /> 
				<%-- <s:if test="description.transactionSubtype == 'VIABILITYTEST'">
					<s:submit action="update!viability" value="Report results" />
				</s:if>--%>
				<s:if test="description.transactionType==@org.iita.inventory.model.Transaction2$Type@OUT && description.status==1 && description.transactionSubtype == 'SAFEDUP'">
					<s:submit action="update!safeduplication" value="Print labels" />
				</s:if> <s:submit name="redirect-action:index" value="To list" />
				<s:if test="description.id!=null && description.order==null && description.transactionType==@org.iita.inventory.model.Transaction2$Type@OUT && description.transactionSubtype=='DISTRIBUTION'">
					<!--  create order? -->
					<s:submit action="update!createorder" value="Create germplasm request" />
				</s:if>
				</td>
			</tr>
		</table>
		<s:hidden name="id" value="%{description.id}" />
		<s:hidden name="description.version" value="%{description.version}" />
	</s:form>
</s:if>
<s:else>
	<s:form namespace="/update" action="saveDescription" method="POST">
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
				<col width="200" />
				<col />
			</colgroup>
			<tr>
				<td class="label"><s:text name="label.title" />:</td>
				<td colspan="3"><s:textfield name="description.title" value="%{description.title}" label="Title" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="label.date" />:</td>
				<td><iita:datepicker name="description.date" value="%{description.date}" format="dd/MM/yyyy" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="transaction.type" />:</td>
				<td><s:select label="Transaction type" name="description.transactionType" list="#{'OUT':'Outgoing','IN':'Incoming','RSET':'Monitoring'}"
					value="%{description.transactionType}" /></td>
				<td class="label">Subtype:</td>
				<td><s:select name="description.transactionSubtype" value="%{description.transactionSubtype}"
					list="#{'SYSTEM':'System', 'MONITOR':'Monitoring', 'DISTRIBUTION':'Distribution', 'CONTAMINATION':'Contamination', 'NECROSIS':'Necrosis', 'DISCARD':'Discard', 'SUBCULTURING':'Subculturing', 'SUBCULTURED':'Subcultured', 'SAFEDUP':'Safe duplication', 'VIABILITYTEST':'Viability test','REGENERATION':'Regeneration'}"
					emptyOption="true" /></td>
			</tr>
			<tr>
				<td class="label"><s:text name="label.description" />:</td>
				<td colspan="3"><s:textarea name="description.description" value="%{description.description}" label="Description" /></td>
			</tr>
			<tr>
				<td />
				<td colspan="3"><s:checkbox name="description.affectingInventory" value="%{description.affectingInventory}" /> <label
					for="saveDescription_description_affectingInventory">Quantities of lots should be appropriately updated in Inventory</label></td>
			</tr>
			<s:if test="description.order!=null">
			<tr>
				<td class="label">Order:</td>
				<td>This distribution is linked to <a href="<s:url action="request/order" namespace="/admin" />?id=<s:property value="description.order.id" />"><s:property value="description.order.title" /></a></td>
			</tr>
			</s:if>
			<s:if test="description.id!=null">
			<tr>
				<td class="label">Created:</td>
				<td><iita:date name="description.createdDate" format="dd/MM/yyyy" /> by <s:property value="description.createdBy" /></td>
				<td class="label">Updated:</td>
				<td><iita:date name="description.lastUpdated" format="dd/MM/yyyy" /> by <s:property value="description.lastUpdatedBy" /></td>
			</tr>
			</s:if>
			<tr>
				<td />
				<td colspan="3"><s:if test="description.id==null">
					<s:submit value="Start editing" />
				</s:if><s:else>
					<s:submit value="Update description" />
					<s:submit value="Export to XLS" action="update!exportXLS" />
				</s:else> <s:if test="description.status == 0 && lots.size>0">
					<s:submit value="Commit changes" action="update!commit"
						onclick="javascript: return confirm('Are you sure you want to commit the changes?\nDouble check the list before commiting.');" />
				</s:if> <iita:authorize ifAnyGranted="ROLE_ADMIN,ROLE_SUPERVISOR">
					<s:if test="description.status == 1">
						<s:submit value="Revert changes" action="update!rollback" />
					</s:if>
				</iita:authorize> <s:submit name="redirect-action:index" value="To list" /> <iita:authorize ifAllGranted="ROLE_ADMIN">
					<s:submit value="Delete bulk" action="update!deleteBulk" />
				</iita:authorize></td>
			</tr>
		</table>
		<s:hidden name="id" value="%{description.id}" />
		<s:hidden name="description.version" value="%{description.version}" />
	</s:form>
</s:else>


<s:if test="!readonly && description.id != null && description.status == 0">
	<h2>Inventory item</h2>
	<s:form namespace="/update" action="update!scanLot" method="POST">
		<s:hidden name="activeScale" value="%{activeScale}" />
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
			</colgroup>
			<tr>
				<td class="label"><label>Scan barcode:</label></td>
				<td><s:textfield name="barCode" value="" label="Scan barcode" cssClass="autofocus" cssStyle="width: 100px;" />
				<s:submit value="Scan" /></td>
			</tr>
		</table>
		<s:hidden name="id" value="%{description.id}" />
	</s:form>
	<s:if test="lot != null">
		<s:form namespace="/update" action="updateItem" method="POST">
			<table class="inputform">
				<colgroup>
					<col width="200" />
					<col />
				</colgroup>
				<tr>
					<td class="label"><label>Item:</label></td>
					<td style="font-weight: bold;"><s:url id="lotUrl" namespace="/lot" action="index" includeParams="none">
						<s:param name="id" value="lot.lot.id" />
					</s:url> <s:a href="%{lotUrl}">
						<s:property value="lot.lot.item.name" />
					</s:a></td>
				</tr>
				<tr>
					<td class="label">Location:</td>
					<td><s:url id="locationUrl" namespace="/browse" action="index" includeParams="none">
						<s:param name="locid" value="lot.lot.location.id" />
					</s:url> <s:a href="%{locationUrl}">
						<s:property value="lot.lot.location.pathNames" />
					</s:a></td>
				</tr>
				<tr>
					<td class="label">Current quantity:</td>
					<td style="font-weight: bold;"><s:property value="lot.lot.quantity" /> <s:property value="lot.lot.scale" /></td>
				</tr>
				<tr>
					<td class="label">Quantity:</td>
					<td><s:textfield name="lot.quantity" value="%{lot.quantity}" label="Quantity" cssClass="autofocus" cssStyle="width: 100px;" /> <s:if
						test="description.affectingInventory">
						<b><s:property value="lot.scale" /></b>
					</s:if><s:else>
						<!-- Other scales -->
						<s:select name="activeScale" value="%{lot.scale}" list="{'g', 'seed', 'tube', 'plant', 'stem', 'tuber', 'sucker', 'leaf', 'root','cutting'}"
							listValue="%{getText('lot.scale.' + toString())}" />
					</s:else>
					<!-- If linked to order -->
					<s:if test="description.order!=null">
						Requested for <b><s:property value="description.order.findItem(lot.lot).quantity" /></b>.
					</s:if>
					
					</td>
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

	<h2>Inventory update list</h2>
	<s:if test="description.lots.size == 0 && !getUser().hasRole('ROLE_BREEDER')">
		<s:if test="description.order!=null">
			<s:include value="updates-order-table.jsp" />
		</s:if>
		<s:else>
			<p>No lots have been added to the list for stock update.</p>
		</s:else>
	</s:if>
	<s:else>
		<s:if test="description.id != null && description.status == 1 && !getUser().hasRole('ROLE_BREEDER')">
		<div class="actionMessage">
		<s:form method="POST" action="trial/create" namespace="/">
			<s:hidden name="source" value="bulk" />
			<s:hidden name="bulkId" value="%{description.id}" />
			<s:submit value="Create trial" />
		</s:form>
		</div>
		</s:if>
		
		<s:if test="description.order==null">
			<s:include value="updates-table.jsp" />
		</s:if>
		<s:else>
			<s:include value="updates-order-table.jsp" />
		</s:else>
	</s:else>
</body>
</html>