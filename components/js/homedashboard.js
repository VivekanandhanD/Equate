$(function ()
{
	var Mstart = sessionStorage.getItem("Mstart");
     var Mend = sessionStorage.getItem("Mend");
	var startDate = sessionStorage.getItem("startDate");
	var endDate = sessionStorage.getItem("endDate");
	
	$.post(
	{
		url:"dashboard.do",
		data:{start:startDate,end:endDate},
		success:function(result)
		{
			data = eval(result);
			console.log(data);
			$("#appa").html(data[0]);
			$("#abiba").html(data[1]);
			$("#netbalance").html(data[2]);
			$("#availbalance").html(data[3]);
			$("#expenses").html(data[4]);
		}
	});
	var data;
	$.post(
	{
		url:"dashboardChart.do",
		data:{from:startDate,to:endDate},
		success:function(result)
		{
			data = eval("(function(){return " + result + ";})()");
			console.log(data);
			chart(data);
		}
	});
});

function chart(data)
{
var chart = AmCharts.makeChart( "bar", {
  "type": "serial",
  "theme": "light",
  "dataProvider": data,
  "columnWidth": 0.6,
  "valueAxes": [ {
    "gridColor": "#FFFFFF",
    "gridAlpha": 0.2,
    "dashLength": 0
  } ],
  "gridAboveGraphs": true,
  "startDuration": 1,
  "graphs": [ {
    "balloonText": "[[category]]: <b>[[value]]</b>",
    "fillAlphas": 0.8,
    "lineAlpha": 0.2,
    "type": "column",
    "fillColors": "#8775a7",
    "valueField": "amount"
  } ],
  "chartCursor": {
    "categoryBalloonEnabled": false,
    "cursorAlpha": 0,
    "zoomable": false
  },
  "categoryField": "entity",
  "categoryAxis": {
    "gridPosition": "start",
    "gridAlpha": 0,
    "tickPosition": "start",
    "tickLength": 20
  },
  "export": {
    "enabled": true
  }

} );
}
