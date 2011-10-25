<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page pageEncoding="UTF-8" %> 
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
		<title>模板资源上传</title>
        <link rel="stylesheet" type="text/css" href='<s:url value="/ewcmssource/easyui/themes/default/easyui.css"/>'>
        <link rel="stylesheet" type="text/css" href='<s:url value="/ewcmssource/easyui/themes/icon.css"/>'>
        <link rel="stylesheet" type="text/css" href='<s:url value="/ewcmssource/css/ewcms.css"/>'>
        <script type="text/javascript" src='<s:url value="/ewcmssource/js/jquery.min.js"/>'></script>
        <script type="text/javascript" src='<s:url value="/ewcmssource/easyui/jquery.easyui.min.js"/>'></script>          
		<script type="text/javascript">
			<s:if test="sourceVo.id != null">
				parent.importLoad();
			</s:if>
		</script>		
	</head>
	<body >
		<s:form action="import" namespace="/site/template/source" method="post" enctype="multipart/form-data" style="padding: 5px;">
			<table class="formtable" align="center">
				<tr>
					<td>资源文件：</td>
					<td class="formFieldError" width="80%">
						<s:file name="sourceFile" cssClass="inputtext" size="60"/>
						<s:fielderror><s:param value="%{'sourceFile'}" /></s:fielderror>
					</td>
				</tr>
				<tr>
					<td>资源路径：</td>
					<td>
						<s:textfield name="sourceVo.path"  readonly="true" cssClass="inputdisabled" size="50"/>
					</td>				
				</tr>
				<tr>
					<td colspan="2" style="padding:0;">
						<div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
						   <a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
						   <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:document.forms[0].reset();">重置</a>
						</div>								
					</td>
				</tr>																											
			</table>
			<s:hidden name="sourceVo.parent.id"/>				
		</s:form>
	</body>
</html>