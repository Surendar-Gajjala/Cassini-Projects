/*
var root = document.documentElement;

function setDarkBlueTheme() {
    root.style.setProperty('--cassini-theme-dark-color', "rgb(0, 37, 63)");
    root.style.setProperty('--cassini-theme-light-color', "rgb(7, 55, 88)");
    root.style.setProperty('--cassini-theme-lighter-color', "rgb(27, 78, 113)");
    root.style.setProperty('--cassini-theme-more-lighter-color', "#157d9c");
    root.style.setProperty('--cassini-theme-widget-hover-color', "rgba(17, 116, 155, 0.5)");
}

function setSlateGreyTheme() {
    root.style.setProperty('--cassini-theme-dark-color', "#20262E");
    root.style.setProperty('--cassini-theme-light-color', "#2B333E");
    root.style.setProperty('--cassini-theme-lighter-color', "#383E45");
    root.style.setProperty('--cassini-theme-more-lighter-color', "#495563");
    root.style.setProperty('--cassini-theme-widget-hover-color', "#4e5157");
}
function updateUserPreferences(theme) {
    if ($rootScope.userPreferences != null) {
        if ($rootScope.userPreferences.userTheme != theme) {
            $rootScope.userPreferences.userTheme = theme;
            $rootScope.updatePreference($rootScope.userPreferences);
        }

    } else {
        var userPreferences = {
            id: null,
            login: $rootScope.loginPersonDetails.id,
            userTheme: theme
        };
        $rootScope.updatePreference(userPreferences);
    }
}

function switchTheme(theme) {
    if (theme === 'darkblue') {
        setDarkBlueTheme();
        updateUserPreferences(theme);
    }
    else if (theme === 'slategrey') {
        setSlateGreyTheme();
        updateUserPreferences(theme);
    }
}


ThemeSwitcher = {
    switchTheme: switchTheme
};*/
