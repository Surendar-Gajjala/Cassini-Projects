var loginPage = require('../pages/loginPage');


describe('Url Testing', function () {
    it('Url validation', function () {
        loginPage.get(loginPage.homeUrl);
        browser.sleep(3000);
        loginPage.verifyUrl(loginPage.loginUrl);
        browser.sleep(2000);
    });
});

describe('Login Testing', function () {

    it('Empty username', function () {
        loginPage.get(loginPage.loginUrl);
        browser.sleep(2000);
        loginPage.enterUserName('');
        loginPage.clickGo();
        browser.sleep(500);
        loginPage.verifyTextValue('Please enter user name');
        browser.sleep(4000);
    });

    it('Empty password', function () {
        loginPage.get(loginPage.loginUrl);
        browser.sleep(2000);
        loginPage.enterUserName('admin');
        loginPage.enterPassword('');
        loginPage.clickGo();
        browser.sleep(500);
        loginPage.verifyTextValue('Please enter password');
        browser.sleep(4000);
    });

    it('Invalid login credentials', function () {
        loginPage.get(loginPage.loginUrl);
        browser.sleep(2000);
        loginPage.enterUserName('admin');
        loginPage.enterPassword('cassini1');
        loginPage.clickGo();
        browser.sleep(500);
        loginPage.verifyTextValue('Incorrect username or password');
        browser.sleep(2000);
    });

    it('Valid login credentials', function () {
        loginPage.get(loginPage.loginUrl);
        browser.sleep(2000);
        loginPage.enterUserName('admin@cassini_plm');
        loginPage.enterPassword('ejr3xym');
        loginPage.clickGo();
        browser.sleep(2000);
        loginPage.verifyUrl(loginPage.homeUrl);
        browser.sleep(5000);
    });

});

