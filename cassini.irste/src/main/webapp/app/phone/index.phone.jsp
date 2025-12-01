<%@ page import="java.util.Date" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="utf-8">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, user-scalable=no"/>
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="app/assets/images/favicon.png" type="image/png">
    <link rel='stylesheet' href='http://fonts.googleapis.com/css?family=Roboto:400,500,700,400italic'>

    <title>Cassini Systems</title>

</head>

<%
    String bootstrap = "app/app.phone.bootstrap.js?bust=" + new Date().getTime();
%>

<body>
<div id="appview" class="appview" layout="row" ui-view style="display: none; overflow: hidden"></div>

<script type="text/javascript" src="app/assets/libs/requirejs/require.js" data-main="<%=bootstrap%>"></script>
</body>

</html>