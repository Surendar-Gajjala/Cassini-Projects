var loginPage = require('../../pages/loginPage');
var urlsPage = require('../../pages/urlsPage');


describe('Url Testing', function () {
    // it('Url validation', function () {
    //     loginPage.get(urlsPage.homeUrl);
    //     browser.sleep(3000);
    //     loginPage.verifyUrl(urlsPage.loginUrl);
    //     browser.sleep(2000);
    // });
});

describe('Login Testing', function () {

    // it('Empty username', function () {
    //     loginPage.get(urlsPage.loginUrl);
    //     browser.sleep(2000);
    //     loginPage.enterUserName('');
    //     loginPage.clickGo();
    //     browser.sleep(500);
    //     loginPage.verifyTextValue('Please enter user name');
    //     browser.sleep(4000);
    // });

    // it('Empty password', function () {
    //     loginPage.get(urlsPage.loginUrl);
    //     browser.sleep(2000);
    //     loginPage.enterUserName('admin');
    //     loginPage.enterPassword('');
    //     loginPage.clickGo();
    //     browser.sleep(500);
    //     loginPage.verifyTextValue('Please enter password');
    //     browser.sleep(4000);
    // });

    // it('Invalid login credentials', function () {
    //     loginPage.get(urlsPage.loginUrl);
    //     browser.sleep(2000);
    //     loginPage.enterUserName('admin');
    //     loginPage.enterPassword('cassini1');
    //     loginPage.clickGo();
    //     browser.sleep(500);
    //     loginPage.verifyTextValue('Incorrect username or password');
    //     browser.sleep(2000);
    // });

    it('Valid login credentials', function () {
        browser.manage().window().maximize();
        loginPage.get(urlsPage.loginUrl);
        browser.sleep(4000);
        loginPage.enterUserName('admin@cassini_plm');
        loginPage.enterPassword('xq6stey');
        loginPage.clickGo();
        browser.sleep(10000);
        loginPage.verifyUrl(urlsPage.homeUrl);
        //expect(browser.getCurrentUrl()).toEqual(urlsPage.homeUrl);
        browser.sleep(8000);
        //browser.get(urlsPage.homeUrl);
        //browser.sleep(8000);
    });

});

