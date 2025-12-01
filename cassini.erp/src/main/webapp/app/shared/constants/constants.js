define(['app/app.modules'], function(app)
{
    app.factory('CONSTANTS',
    [
        function()
        {
            return {
                REQUIRED: "Field is required",
                PASSWORDMISMATCH: "Password and confirmation password do not match",
                INVALIDEMAIL: "Invalid email format",
                INVALIDMOBILE: "Invalid mobile format",
                INVALIDZIP: "Invalid zip code",
                STARTENDDATE: "End date should be greater than Start date"
            }
        }
    ]);
});