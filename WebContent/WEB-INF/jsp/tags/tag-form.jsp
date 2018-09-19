<%@ include file="/common/taglibs.jsp"%>
<s:form method="post" action="tag!update">
	<s:hidden name="id" value="%{id}" />
	<s:hidden name="%{top.class.simpleName.substring(0, top.class.simpleName.indexOf('Tag')).toLowerCase()}" value="%{entity.id}" />
	<table class="rawform">
		<colgroup>
			<col width="100px" />
			<col />
		</colgroup>
		<tr>
			<td>Tag:</td>
			<td><s:textfield name="tagValue" value="" /></td>
		</tr>
<%--		<tr>
			<td class="ar">Percentage:</td>
			<td><s:textfield name="tag.percentage" value="%{percentage}" cssClass="numeric-input" /></td>
		</tr>
 --%>		<tr>
			<td />
			<td><s:if test="id==null"><s:submit value="Add tag" /></s:if><s:else>
				<s:submit value="Update" />
				<s:submit value="Remove" action="tag!remove" cssClass="button-delete" onclick="javascript: return confirm('Are you sure you want to remove this record?');" />
				</s:else>				
			</td>
		</tr>
	</table>
</s:form>