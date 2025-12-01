<%@ page import="net.sf.uadetector.ReadableDeviceCategory" %>
<%@ page import="net.sf.uadetector.ReadableUserAgent" %>
<%@ page import="net.sf.uadetector.UserAgentStringParser" %>
<%@ page import="net.sf.uadetector.service.UADetectorServiceFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    UserAgentStringParser parser = UADetectorServiceFactory.getResourceModuleParser();
    ReadableUserAgent agent = parser.parse(request.getHeader("User-Agent"));
%>

<%@ include file="app/desktop/index.desktop.jsp" %>
