define(['app/desktop/desktop.root', 'app/shared/constants/constants'], function(module)
{
    module.factory('validation',
    [
        'CONSTANTS',
        function(CONSTANTS)
        {
            return {
                required: function(value) {
                    var result = {isValid: false, message: ""};
                	
                    if(value !== ""){
                    	result.isValid = true;                    	
                    }else {
                    	result.message = CONSTANTS.REQUIRED
                    }
                    
                    return result;
                },
                
                email: function(value) {
                	var regExp = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i,
                		result = {isValid: false, message: ""};
                	
                    if(value !== ""){
                    	if(regExp.test(email)){
                    		result.isValid = true;
                    	}else {
                        	result.message = CONSTANTS.INVALIDEMAIL;
                        }                    	
                    }else {
                    	result.message = CONSTANTS.REQUIRED
                    }
                    
                    return result;
                },
                
                comparePassword: function(pwd, confirmPwd) {
                	var result = {isValid: false, message: ""};
                	
                	if(pwd !== "" && confirmPwd !== "") {
                		if(pwd === confirmPwd){
                			result.isValid = true;
                		}else {
                        	result.message = CONSTANTS.PASSWORDMISMATCH
                        }
                	}else {
                    	result.message = CONSTANTS.REQUIRED
                    }
                	
                	return result;
                },
                
                mobile: function(value) {
                	var regExp = /^[1-9]{1}[0-9]{9}$/,
                		result = {isValid: false, message: ""};
                    
                    if(value !== "") {
                    	if(regExp.test(value)) {
                    		result.isValid = true;
                        }else {
                        	result.message = CONSTANTS.INVALIDMOBILE;
                        }
                    }else {
                    	result.message = CONSTANTS.REQUIRED
                    }
                    
                    return result;
                },
                
                zipCode: function(value) {
                	var regExp = /(^\d{6}$)/,
                		result = {isValid: false, message: ""};
                	
                	if(value !== "") {
                		if(value.test(MyZipCode))
                        {
                			result.isValid = true;
                        }else {
                        	result.message = CONSTANTS.INVALIDZIP
                        }
                	}else {
                    	result.message = CONSTANTS.REQUIRED
                    }
                	
                    return result;
                }
            }
        }
    ]);
});