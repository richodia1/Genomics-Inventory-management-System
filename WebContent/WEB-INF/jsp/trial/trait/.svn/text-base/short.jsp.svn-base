<%@ include file="/common/taglibs.jsp"%>

<div><b><s:property value="title" /> [<s:property value="var" />]</b></div>
<div><s:property value="description" /></div>
<s:if test="uom!=null"><div>UOM: <b><s:property value="uom" /></b></div></s:if>
<s:if test="coded">
<ul>
<s:iterator value="coding">
	<li><em><s:property value="value" /></em>: <s:property value="coding" /></li>
</s:iterator>
</ul>
</s:if>