<%@ include file="/common/taglibs.jsp"%>

<div style="margin-bottom: 10px;">
<a style="margin-right: 20px;" href="<s:url action="xls-import" />">Upload</a>
<s:if test="xlsColumns!=null"><a style="margin-right: 20px;" href="<s:url action="xls-import!mapping" />">Mapping</a></s:if>
<s:if test="results!=null"><a style="margin-right: 20px;" href="<s:url action="xls-import!preview" />">Preview data</a></s:if>
</div>
