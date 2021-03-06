<%@ include file="/common/taglibs.jsp"%>

<s:if test="item.id!=null">
	<s:set name="title" value="%{'Inventory item: ' + item.name}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Register new inventory item'}" />
</s:else>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><s:property value="#title" /></title>
</head>
<body>

<s:if test="item.id!=null">	
	<s:form action="item/edit" method="POST" theme="simple">
		<table class="inputform">
			<colgroup>
				<col width="20%" />
				<col width="30%" />
				<col width="20%" />
				<col width="30%" />
			</colgroup>
			<tr class="identifying">
				<td class="tdLabel">Name:</td>
				<td><s:property value="item.name" /></td>
				<s:if test="item.alternativeIdentifier!=null">
					<td class="tdLabel">Alternative identifier:</td>
					<td><s:property value="item.alternativeIdentifier" /></td>
				</s:if>
			</tr>
			<tr class="identifying">
				<td class="tdLabel">Prefix:</td>
				<td><s:property value="item.prefix" /></td>
				<td class="tdLabel">Accession identifier:</td>
				<td><s:property value="item.accessionIdentifier" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Latin name:</td>
				<td colspan="3"><s:textfield name="item.latinName" value="%{item.latinName}" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Notes:</td>
				<td colspan="3"><s:textarea name="item.notes" value="%{item.notes}" cssClass="sizable-textarea" /></td>
			</tr>			
			<tr>
				<td />
				<td colspan="3">
				<iita:authorize ifNotGranted="ROLE_BREEDER">
					<s:submit value="Update" action="item/edit!save" /> <iita:authorize ifAnyGranted="ROLE_ADMIN, ROLE_MANAGER">
					<s:submit action="item/edit!remove" value="Remove" onclick="javascript: return window.confirm('Are you sure you want to completely delete this item?');" />
				</iita:authorize></iita:authorize> <s:submit name="redirect:%{referer}" value="Back" /></td>
			</tr>
		</table>
		<s:hidden name="referer" value="%{referer}" />
		<s:hidden name="id" value="%{item.id}" />
	</s:form>
	
	<iita:authorize ifNotGranted="ROLE_BREEDER">
		<h2>Lots</h2>
		<p><a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${item.id}&lotType=seedlot"><b>New seed lot</b> for ${item.name}</a> 
		<a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${item.id}&lotType=invitrolot"><b>New invitro lot</b> for ${item.name}</a>
		<a style="margin-right: 20px" href="<s:url action="register" namespace="/lot" />?item=${item.id}&lotType=fieldlot"><b>New field lot</b> for ${item.name}</a>
		<a style="margin-right: 20px" href="<s:url action="choosecryo" namespace="/lot" />?item=${item.id}&lotType=cryolot"><b>New cryo lot</b> for ${item.name}</a>
		<a href="<s:url action="register" namespace="/lot" />?item=${item.id}&lotType=otherlot"><b>New other lot</b> for ${item.name}</a>
		</p>
	</iita:authorize>
	<s:set name="results" value="%{item.lots}" />
	
	<s:push value="lots">
		<s:include value="/WEB-INF/jsp/lot/tabular-list.jsp" />
	</s:push>
	
	
	<s:if test="traitLastValues!=null && traitLastValues.size>0">
		<h2>Last trial results</h2>
		<s:push value="traitLastValues">
			<s:include value="/WEB-INF/jsp/trial/trait-values-item.jsp" />
		</s:push>
	</s:if>
</s:if>
<s:else>

	<p><a href="<s:url action="item/edit" />">Add new item</a></p>

	<s:form action="item/edit" method="POST">
		<table class="inputform">
			<colgroup>
				<col width="20%" />
				<col width="30%" />
				<col width="20%" />
				<col width="30%" />
			</colgroup>
			<tr class="identifying">
				<td class="tdLabel">Item type:</td>
				<td><s:select name="item.itemType" value="%{item.itemType}" list="itemTypes" headerKey="" headerValue="-- Select item type" listKey="id" listValue="name" /></td>
			</tr>
			<tr class="identifying">
				<td class="tdLabel">Name:</td>
				<td><s:textfield name="item.name" /></td>
				<td class="tdLabel">Alternative identifier:</td>
				<td><s:textfield name="item.alternativeIdentifier" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Prefix:</td>
				<td><s:textfield name="item.prefix" /></td>
				<td class="tdLabel">Accession identifier:</td>
				<td><s:textfield name="item.accessionIdentifier" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Latin name:</td>
				<td colspan="3"><s:textfield name="item.latinName" value="%{item.latinName}" /></td>
			</tr>
			<tr>
				<td class="tdLabel">Notes:</td>
				<td colspan="3"><s:textarea name="item.notes" value="%{item.notes}" /></td>
			</tr>
			<tr>
				<td />
				<td colspan="3"><s:submit value="Update" action="item/edit!save" /> <s:submit name="redirect:%{referer}" value="Back" /></td>
			</tr>
		</table>
		<s:hidden name="type" value="%{type}" />
	</s:form>

</s:else>
</body>
</html>