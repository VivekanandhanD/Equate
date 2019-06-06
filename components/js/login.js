/*
	Author : D.Vivekanandhan
*/
function logi()
{
	var parameters={};
	this.username=$('#username').val();
	this.password=$('#password').val();
	if((this.username=="")||(this.password==""))
	{
		document.getElementById('usg').classList.add('has-error');
    		document.getElementById('psg').classList.add('has-error');
    	}
    	else
    	{
        	$.post(
        	{
        		url: "login.do", 
        		data:{uname:username.trim(),pwd:password.trim()},
        		success:function(result) {
      				/*if(result.trim()==="Success")
      				{
      					document.getElementById('usg').className = 'input-group has-success';
    						document.getElementById('psg').className = 'input-group has-success';
    						window.location.reload();
    					}*/
    					if(result.trim()==="Failed")
    					{
    						document.getElementById('usg').classList.add('has-error');
    						document.getElementById('psg').classList.add('has-error');
    					}
    					else
    					{
    						sessionStorage.setItem("user",result);
    						document.getElementById('usg').classList.remove('has-error');
    						document.getElementById('psg').classList.remove('has-error');
    						document.getElementById('usg').className = 'input-group has-success';
    						document.getElementById('psg').className = 'input-group has-success';
    						window.location.reload();
    					}
    			}
        	}
        	);
    	}
}

