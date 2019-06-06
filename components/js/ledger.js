/*
	Author : D.Vivekanandhan
*/
$(function () {
	 jQuery("#Lgrid").jqGrid(
	 { 
	 	datatype: "local", 
	 	height: 490,
	 	width: 1290,
	 	rowNum: 1000,
	 	colNames:['Date', 'Ledger', 'Debit','Credit','Description'], 
	 	colModel:[ {name:'Date',index:'Date', width:90, align:"center",sorttype:"text"}, 
	 			 {name:'Entity',index:'Entity', width:100, align:"center"}, 
	 			 {name:'Debit',index:'Debit', width:80, align:"center",sorttype:"float"}, 
	 			 {name:'Credit',index:'Credit', width:80, align:"center",sorttype:"float"}, 
	 			 {name:'Description',index:'Description', width:150, align:"center", sortable:false} ], 
	 	multiselect: false
	 }); 
	jQuery("#Lgrid").jqGrid('navGrid',{edit:false,add:false,del:false});
	jQuery("#Lgrid").jqGrid('bindKeys', {"onEnter":function( rowid )
										{ 
											alert("You enter a row with id:"+rowid)
										}
								}
					);
	/*$("#Lgrid").keydown(function(e)
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
	});*/

	extractLGridData(); 

});

function extractLGridData()
{
	console.log (startDate+"---"+endDate);
	jQuery("#Lgrid").jqGrid('clearGridData');
	var data={};
	$.post(
   	{
   		url: "ledgerdata.do", 
		data: {from:startDate.trim(),to:endDate.trim()},
   		success: function(result)
   		{
   			data = eval(result);
   			for(var i=0;i<data.length;i++) 
 			{
 				jQuery("#Lgrid").jqGrid('addRowData',i+1,data[i]);
				
 				ids.set(i+1,data[i].Id);
 			}
 			console.log(ids);
 			jQuery("#Lgrid").trigger("reloadGrid");
   		}
   	});

}
