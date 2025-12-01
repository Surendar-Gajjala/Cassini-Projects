define(['app/app.modules'], function(app)
{
    app.filter('rupee', function () {
	  	return function (text) {
	  		if (text) {
	        	return text.toString().replace(/(\d)(?=(\d\d)+\d$)/g, "$1,");	  			
	  		}else {
	  			return 0;
	  		};
	    };
	});
});