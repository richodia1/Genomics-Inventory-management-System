<%@ include file="/common/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Register inventory lot</title>
</head>
<body>
<h1>Registering new <s:text name="lot.type.%{lotType}" /></h1>
<p><a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${lot.item.id}&lotType=seedlot"><b>New seed lot</b> for ${lot.item.name}</a> 
	<a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${lot.item.id}&lotType=invitrolot"><b>New invitro lot</b> for ${lot.item.name}</a>
	<a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${lot.item.id}&lotType=fieldlot"><b>New field lot</b> for ${lot.item.name}</a>
	<a style="margin-right: 20px" href="<s:url action="choosecryo" namespace="/lot" />?item=${lot.item.id}&lotType=cryolot"><b>New cryo lot</b> for ${lot.item.name}</a>
	<a href="<s:url action="register" namespace="/lot" />?item=${lot.item.id}&lotType=otherlot"><b>New other lot</b> for ${lot.item.name}</a>
	</p>
<s:form action="register!register" namespace="/lot" method="POST">
	<s:hidden name="item" value="%{lot.item.id}" />
	<s:hidden name="lotType" value="%{lotType}" />
	<table class="inputform">
		<colgroup>
			<col width="20%" />
			<col width="30%" />
			<col width="20%" />
			<col width="30%" />
		</colgroup>
		<tr class="identifying">
			<td class="label">Name:</td>
			<td><a href="<c:url value="/item/${lot.item.itemType.shortName}/${lot.item.name}" />"><s:property value="lot.item.name" /></a></td>
			<s:if test="lot.item.alternativeIdentifier!=null">
				<td class="label">Alternative identifier:</td>
				<td><s:property value="lot.item.alternativeIdentifier" /></td>
			</s:if>
		</tr>
		<tr class="separator" />
		<tr>
			<td class="label">Quantity:</td>
			<td><s:textfield name="lot.quantity" value="%{lot.quantity}" cssClass="numeric-input" /> <s:select name="lot.scale" list="lot.uoms"
				listValue="%{getText('lot.scale.' + toString())}" /></td>
			<td></td><td></td>
		</tr>
		<tr>
			<td class="label"><label>Line:</label></td>
			<td><s:textfield name="lot.line" value="%{lot.line}" cssStyle="width:auto;" /></td>
			<td></td><td></td>
		</tr>
		<s:if test="lot instanceof org.iita.inventory.model.SeedLot">
			<%@ include file="editSeedLot.jsp"%>
		</s:if>
		<s:elseif test="lot instanceof org.iita.inventory.model.InVitroLot">
			<%@ include file="editInVitroLot.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.FieldLot">
			<%@ include file="editFieldLot.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.CryoLotLts">
			<%@ include file="editCryoLotLts.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.CryoLotTemp">
			<%@ include file="editCryoLotTp.jsp"%>
		</s:elseif>
		<s:elseif test="lot instanceof org.iita.inventory.model.CryoLotSd">
			<%@ include file="editCryoLotSd.jsp"%>
		</s:elseif>		
		<s:elseif test="lot instanceof org.iita.inventory.model.OtherLot">
			
		</s:elseif>
		<s:else>
			<!-- GENERIC -->
		</s:else>
		<tr>
			<td class="label">Location detail:</td>
			<td colspan="3"><s:textfield name="lot.locationDetail" value="%{lot.locationDetail}" /></td>
		</tr>
		<tr>
			<td class="label">Notes:</td>
			<td colspan="3"><s:textarea name="lot.notes" value="%{lot.notes}" cssClass="sizable-textarea" /></td>
		</tr>
		<tr>
			<td />
			<td><s:submit value="Update" /> <s:submit name="redirect:%{referer}" value="Back" /></td>
		</tr>
	</table>
	<s:hidden name="referer" value="%{referer}" />
</s:form>
</body>
</html>