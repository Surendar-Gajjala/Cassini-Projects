<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="app/assets/images/favicon.png" type="image/png">


    <title>Cassini.TMS</title>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="app/assets/bower_components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="app/assets/bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<!-- Preloader -->
<div id="preloader">
    <div id="status"><i class="fa fa-spinner fa-spin"></i></div>
</div>

<!-- Main app view -->
<div id="appview" app-view class="appview" ui-view style="display: none;"></div>
<div id="busy-indicator" class="loading style-2" style="display: none"><div class="loading-wheel"></div></div>

<script type="text/javascript" src="app/assets/bower_components/requirejs/require.js" data-main="app/desktop/desktop.bootstrap.js"></script>
</body>

</html>