/*
	Author : D.Vivekanandhan
*/
$(function () {
	console.log(startDate+"---"+endDate);
	
	$('.date').datepicker({
		format: 'dd-mm-yyyy',
		date: Date,
		autoclose : true,
	});
	$('.date').datepicker('setDate',new Date);
	
	$('#frame').attr("src","homedashboard.do");
	var start = sessionStorage.getItem("start");
    var end = sessionStorage.getItem("end");
    if( start==null)
    {
    	start = moment().subtract(29, 'days');
    	end = moment();
    	sessionStorage.setItem("Mstart",start.format('YYYY-MM-DD'));
    	sessionStorage.setItem("Mend",end.format('YYYY-MM-DD'));
    }
    else
    {
    	start = moment(start);
    	end = moment(end);
    }
    
    function cb(start, end) {
        $('#daterange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
        sessionStorage.setItem("startDate",start.format('YYYY-MM-DD'));
    	   sessionStorage.setItem("endDate",end.format('YYYY-MM-DD'));
    	//window.parent.hideloading();
    }

    $('#daterange').daterangepicker({
        startDate: start,
        endDate: end,
        opens: "left",
        ranges: {
           'Today': [moment(), moment()],
           'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
           'Last 7 Days': [moment().subtract(6, 'days'), moment()],
           'Last 30 Days': [moment().subtract(29, 'days'), moment()],
           'This Month': [moment().startOf('month'), moment().endOf('month')],
           'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }
    }, cb);
    cb(start, end);
    $('#daterange').on('apply.daterangepicker', function(ev, picker) {
  	sessionStorage.setItem("start",picker.startDate);
    	sessionStorage.setItem("end",picker.endDate);
    	sessionStorage.setItem("startDate",picker.startDate.format('YYYY-MM-DD'));
    	sessionStorage.setItem("endDate",picker.endDate.format('YYYY-MM-DD'));
    	$('#frame').attr("src", $('#frame').attr("src"));
    });
    
	user = sessionStorage.getItem("user");
	$("#User_Name").html(user);
	//$("#frame").attr("src", "homedashboard.do");

	$.post(
     {
     	url: "debtorEntity.do", 
     	success:function(result)
     	{
     		
     		debtorEntity = eval(result);
			entityData.push.apply(entityData,debtorEntity);
     		console.log(debtorEntity);
     	}
     });

	$.post(
     {
     	url: "creditorEntity.do", 
     	success:function(result)
     	{
     		creditorEntity = eval(result);
			entityData.push.apply(entityData,creditorEntity);
     		entityData.push("Add New..");
     		console.log(creditorEntity);
     		initList();
     	}
     });


	function initList()
	{
		debtorEntity.push("Add New..");
		creditorEntity.push("Add New..");
		$( "#exCredit" ).autocomplete({
			source: debtorEntity,
			autoFocus: true,
		});
		$( "#exDebit" ).autocomplete({
			source: creditorEntity,
			autoFocus: true,
		});
		$( "#inCredit" ).autocomplete({
			source: creditorEntity,
			autoFocus: true,
		});
		$( "#inDebit" ).autocomplete({
			source: debtorEntity,
			autoFocus: true,
		});
		$( "#exCredit" ).autocomplete( "option", "appendTo", ".erow" );
		$( "#exDebit" ).autocomplete( "option", "appendTo", ".erow" );
		$( "#inCredit" ).autocomplete( "option", "appendTo", ".inrow" );
		$( "#inDebit" ).autocomplete( "option", "appendTo", ".inrow" );
	}
	
	
	$("#frame").on('load',function()
	{
		/*setTimeout(function()
		{
			var ent = $("#frame").focus().contents().find('#entity')
			ent.focus();
			ent.autocomplete({
				source: entityData,
				autoFocus: true,
				position: { my : "top-100%", at: "top", collision: "flip" },
			});
		}, 1000);*/
	});

	$("#homeButton").click(function(event)
	{
		$('#frame').attr("src","homedashboard.do");
		$(".active").removeClass("active");
		$("#homeButton").addClass("active");
		//$("#").addClass("active");
	});

	$("#dayButton").click(function(event)
	{
		$('#frame').attr("src","daybook.do");
		$(".active").removeClass("active");
		$("#dayButton").addClass("active");
	});

	$("#ledgerButton").click(function(event)
	{
		$('#frame').attr("src","ledger.do");
		$(".active").removeClass("active");
		$("#ledgerButton").addClass("active");
	});

	$("#expenceButton").click(function(event)
	{
		$("#expences_modal").modal('show');
	});

	$("#incomeButton").click(function(event)
	{
		$("#income_modal").modal('show');
	});

	$("#settings").click(function(event)
	{
		$("#settings_modal").modal('show');
	});
	
});

function logout()
{
	$.post(
        	{
        		url: "logout.do", 
        		success:function(result) {
      				window.location = "#";
      				window.location.reload();
    			}
        	}
        	);
}

