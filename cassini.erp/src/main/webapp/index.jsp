<%@ page import="net.sf.uadetector.ReadableUserAgent" %>
<%@ page import="net.sf.uadetector.UserAgentStringParser" %>
<%@ page import="net.sf.uadetector.service.UADetectorServiceFactory" %>
<%@ page import="net.sf.uadetector.ReadableDeviceCategory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
    ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
%>

<%
    if(agent.getDeviceCategory().getCategory() == ReadableDeviceCategory.Category.SMARTPHONE) {
%>
        <%@ include file="index.phone.jsp" %>
<%  }
    else if(agent.getDeviceCategory().getCategory() == ReadableDeviceCategory.Category.TABLET) {
%>
        <%@ include file="index.phone.jsp" %>
<%  }
    else  {
%>
        <%@ include file="index.web.jsp" %>
<%  }
%>


