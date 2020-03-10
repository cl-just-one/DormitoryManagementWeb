<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta charset="UTF-8">
	<title>住宿列表</title>
	<link rel="stylesheet" type="text/css" href="easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="easyui/css/demo.css">
	<script type="text/javascript" src="easyui/jquery.min.js"></script>
	<script type="text/javascript" src="easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="easyui/js/validateExtends.js"></script>
	<script type="text/javascript">
	$(function() {	
		//datagrid初始化 
	    $('#dataList').datagrid({ 
	        title:'住宿列表', 
	        iconCls:'icon-more',//图标 
	        border: true, 
	        collapsible:false,//是否可折叠的 
	        fit: true,//自动大小 
	        method: "post",
	        url:"LiveServlet?method=LiveList&t="+new Date().getTime(),
	        idField:'id', 
	        singleSelect:false,//是否单选 
	        pagination:true,//分页控件 
	        rownumbers:true,//行号 
	        remoteSort: false,
	        columns: [[  
				{field:'chk',checkbox: true,width:50},
 		        {field:'id',title:'ID',width:200, sortable: true},
 		       {field:'studentId',title:'学生',width:200, formatter: function(value, rowData, rowIndex) {
		        	var data = $("#search-studentId").combobox("getData");
		        	for(var i = 0; i < data.length; i++) {
		        		if(value == data[i].id) {
		        			return data[i].name;
		        		}
		        	}
		        	return value;
		        }},
 		        {field:'dormitoryId',title:'宿舍',width:200, formatter: function(value, rowData, rowIndex) {
 		        	var data = $("#search-dormitoryId").combobox("getData");
 		        	for(var i = 0; i < data.length; i++) {
 		        		if(value == data[i].id) {
 		        			return data[i].sn;
 		        		}
 		        	}
 		        	return value;
 		        }},
 		        {field:'liveDate',title:'入住时间',width:150, formatter: function(value, rowData, rowIndex) {
 		        	var date = new Date(value);
 		        	return date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate();
 		        }}
	 		]], 
	        toolbar: "#toolbar",
	        onBeforeLoad: function() {
	        	try {
	        		var data = $("#search-studentId").combobox("getValue");
	        		if (data.length == 0) {
						preBuilding();
					}
	        		data = $("#search-dormitoryId").combobox("getValue");
	        		if (data.length == 0) {
						preBuilding();
					}
				} catch (e) {
					// TODO: handle exception
					preBuilding()
				}
	        }
	    });
		// 获取楼宇
		function preBuilding() {
			// 搜索学生下拉框
		  	$("#search-studentId").combobox({
		  		url: "StudentServlet?method=StudentList&from=combox",
				onLoadSuccess: function(){
					/* var data = $(this).combobox("getData");
					$(this).combobox("setValue", data[0].name); */
				}
		  	});
			// 搜索宿舍下拉框
		  	$("#search-dormitoryId").combobox({
		  		url: "DormitoryServlet?method=DormitoryList&from=combox",
				onLoadSuccess: function(){
					/* var data = $(this).combobox("getData");
					$(this).combobox("setValue", data[0].sn); */
				}
		  	});
		}
	    //设置分页控件 
	    var p = $('#dataList').datagrid('getPager'); 
	    $(p).pagination({ 
	        pageSize: 10,//每页显示的记录条数，默认为10 
	        pageList: [10,20,30,50,100],//可以设置每页记录条数的列表 
	        beforePageText: '第',//页数文本框前显示的汉字 
	        afterPageText: '页    共 {pages} 页', 
	        displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录', 
	    }); 
	    //设置工具类按钮
	    $("#add").click(function(){
	    	$("#addDialog").dialog("open");
	    });
	    //修改
	    $("#edit").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	if(selectRows.length != 1){
            	$.messager.alert("消息提醒", "请选择一条数据进行操作!", "warning");
            } else{
		    	$("#editDialog").dialog("open");
            }
	    });
	    //删除
	    $("#delete").click(function(){
	    	var selectRows = $("#dataList").datagrid("getSelections");
        	var selectLength = selectRows.length;
        	if(selectLength == 0){
            	$.messager.alert("消息提醒", "请选择数据进行删除!", "warning");
            } else{
            	var ids = [];
            	$(selectRows).each(function(i, row){
            		ids[i] = row.id;
            	});
            	$.messager.confirm("消息提醒", "将删除与住宿相关的所有数据，确认继续？", function(r){
            		if(r){
            			$.ajax({
							type: "post",
							url: "DormitoryServlet?method=DeleteDormitory",
							data: {ids: ids},
							success: function(msg){
								if(msg == "success"){
									$.messager.alert("消息提醒","删除成功!","info");
									//刷新表格
									$("#dataList").datagrid("reload");
									$("#dataList").datagrid("uncheckAll");
								} else{
									$.messager.alert("消息提醒","删除失败!","warning");
									return;
								}
							}
						});
            		}
            	});
            }
	    });
	    
	  	//设置添加住宿窗口
	    $("#addDialog").dialog({
	    	title: "添加住宿",
	    	width: 300,
	    	height: 200,
	    	iconCls: "icon-add",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'添加',
					plain: true,
					iconCls:'icon-user_add',
					handler:function(){
						var validate = $("#addForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							$.ajax({
								type: "post",
								url: "LiveServlet?method=AddLive",
								data: $("#addForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","添加成功!","info");
										//关闭窗口
										$("#addDialog").dialog("close");
										//清空原表格数据
										/* $("#add_floor").textbox('setValue', "");
										$("#add_dormitoryId").textbox('setValue', "");
										$("#add_maxNumber").textbox('setValue', ""); */
										
										//重新刷新页面数据
							  			$('#dataList').datagrid("reload");
										
									} else{
										$.messager.alert("消息提醒",msg,"warning");
										return;
									}
								}
							});
						}
					}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						$("#add_studentId").textbox('setValue', "");
						$("#add_dormitoryId").textbox('setValue', "");
					}
				},
			]
	    });
	  	
	  	//设置编辑住宿窗口
	    $("#editDialog").dialog({
	    	title: "修改住宿信息",
	    	width: 300,
	    	height: 200,
	    	iconCls: "icon-edit",
	    	modal: true,
	    	collapsible: false,
	    	minimizable: false,
	    	maximizable: false,
	    	draggable: true,
	    	closed: true,
	    	buttons: [
	    		{
					text:'提交',
					plain: true,
					iconCls:'icon-user_edit',
					handler:function(){
						var validate = $("#editForm").form("validate");
						if(!validate){
							$.messager.alert("消息提醒","请检查你输入的数据!","warning");
							return;
						} else{
							$.ajax({
								type: "post",
								url: "LiveServlet?method=EditLive&t="+new Date().getTime(),
								data: $("#editForm").serialize(),
								success: function(msg){
									if(msg == "success"){
										$.messager.alert("消息提醒","更新成功!","info");
										//关闭窗口
										$("#editDialog").dialog("close");
										//刷新表格
										$("#dataList").datagrid("reload");
										$("#dataList").datagrid("uncheckAll");
							  			
									} else{
										$.messager.alert("消息提醒","更新失败!","warning");
										return;
									}
								}
							});
						}
					}
				},
				{
					text:'重置',
					plain: true,
					iconCls:'icon-reload',
					handler:function(){
						//清空表单
						$("#edit_studentId").textbox('setValue', "");
						$("#edit_dormitoryId").textbox('setValue', "");
					}
				}
			],
			onBeforeOpen: function(){
				var selectRow = $("#dataList").datagrid("getSelected");
				//设置值
				$("#edit_id").val(selectRow.id);
				$("#edit_old_dormitoryId").val(selectRow.dormitoryId);
				
				/* var studentList = $("#edit_studentId").combobox("getData");
				for(var i = 0; i < studentList.length - 1; i++) {
					if(studentList[i].id == selectRow.studentId){
						$("#edit_studentId").textbox('setValue', studentList[i].name);
					}
				} */
				$("#edit_studentId").textbox('setValue', selectRow.studentId);
				
				/* var dormitoryList = $("#edit_dormitoryId").combobox("getData");
				for(var j = 0; j < dormitoryList.length - 1; j++) {
					if(dormitoryList[j].id == selectRow.dormitoryId){
						$("#edit_dormitoryId").textbox('setValue', dormitoryList[j].sn);
					}
				} */
				$("#edit_dormitoryId").textbox('setValue', selectRow.dormitoryId);
			}
	    });
	  	
	  	// 搜索按钮监听
	  	$("#search").click(function() {
	  		$('#dataList').datagrid("load", {
	  			studentId: $("#search-studentId").combobox("getValue"),
	  			dormitoryId: $("#search-dormitoryId").combobox("getValue")
	  		});
	  	});
	  	
	  	//学生下拉框通用属性
	  	$("#search-studentId, #add_studentId, #edit_studentId").combobox({
	  		width: "150",
	  		height: "30",
	  		valueField: "id",
	  		textField: "name",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});
	 	//宿舍下拉框通用属性
	  	$("#search-dormitoryId, #add_dormitoryId, #edit_dormitoryId").combobox({
	  		width: "150",
	  		height: "30",
	  		valueField: "id",
	  		textField: "sn",
	  		multiple: false, //可多选
	  		editable: false, //不可编辑
	  		method: "post",
	  	});
	  	
	 	// 添加弹框 学生下拉框
	  	$("#add_studentId, #edit_studentId").combobox({
	  		url: "StudentServlet?method=StudentList&from=combox",
			onLoadSuccess: function(){
				//默认选择第一条数据
				var data = $(this).combobox("getData");
				$(this).combobox("setValue", data[0].id);
	  		}
	  	});
	 	// 添加弹框 宿舍下拉框
	  	$("#add_dormitoryId, #edit_dormitoryId").combobox({
	  		url: "DormitoryServlet?method=DormitoryList&from=combox",
			onLoadSuccess: function(){
				//默认选择第一条数据
				var data = $(this).combobox("getData");
				$(this).combobox("setValue", data[0].sn);
	  		}
	  	});
	   
	});
	</script>
</head>
<body>
	<!-- 住宿列表 -->
	<table id="dataList" cellspacing="0" cellpadding="0"> 
	    
	</table> 
	<!-- 工具栏 -->
	<div id="toolbar">
		<c:if test="${userType != 3}">
		<div style="float: left;"><a id="add" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">入住</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		</c:if>
		<div style="float: left;"><a id="edit" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">调整</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		<c:if test="${userType != 3}">
		<div style="float: left;"><a id="delete" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-some-delete',plain:true">退宿</a></div>
			<div style="float: left;" class="datagrid-btn-separator"></div>
		</c:if>
		<div style="float: left; margin: 0 10px 0 10px">学生：<input id="search-studentId" class="easyui-combobox" name="studentId" /></div>
		<div style="float: left; margin: 0 10px 0 10px">宿舍：<input id="search-dormitoryId" class="easyui-combobox" name="dormitoryId" /></div>
		<div><a id="search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索</a></div>
	
	</div>
	
	<!-- 添加住宿窗口 -->
	<div id="addDialog" style="padding: 10px">  
		<!-- <div style="float: right; margin: 20px 20px 0 0; width: 200px; border: 1px solid #EBF3FF" id="photo">
	    	<img alt="照片" style="max-width: 200px; max-height: 400px;" title="照片" src="photo/student.jpg" />
	    </div>  -->
    	<form id="addForm" method="post">
    		<table>
	    		<tr>
	    			<td>学生:</td>
	    			<td><select id="add_studentId" class="easyui-combobox" data-options="editable: false, panelHeight: 'auto'" name="studentId"></select></td>
	    		</tr>
	    		<tr>
	    			<td>宿舍:</td>
	    			<td><select id="add_dormitoryId" class="easyui-combobox" data-options="editable: false, panelHeight: 'auto'" name="dormitoryId"></select></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
	<!-- 修改住宿窗口 -->
	<div id="editDialog" style="padding: 10px">
    	<form id="editForm" method="post">
			<input type="hidden" id="edit_id" name="id" />
			<input type="hidden" id="edit_old_dormitoryId" name="oldDormitoryId" />
	    	<table cellpadding="8" >
	    		<tr>
	    			<td>学生:</td>
	    			<td><select id="edit_studentId" class="easyui-combobox" data-options="editable: false, panelHeight: 'auto'" name="studentId"></select></td>
	    		</tr>
	    		<tr>
	    			<td>宿舍:</td>
	    			<td><select id="edit_dormitoryId" class="easyui-combobox" data-options="editable: false, panelHeight: 'auto'" name="dormitoryId"></select></td>
	    		</tr>
	    	</table>
	    </form>
	</div>
	
</body>
</html>