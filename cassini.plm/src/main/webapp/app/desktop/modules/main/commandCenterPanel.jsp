<div>
    <style scoped>

        .command-center-panel {
            border-radius: 5px;
            width: 500px;
            height: 120px;
            z-index: 9990;
            margin-left: -250px;
            display: none;
            opacity: 0.9 !important;
            overflow-y: auto;
            border: 1px solid #2b5876;
            background: #005C97; /* fallback for old browsers */
            background: -webkit-linear-gradient(to right, #363795, #005C97); /* Chrome 10-25, Safari 5.1-6 */
            background: linear-gradient(to right, #363795, #005C97); /* W3C, IE 10+/ Edge, Firefox 16+, Chrome 26+, Opera 12+, Safari 7+ */
        }

        .command-center-panel.centered {
            position: absolute;
            top: 30%;
            left: 50%;
        }

        #commandCenter .content {
            margin: 20px auto;
            width: 90%;
            display: -webkit-flex; /* Safari */
            -webkit-flex-flow: wrap; /* Safari 6.1+ */
            display: flex;
            flex-flow: wrap;
            text-align: center;
        }

        #commandCenter .content > div {
            margin: 20px;
            width: 132px;
        }

        #commandCenter .content a {
            color: white;
            -webkit-filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.4));
            filter: drop-shadow(0 1px 3px rgba(0, 0, 0, 0.4));
            font-size: 14px;
            text-align: center;
            text-shadow: 1px 1px 0 rgba(0, 0, 0, 0.5);
            white-space: nowrap;
        }

        #commandCenter .content a img {
            width: 64px;
            height: 64px;
        }

        #commandCenter .content a i {
            font-size: 64px;
            margin-bottom: 10px;
        }

        #commandCenter .content a:active {
            opacity: .6;
        }

        #commandCenter .content a:link,
        #commandCenter .content a:visited {
            text-decoration: none;
        }

        #commandCenter .content a[class^="icon"] {
            background-position: top;
            background-repeat: no-repeat;
            display: inline-block;
            height: 64px;
            padding: 64px 0 0 0;
            margin: 40px;
            width: 64px;
        }

        .close-btn {
            font: 14px/100% arial, sans-serif;
            position: absolute;
            right: 10px;
            text-decoration: none;
            text-shadow: 0 1px 0 #fff;
            top: 10px;
            color: #fff;
        }

        .close-btn:hover, .close-btn:visited,
        .close-btn:link {
            opacity: 1.0 !important;
            text-shadow: none;
            color: #fff;
            text-decoration: none;
        }

        .close-btn:active {
            opacity: 0.6 !important;
            text-shadow: none;
            color: #fff;
            text-decoration: none;
        }

        #commandCenter [class^="flaticon-"]::before,
        #commandCenter [class*=" flaticon-"]::before,
        #commandCenter [class^="flaticon-"]::after,
        #commandCenter [class*=" flaticon-"]::after {
            font-size: 64px;
        }

        a {
            cursor: pointer;
        }

        a:hover, a:active {
            text-decoration: none;
        }

        .command-form {
            border-radius: 30px;
            border: 2px solid #fff;
            padding-left: 15px;
            background-color: inherit;
            margin-top: 20px;
            color: #fff !important;
            font-size: 18px;
        }

        .command-form:focus {
            border-color: #fff;
            color: #fff !important;
        }
    </style>


    <div id="commandCenter" class="centered command-center-panel style-8" ng-click="mainVm.stopPropagation($event)">
        <a class="close-btn" ng-click="commandCenterVm.close();">&#10006;</a>

        <div class="content">
            <input type="text"
                   class="form-control input-sm command-form"
                   placeholder="Enter command">
        </div>
    </div>
</div>