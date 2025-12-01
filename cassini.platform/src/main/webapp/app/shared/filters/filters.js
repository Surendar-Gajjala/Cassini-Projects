define(
    [
        'app/shared/services/services.module'
    ],

    function (module) {

        module.filter('highlightText', ['$sce', function ($sce) {

            return function (input, match, className) {

                // Extract value from string
                var output = $sce.valueOf(input);

                if(typeof output === 'string') {

                    // If match string is defined
                    if (match) {
                        var found = true;
                        var arr = match.split(' ');
                        var newArr = [];
                        for(var i=0; i<arr.length; i++) {
                            if(arr[i].length > 0) {
                                if (output.trim().toLowerCase().indexOf(arr[i].trim().toLowerCase()) === -1) {
                                    found = false;
                                }
                                newArr.push(arr[i].trim());
                            }
                        }
                        if(found) {
                            var re = new RegExp(newArr.join("|"), "gi");
                            output = output.replace(re, '<span class="' + (className || 'highlighted') + '">$&</span>');
                        }
                    }
                }

                // Return trusted html string
                return $sce.trustAsHtml(output);

            };

        }])
    }
);
