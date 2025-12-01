<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="app/assets/images/favicon-circle.png" type="image/png">
    <link rel="stylesheet" href="app/assets/fonts/line-awesome/css/line-awesome.min.css">
    <link rel="stylesheet" href="app/assets/css/app/desktop/slide-show.css">
    <link rel="stylesheet" href="app/assets/bower_components/pdfjs-dist/web/pdf_viewer.css">

    <title>Cassini | Cloud PLM Platform</title>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="app/assets/bower_components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="app/assets/bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->


    <link rel="stylesheet" href="app/assets/js/gantt/dhtmlxgantt.css?v=6.1.4">
    <script src="app/assets/js/apexcharts/apexcharts.js"></script>
    <script src="app/assets/js/gantt/dhtmlxgantt.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/api.js"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_undo.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_auto_scheduling.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_keyboard_navigation.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_tooltip.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_smart_rendering.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_multiselect.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_marker.js?v=6.1.4"></script>
    <script src="app/assets/js/gantt/ext/dhtmlxgantt_fullscreen.js?v=6.1.4"></script>
    <script src="app/assets/js/themes.js"></script>
    <script src="app/assets/bower_components/pdfjs-dist/build/pdf.js"></script>
    <script src="app/assets/bower_components/pdfjs-dist/web/pdf_viewer.js"></script>
    <script src="app/assets/js/echarts.min.js"></script>
  <%--  <script src="https://fastly.jsdelivr.net/npm/echarts@5.3.3/dist/echarts.min.js"></script>--%>
</head>

<script>
    window.sessionId = "<%=session.getId()%>";
</script>

<body>
<!-- Main app view -->
<div id="appview" app-view class="app-view disable-animations" ui-view style="display: none;"></div>
<div id="busy-indicator" class="loading style-2" style="display: none">
    <div class="loading-wheel"></div>
</div>

<script type="text/javascript" src="app/assets/bower_components/requirejs/require.js"
        data-main="app/desktop/desktop.bootstrap.js"></script>
<script type="text/javascript">
    document.body.setAttribute('spellcheck', false);
</script>
</body>

</html>