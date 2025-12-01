<!DOCTYPE html>
<html lang="en">
<head>
    <meta content="text/html;charset=utf-8" http-equiv="Content-Type">
    <meta content="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="app/assets/images/favicon.png" type="image/png">
    <link href="https://fonts.googleapis.com/css?family=Ubuntu" rel="stylesheet">

    <title>Cassini Systems</title>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="app/assets/bower_components/html5shiv/dist/html5shiv.min.js"></script>
    <script src="app/assets/bower_components/respond/dest/respond.min.js"></script>
    <![endif]-->

    <style type="text/css">
        .loading-text {
            color: white;
            text-align: center;
            width: 300px;
            padding-top: 60px;
            font-family: 'Open Sans',sans-serif;
            margin-left: -110px;
            font-size: 15px;
        }

        @-webkit-keyframes sk-rotateplane {
            0% { -webkit-transform: perspective(120px) }
            50% { -webkit-transform: perspective(120px) rotateY(180deg) }
            100% { -webkit-transform: perspective(120px) rotateY(180deg)  rotateX(180deg) }
        }

        @keyframes sk-rotateplane {
            0% {
                transform: perspective(120px) rotateX(0deg) rotateY(0deg);
                -webkit-transform: perspective(120px) rotateX(0deg) rotateY(0deg)
            } 50% {
                  transform: perspective(120px) rotateX(-180.1deg) rotateY(0deg);
                  -webkit-transform: perspective(120px) rotateX(-180.1deg) rotateY(0deg)
              } 100% {
                    transform: perspective(120px) rotateX(-180deg) rotateY(-179.9deg);
                    -webkit-transform: perspective(120px) rotateX(-180deg) rotateY(-179.9deg);
                }
        }

        .sk-cube-grid {
            width: 50px;
            height: 50px;
            margin-left: auto;
            margin-right: auto;
            margin-top: -100px !important;
            /*
             * Spinner positions
             * 1 2 3
             * 4 5 6
             * 7 8 9
             */
        }
        .sk-cube-grid .sk-cube {
            width: 33.33%;
            height: 33.33%;
            background-color: #fff;
            float: left;
            -webkit-animation: sk-cubeGridScaleDelay 1.3s infinite ease-in-out;
            animation: sk-cubeGridScaleDelay 1.3s infinite ease-in-out; }
        .sk-cube-grid .sk-cube1 {
            -webkit-animation-delay: 0.2s;
            animation-delay: 0.2s; }
        .sk-cube-grid .sk-cube2 {
            -webkit-animation-delay: 0.3s;
            animation-delay: 0.3s; }
        .sk-cube-grid .sk-cube3 {
            -webkit-animation-delay: 0.4s;
            animation-delay: 0.4s; }
        .sk-cube-grid .sk-cube4 {
            -webkit-animation-delay: 0.1s;
            animation-delay: 0.1s; }
        .sk-cube-grid .sk-cube5 {
            -webkit-animation-delay: 0.2s;
            animation-delay: 0.2s; }
        .sk-cube-grid .sk-cube6 {
            -webkit-animation-delay: 0.3s;
            animation-delay: 0.3s; }
        .sk-cube-grid .sk-cube7 {
            -webkit-animation-delay: 0.0s;
            animation-delay: 0.0s; }
        .sk-cube-grid .sk-cube8 {
            -webkit-animation-delay: 0.1s;
            animation-delay: 0.1s; }
        .sk-cube-grid .sk-cube9 {
            -webkit-animation-delay: 0.2s;
            animation-delay: 0.2s; }
        @-webkit-keyframes sk-cubeGridScaleDelay {
            0%, 70%, 100% {
                -webkit-transform: scale3D(1, 1, 1);
                transform: scale3D(1, 1, 1); }
            35% {
                -webkit-transform: scale3D(0, 0, 1);
                transform: scale3D(0, 0, 1); } }
        @keyframes sk-cubeGridScaleDelay {
            0%, 70%, 100% {
                -webkit-transform: scale3D(1, 1, 1);
                transform: scale3D(1, 1, 1); }
            35% {
                -webkit-transform: scale3D(0, 0, 1);
                transform: scale3D(0, 0, 1); } }
    </style>
</head>

<script>
    window.sessionId = "<%=session.getId()%>";
</script>

<body style="font-family: 'Ubuntu' !important;">
    <style scoped>
        h1, h2, h3, h4, h5, .table th {
            font-family: 'Ubuntu';
            font-weight: 700;
        }
        input, textarea, select {
            font-family: 'Ubuntu' !important;
        }
        .angular-center-container {
            position: fixed;
            top:0;
            left:0;
            height:100%;
            width:100%;
            display:table;
            pointer-events: none;
            z-index:9999;
        }
        .angular-centered {
            display: table-cell;
            vertical-align: middle;
            text-align: center;
        }
    </style>

    <div id="preloader"  class="angular-center-container">
        <div class="angular-centered">
            <div class="sk-cube-grid">
                <div class="sk-cube sk-cube1"></div>
                <div class="sk-cube sk-cube2"></div>
                <div class="sk-cube sk-cube3"></div>
                <div class="sk-cube sk-cube4"></div>
                <div class="sk-cube sk-cube5"></div>
                <div class="sk-cube sk-cube6"></div>
                <div class="sk-cube sk-cube7"></div>
                <div class="sk-cube sk-cube8"></div>
                <div class="sk-cube sk-cube9"></div>
                <div class="loading-text">
                    Loading Cassini.MS.  Please wait...
                </div>
            </div>
        </div>
    </div>

    <!-- Main app view -->
    <div id="appview" app-view class="appview" ui-view style="display: none;"></div>
    <div id="busy-indicator" class="loading style-2" style="display: none"><div class="loading-wheel"></div></div>

    <script type="text/javascript" src="app/assets/bower_components/requirejs/require.js" data-main="app/desktop/desktop.bootstrap.js"></script>
</body>

</html>