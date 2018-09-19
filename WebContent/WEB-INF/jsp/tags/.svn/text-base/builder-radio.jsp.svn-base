<%@ include file="/common/taglibs.jsp"%>

<s:iterator value="categories" status="status">
<h3 class="clearfloat"><s:property /> tags</h3>
<ul class="taglist" id="taglist<s:property value="#status.index" />">
<s:iterator value="[1].getTagsForCategory(top)">
	<s:set name="myTag" value="[2].findTag(top)" />
	<s:if test="#myTag!=null">
	<li class="selected-tag"><input type="radio" name="usedTag" value="${top}" checked="checked" /><%-- <s:textfield name="tagValue['%{top}']" value="%{#myTag.percentage}" cssStyle="width: 50px;" />%--%> <span style="margin-left: 20px;"><s:property /></span></li>
	</s:if>
	<s:else>
	<li><input type="radio" name="usedTag" value="${top}" /><%-- <s:textfield name="tagValue['%{top}']" cssStyle="width: 50px;" />%--%> <span style="margin-left: 20px;"><s:property /></span></li>
	</s:else>
</s:iterator>
</ul>
</s:iterator>

<style type="text/css">
li.selected-tag {
	font-weight: bold;
	background-color: #FFF3CF; 
}
</style>
