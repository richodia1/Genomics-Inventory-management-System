<%@ include file="/common/taglibs.jsp"%>

<s:if test="lot!=null">
	<s:set name="title" value="%{'Lot: ' + lot.item.name  + (lot.location ? ' in ' + lot.location.pathNames : '')}" />
</s:if>
<s:else>
	<s:set name="title" value="%{'Scan barcode'}" />
</s:else>

<html>
<head>
<title><s:property value="#title" /></title>
</head>
<body>
<table class="inputform" id="lotprofile_editor">
	<colgroup>
		<col width="200" />
		<col />
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td>Item:</td>
		<td colspan="3"><big><s:property value="lot.item.name" /></big> View <a href="<c:url value="/item/${lot.item.itemType.shortName}/${lot.item.name}" />">all
		lot information for <b><s:property value="lot.item.name" /></b></a>.</td>
	</tr>
	<s:if test="lot.item.notes!=null && lot.item.notes.length()>0">
		<tr>
			<td>Item Notes:</td>
			<td colspan="3"><s:property value="lot.item.notes" /></td>
		</tr>
	</s:if>
	<tr>
		<td>Barcode:</td>
		<td colspan="3"><s:if test="lot.barCode==null">
			<s:form action="update!assignBarcode" namespace="/lot" method="post">
				<s:hidden name="referer" value="%{referer}" />
				<s:hidden name="id" value="%{lot.id}" />
				<s:submit value="Assign barcode" />
			</s:form>
		</s:if><s:else>
			<s:property value="lot.barCode.id" />
		</s:else></td>
	</tr>
	<s:if test="lot.container!=null">
		<tr>
			<td>Container:</td>
			<td colspan="3"><s:property value="lot.container.name" /></td>
		</tr>
	</s:if>
	<tr>
		<td>Current quantity:</td>
		<td><b><s:property value="lot.quantity" /> <s:property value="lot.scale" /></b></td>
		<s:if test="lot.initialQuantity!=null">
			<td class="label">Initial quantity:</td>
			<td><s:property value="lot.initialQuantity" /> <s:property value="lot.initialScale" /></td>
		</s:if>
	</tr>
	<s:if test="lot.location!=null">
		<tr>
			<td>Total quantity:</td>
			<td colspan="3"><b><s:property value="totalLocationQuantity" /> <s:property value="lot.scale" /></b> in the same location</td>
		</tr>
	</s:if>
	<s:if test="lot.line!=null">
	<tr>
		<td><label>Line:</label></td>
		<td><s:property value="lot.line" /></td>
	</tr>
	</s:if>
	<%-- Specific profiles --%>
	<s:if test="lot instanceof org.iita.inventory.model.SeedLot">
		<%@ include file="profile-seed.jsp"%>
	</s:if>
	<s:elseif test="lot instanceof org.iita.inventory.model.InVitroLot">
		<%@ include file="profile-invitro.jsp"%>
	</s:elseif>
	<s:elseif test="lot instanceof org.iita.inventory.model.FieldLot">
		<%@ include file="profile-field.jsp"%>
	</s:elseif>
	<s:elseif test="lot instanceof org.iita.inventory.model.OtherLot">
		
	</s:elseif>
	<s:else>

	</s:else>


	<tr>
		<td>Location:</td>
		<td colspan="3"><s:if test="lot.location!=null">
			<a href="<c:url value="/browse/location/${lot.location.id}" />"><s:property value="lot.location.pathNames" /></a>
		</s:if> <s:else>
			<span class="important">Location not assigned, use <a href="<c:url value="/update/migrate/" />">lot migration</a> to assign lot to a location.</span>
		</s:else></td>
	</tr>
	<s:if test="lot.locationDetail!=null">
		<tr>
			<td>Location detail:</td>
			<td colspan="3"><s:property value="lot.locationDetail" /></td>
		</tr>
	</s:if>
	<s:if test="lot.parent1!=null">
		<tr>
			<td>Parent:</td>
			<td colspan="3"><s:if test="lot.parent1">
				<s:property value="%{getText('lot.parentType.' + lot.parent1type)}" />
				<a href="<c:url value="/lot/" /><s:property value="lot.parent1.id" />"><s:property value="lot.parent1.item.name" />, Lot <s:property
					value="lot.parent1.id" /></a>
			</s:if></td>
		</tr>
	</s:if>
	<s:if test="lot.children1!=null && lot.children1.size>0">
		<tr>
			<td>Children:</td>
			<td colspan="3"><s:iterator value="lot.children1">
				<a href="<c:url value="/lot/" /><s:property value="id" />">Lot <s:property value="id" /></a>
			</s:iterator></td>
		</tr>
	</s:if>
	<s:if test="lot.notes!=null && lot.notes.length()>0">
		<tr>
			<td>Lot Notes:</td>
			<td colspan="3"><s:property value="lot.notes" /></td>
		</tr>
	</s:if>
	<tr>
		<td>Last updated:</td>
		<td><s:date name="lot.lastUpdated" format="dd/MM/yyyy HH:mm" /> by <s:property value="lot.lastUpdatedBy" /></td>
		<td class="label">Created:</td>
		<td><s:date name="lot.createdDate" format="dd/MM/yyyy HH:mm" /> by <s:property value="lot.createdBy" /></td>
	</tr>
</table>
<s:if test="!getUser().hasRole('ROLE_BREEDER')">
	<iita:inlineeditor targetId="lotprofile_editor" id="lotprofile_form">
		<%@ include file="edit-form.jsp" %>
	</iita:inlineeditor>
</s:if>
<s:if test="lot.transactions.size>0">
	<h2>Transactions</h2>
	<%@ include file="transactions.jsp"%>
</s:if>

<s:if test="traitLastValues!=null && traitLastValues.size>0">
	<h2>Last trial results</h2>
	<s:push value="traitLastValues">
		<s:include value="/WEB-INF/jsp/trial/trait-values.jsp" />
	</s:push>
</s:if>

<s:if test="lot.migrations.size>0">
	<h2>Migrations</h2>
	<%@ include file="migrations.jsp"%>
</s:if>

<s:if test="!getUser().hasRole('ROLE_BREEDER')">
	<table>
	<tr><td><h2>Lot Variable</h2></td><td><h2>Field Variables</h2></td></tr>
	<tr><td><s:form action="updateLotVariable" namespace="/lot" method="post">
		<s:hidden name="referer" value="%{referer}" />
		<s:hidden name="id" value="%{lot.id}" />
		<s:hidden name="lotVarid" value="%{lotVariable.id}" />
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col />
			</colgroup>
			<tr>
				<td class="label">Variable:</td>
				<td><s:select name="varid" value="%{lotVariable.variable.id}" list="%{variables}" listKey="id" listValue="%{name}" headerKey="" headerValue="[Not specified]" /></td>
			</tr>
			<tr>
				<td>Quantity:</td>
				<td><s:textfield name="variableQuantity" value="%{lotVariable.quantity}" cssClass="numeric-input" /></td>
			</tr>
			<tr>
				<td>Date processed:</td>
				<td><iita:datepicker format="dd/MM/yyyy" cssClass="date-time" name="variableDate" value="%{lotVariable.variabledate}" required="true" /> <span class="hint">[DD/MM/YYYY]</span></td>
			</tr>
			<tr>
				<td />
				<td><s:submit value="Update" /></td>
			</tr>
		</table>
	</s:form>
	
	<s:if test="lotVariables.size>0">
		<h2>Lot variables list</h2>
		<%@ include file="lotvariables.jsp"%>
	</s:if></td><td>
	<s:form action="updateFieldVariable" namespace="/lot" method="post">
		<s:hidden name="referer" value="%{referer}" />
		<s:hidden name="id" value="%{lot.id}" />
		<table class="inputform">
			<colgroup>
				<col width="200" />
				<col width="100" />
				<col />
			</colgroup>
			<tr>
				<td>VAR:</td>
				<td>VALUE:</td>
				<td>DATE:<span class="hint"><font size="1">[dd/mm/yyyy]</font></span></td>
			</tr>
				<s:set name="lIndex" value="0" />
				<s:iterator value="lot.fieldVariables" status="status">
					<s:set name="varIndex" value="#status.index" />
					<s:set name="lIndex" value="#status.index+1" />
					<tr>
						<td><s:hidden name="lot.fieldVariables[%{varIndex}].version" value="%{version}" />
						<s:hidden name="lot.fieldVariables[%{varIndex}].var" value="%{var}" cssStyle="width: 100%" />
						<s:property value="%{var}" /></td>
						<td><s:textfield name="lot.fieldVariables[%{varIndex}].qty" value="%{qty}" cssClass="numeric-input" /></td>
						<td><iita:datepicker format="dd/MM/yyyy" cssStyle="width:100px" cssClass="date-time" name="lot.fieldVariables[%{varIndex}].date" value="%{date}" required="true" /></td>
					</tr>
				</s:iterator>
				<%-- <c:set var="myFieldVarSize" scope="request" value="${fn:length(lot.fieldVariables)}" />
				<c:forEach var="i" begin="${myFieldVarSize}" end="4"> --%>
					<s:iterator value="fieldVariablesList" status="status">
						<s:set name="iIndex" value="#status.index" />
						<s:set name="found" value="%{'false'}" />
						<s:iterator value="lot.fieldVariables" status="status">
							<s:if test="fieldVariablesList.get(#iIndex).var.toString()==lot.fieldVariables[#status.index].var.toString()">
								<s:set name="found" value="%{'true'}" />
							</s:if>
						</s:iterator>
						<%--<c:if test="${found=='false'}"> ${i==lIndex} --%>
						<s:if test="%{#found=='false'}">
	                 		<tr>
								<td>
									<input type="hidden" name="lot.fieldVariables[${lIndex}].version" value="" /><%-- name="lot.fieldVariables[${i}].version" --%>
									<input type="hidden" name="lot.fieldVariables[${lIndex}].var" value="<s:property value="%{fieldVariablesList.get(#status.index).var}" />" style="width: 100%" />
									<s:property value="%{fieldVariablesList.get(#status.index).var}" />
								</td>
								<td>
									<input type="text" name="lot.fieldVariables[${lIndex}].qty" value="" class="numeric-input" />
								</td>
								<td>
									<iita:datepicker format="dd/MM/yyyy" cssStyle="width:100px" cssClass="date-time" name="lot.fieldVariables[%{lIndex}].date" value="" required="true" />
								</td>
							</tr>
							<s:set name="lIndex" value="%{#lIndex+1}" />
						</s:if>
						<%--</c:if> --%>
					</s:iterator>
	            <%-- </c:forEach>   --%>
				
			<tr>
				<td />
				<td />
				<td><s:submit value="Update" /></td>
			</tr>
		</table>
	</s:form>
	</td></tr>
	</table>
</s:if>


<%--
<s:if test="lotVariablesByBarCode.size>0">
	<h2>Lot variables list</h2>
	<%@ include file="lotvariablesByBarCode.jsp"%>
</s:if>

<s:if test="lot.viabilityResults.size>0">
	<h2>Viability testing (legacy data)</h2>
	<s:include value="viability.jsp" />
</s:if>
--%>

</body>
</html>