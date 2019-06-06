/*
	Author : D.Vivekanandhan
*/
var debit=0,credit=0,date,desc,entity,date;
var ids = new Map();
var startDate = sessionStorage.getItem("startDate");
var endDate = sessionStorage.getItem("endDate");
console.log (startDate+"---"+endDate);
$(function () {
	$('.date').datepicker({
		format: 'dd-mm-yyyy',
		date: Date,
		autoclose : true,
	});
	$('.date').datepicker('setDate',new Date);

	 jQuery("#grid").jqGrid(
	 { 
	 	datatype: "local", 
	 	height: 490,
	 	width: 1290,
	 	rowNum: 1000,
	 	colNames:['Date', 'Entity', 'Debit','Credit','Description'], 
	 	colModel:[ {name:'Date',index:'Date', width:90, align:"center",sorttype:"text"}, 
	 			 {name:'Entity',index:'Entity', width:100, align:"center"}, 
	 			 {name:'Debit',index:'Debit', width:80, align:"center",sorttype:"float"}, 
	 			 {name:'Credit',index:'Credit', width:80, align:"center",sorttype:"float"}, 
	 			 {name:'Description',index:'Description', width:150, align:"center", sortable:false} ], 
	 	multiselect: false
	 }); 
	jQuery("#grid").jqGrid('navGrid',{edit:false,add:false,del:false});
	jQuery("#grid").jqGrid('bindKeys', {"onEnter":function( rowid )
										{ 
											alert("You enter a row with id:"+rowid)
										}
								}
					);
	$("#grid").keydown(function(e)
	{
		var gridId = parseInt(e.target.id);
		var daybookId;
		switch (e.which)
		{
			case 46:
				daybookId = ids.get(gridId);
				console.log(daybookId);
				$.post(
				{
					url: "deleteEntry.do",
					data: {id:daybookId},
					success:function(result)
					{
						console.log(result);
						extractGridData();
					}
				});
		}
	});
	var debtorEntity = [];
	var creditorEntity = [];
	var entityData = [];
	$.post(
     {
     	url: "debtorEntity.do", 
     	success:function(result)
     	{
     		
     		debtorEntity = eval(result);
			entityData.push.apply(entityData,debtorEntity);
     		console.log(entityData);
     	}
     });

	$.post(
     {
     	url: "creditorEntity.do", 
     	success:function(result)
     	{
     		creditorEntity = eval(result);
			entityData.push.apply(entityData,creditorEntity);
     		console.log(entityData);
     		initList();
     	}
     });
     
	extractGridData(); 

	function extractGridData()
	{
		console.log (startDate+"---"+endDate);
		jQuery("#grid").jqGrid('clearGridData');
		var data={};
		$.post(
        	{
        		url: "daybookdata.do", 
			data: {from:startDate.trim(),to:endDate.trim()},
        		success: function(result)
        		{
        			data = eval(result);
        			for(var i=0;i<data.length;i++) 
	 			{
	 				jQuery("#grid").jqGrid('addRowData',i+1,data[i]);
					
	 				ids.set(i+1,data[i].Id);
	 			}
	 			console.log(ids);
	 			jQuery("#grid").trigger("reloadGrid");
        		}
        	});
	
	}

	function initList()
	{
		entityData.push("Add New..");
		$( "#entity" ).autocomplete({
     				source: entityData,
     				autoFocus: true,
     				position: { my : "top-100%", at: "top", collision: "flip" },
				});
	}

	$("#date").keyup(function(event)
	{
		var key = event.keyCode;
		if(key === 13)
			insertRow();
	});
	$("#debit").keyup(function(event)
	{
		var key = event.keyCode;
		if(key === 13)
			insertRow();
	});

	$("#credit").keyup(function(event)
	{
		var key = event.keyCode;
		if(key === 13)
			insertRow();
	});

	$("#desc").keyup(function(event)
	{
		var key = event.keyCode;
		if(key === 13)
			insertRow();
	});
	
	$("#entity").keyup(function(event)
	{
		var key = event.keyCode;
		//console.log($("#entity").val());
		if($("#entity").val()==="Add New..")
		{
			$("#entity").val("");
			entity="";
			//alert("Add New");
			$("#addNew").modal('show');
			setTimeout(function()
			{
				$('#newEntity').focus();
			}, 1000);
		}
		else if(key === 13)
		{
			entity = ($("#entity").val());
			insertRow();
		}
	});

	$("#addButton").click(function(event)
	{
		if($('#newEntity').val()=="")
			alert("Entity cannot be empty");
		else
		{
			var ent = $('#newEntity').val();
			var type = $('#type option:selected').val();
			$("#addNew").modal('hide');
			$.post(
			{
				url : "newentity.do",
				data : {entity:ent,type:type},
				success : function(result)
				{
					if(result.trim()==="Success")
					{
						$('#newEntity').val("");
						window.location.reload();
					}
					else
						alert(result);
				}
			});
		}
	});

	$("#entity").change(function()
	{
		var flag = false;
		for(i=0;i<20;i++)
			{
				 if($("#entity").val()=== debtorEntity[i])
				 {
				 	console.log(debtorEntity[i]);
				 	$("#credit").attr("disabled", "disabled");
				 	$("#debit").removeAttr("disabled"); 
				 	$("#debit").focus();
				 	flag = true;
				 }
				 else if($("#entity").val()=== creditorEntity[i])
				 {
				 	console.log(creditorEntity[i]);
				 	$("#debit").attr("disabled", "disabled");
				 	$("#credit").removeAttr("disabled"); 
				 	$("#credit").focus();
				 	flag = true;
				 }
			}
		if(!flag)
		{
			$("#entity").val("");
			entity="";
			alert("Not in the list");
			setTimeout(function()
			{
				$("#entity").focus();
			},100);
		}
		else if(flag)
		{
			entity = $("#entity").val();
		}
	});

	function insertRow()
	{
		date = $("#date").val();
		if($("#debit").val())
			debit = parseInt($("#debit").val());
		if($("#credit").val())
			credit = parseInt($("#credit").val());
		desc = $("#desc").val();
		if((date&&entity&&credit)||(date&&entity&&debit))
		{
			//alert("success");
			$.post(
        		{
        			url: "insertRow.do", 
				data: {date:date.trim(),entity:entity.trim(),debit:debit,credit:credit,description:desc},
        			success:function(result)
        			{
        				if(result.trim()==="Success")
        				{
        					clear();
        					setTimeout(function()
						{
							$("#entity").focus();
						},100);
        					extractGridData();
        				}
        			}
        		});
		}
	}

	function clear()
	{
		$("#entity").val("");
		$("#debit").val("");
		$("#credit").val("");
		$("#desc").val("");
		$("#credit").removeAttr("disabled");
		$("#debit").removeAttr("disabled");
		debit=0;
		credit=0;
	}	
});

