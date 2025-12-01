var urlsPage = require('../../../pages/urlsPage.js');

describe('Settings -> Lifecycles: ', function () {

    beforeAll(function () {
       browser.get(urlsPage.homeUrl);
       browser.sleep(5000);
    });

    //---------Select Lifecycles-------
    it('Select Lifecycles ', function () {
        var sideManuButton = element(by.id('sideToggler'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(sideManuButton), 15000);
        sideManuButton.click();
        browser.sleep(3000);
        var settingsButton = element(by.id('settings'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(settingsButton), 15000);
        settingsButton.click();
        browser.sleep(5000);

        var selectLifeCycle = element.all(by.id('settingsTree')).all(by.xpath('li/ul/li')).get(1);
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectLifeCycle), 15000);
        selectLifeCycle.click();
        browser.sleep(5000);
        expect(browser.getCurrentUrl()).toEqual(urlsPage.lifecyclesUrl);
        browser.sleep(10000);
    });

   
    //------------------Header Text testCase---------------------
    it("Header Text testCase ", function () {
        browser.sleep(1000);
        var header = element(by.css('h3.ng-scope'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(header), 15000);
        expect(header.getText()).toEqual('Lifecycles');
        browser.sleep(10000);
    });

     //---------Check New Lifecycles Button Tooltip-----------
     it("Check New Lifecycles Button Tooltip", function () {
        browser.sleep(1000);
        var newLifeCycleButtonTitle = element(by.id('lifecycleAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newLifeCycleButtonTitle), 15000);
        expect(newLifeCycleButtonTitle.getAttribute('title')).toEqual('New Lifecycle');
        browser.sleep(8000);
    });

     //---------Check New Lifecycles Button Click-----------
     xit("Check New Lifecycles Button Click", function () {
        browser.sleep(1000);
        var newLifeCycleButton = element(by.id('lifecycleAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newLifeCycleButton), 15000);
        newLifeCycleButton.click();
        browser.sleep(2000);
        expect(element(by.model('lc.newName')).isPresent()).toBe(true);
        browser.sleep(8000);
    });

    //---------Check New Lifecycles Button Click-----------
    it("Create New Lifecycles", function () {
        browser.sleep(1000);
        var newLifeCycleButton = element(by.id('lifecycleAddButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newLifeCycleButton), 10000);
        newLifeCycleButton.click();
        browser.sleep(2000);

        var enterNewLifeCycleName = element(by.model('lc.newName'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(enterNewLifeCycleName), 10000);
        enterNewLifeCycleName.sendKeys('Testing Lifecycle');
        browser.sleep(2000);

        var createButton = element(by.css('[class="las la-check"]'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 15000);
        createButton.click();
        browser.sleep(2000);

        var alertMessage = element(by.id('alertMessage')).getText();
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(alertMessage), 5000, "Text is not something I've expected");
        expect(alertMessage).toEqual('Lifecycle saved successfully');
        browser.sleep(8000);
    });


    


});
