<%@ include file="/common/taglibs.jsp"%>

<span class="tag quick"> <s:property value="tag" /> 
	<a href="<s:url action="tag/remove" />?tagId=<s:property value="id" /><s:push value="profile"><s:if test="top instanceof org.iita.trainingunit.model.Trainee && id!=null">&amp;traineeId=<s:property value="id" /></s:if><s:elseif test="top instanceof org.iita.trainingunit.model.TrainingProgram && id!=null">&amp;programId=<s:property value="id" /></s:elseif></s:push>"
	onclick="javascript: return confirm('Are you sure you want to remove this tag?');">[ X ]</a></span>