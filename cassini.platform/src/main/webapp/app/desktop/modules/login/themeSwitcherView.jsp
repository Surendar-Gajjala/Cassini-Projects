<div>
    <style scoped>
        .theme-switcher {
            position: absolute;
            bottom: 30px;
            right: 0;
            height: 154px;
            width: 500px;
            display: none;
            background-color: var(--cassini-bg-color);
            z-index: 9999;
            border: 1px solid #eee;
            border-radius: 3px;
            padding: 20px;

            grid-template-columns: auto auto;
            grid-gap: 20px;
            justify-content: center;
        }

        .theme-switcher .theme-card {
            position: relative;
            width: 220px;
            cursor: pointer;
        }

        .theme-switcher .theme-card img {
            width: 100%;
            border-radius: 3px;
        }

        .theme-switcher .theme-card:hover {
            box-shadow: 0 7px 14px rgba(147, 148, 150, 0.25),
            0 5px 5px rgba(177, 177, 179, 0.22);
            transition: all .2s ease;
            transform: translate3D(0, -1px, 0) scale(1.01);
        }
    </style>

    <div id="themeSwitcher" class="theme-switcher">
        <div class="theme-card" ng-click="switchTheme('slategrey','switching')">
            <img src="app/assets/images/slategrey-theme.png" alt="">
        </div>
        <div class="theme-card" ng-click="switchTheme('darkblue','switching')">
            <img src="app/assets/images/darkblue-theme.png" alt="">
        </div>
    </div>
</div>