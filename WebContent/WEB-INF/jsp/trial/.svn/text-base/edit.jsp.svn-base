<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<title>Edit trial data: <s:property value="trial.name" /></title>
</head>
<body iita:help="inventory/trials">
<table class="inputform" id="lotprofile_editor">
	<colgroup>
		<col width="200" />
		<col />
		<col width="200" />
		<col />
	</colgroup>
	<tr>
		<td>Trial:</td>
		<td colspan="3"><big><s:property value="trial.name" /></big></td>
	</tr>
</table>

<h2>Trial results</h2>

<s:if test="paged!=null">
	<s:push value="paged">
		<s:include value="/WEB-INF/jsp/paging.jsp">
			<s:param name="href" value="%{'id=' + trial.id}" />
	</s:include>
	</s:push>
</s:if>

<s:form method="post" action="trial/edit!update">
<s:hidden name="id" value="%{trial.id}" />
<table class="data-listing">
	<s:iterator value="paged.results" status="rowIndex">
		<s:if test="#rowIndex.index==0">
			<thead>
				<tr>
					<s:iterator status="columnIndex">
						<td><s:if test="#columnIndex.index==0">

						</s:if><s:else>
							<s:hidden name="data[%{#rowIndex.index}][%{#columnIndex.index}]" value="%{id}" /> 
							<big><s:property value="title" /></big>
							<s:if test="description!=null">
								<br />
								<s:property value="description" />
							</s:if>
						</s:else></td>
					</s:iterator>
				</tr>
			</thead>
		</s:if>
		<s:else>
			<tr>
				<s:iterator status="columnIndex">
					<s:if test="#columnIndex.index==0">
						<td class="identifying"><a href="<s:url action="index" namespace="/lot" />?id=<s:property value="id" />"><s:property value="item.name" /></a>
						<s:hidden name="data[%{#rowIndex.index}][%{#columnIndex.index}]" value="%{id}" /> 
						</td>
					</s:if>
					<s:else>
						<td>
							<s:if test="paged.results[0][#columnIndex.index].coded">
								<s:select name="data[%{#rowIndex.index}][%{#columnIndex.index}]" value="%{top}" list="paged.results[0][#columnIndex.index].coding" listKey="value" listValue="coding" headerKey="" headerValue="Not measured" />							
							</s:if>
							<s:else>
								<s:textfield name="data[%{#rowIndex.index}][%{#columnIndex.index}]" value="%{top}" cssClass="numeric-input" /> <s:property value="paged.results[0][#columnIndex.index].uom" />
							</s:else>
						</td>
					</s:else>
				</s:iterator>
			</tr>
		</s:else>
	</s:iterator>
</table>
<s:submit value="Submit data" />
</s:form>
</body>
</html>
