<%@ include file="/common/taglibs.jsp"%>
<s:set name="myTag" value="[1].findTag(top)" />
<s:if test="#myTag!=null">
<li class="selected-tag"><input type="checkbox" name="usedTag" value="${top}" checked="checked" /><%-- <s:textfield name="tagValue['%{top}']" value="%{#myTag.percentage}" cssStyle="width: 50px;" />%--%> <span style="margin-left: 20px;"><s:property /></span></li>
</s:if>
<s:else>
<li><input type="checkbox" name="usedTag" value="${top}" /><%-- <s:textfield name="tagValue['%{top}']" cssStyle="width: 50px;" />%--%> <span style="margin-left: 20px;"><s:property /></span></li>
</s:else>