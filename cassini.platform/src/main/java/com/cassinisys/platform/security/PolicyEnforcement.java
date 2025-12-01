package com.cassinisys.platform.security;


import com.cassinisys.platform.model.core.Login;

public interface PolicyEnforcement {

	boolean checkObject(Login subject, Object object, Object action, Object environment);

	boolean checkAttribute(Login subject, Integer objectId, Object attributeDef , Object action, Object environment);

}