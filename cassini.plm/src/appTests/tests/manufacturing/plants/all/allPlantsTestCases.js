var urlsPage = require('../../../../pages/urlsPage.js');

  //---------Check Plants tab in the side navigation-------
  it('Check Plants tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();

    var manufacturingButton = element(by.id('manufacturing'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();

    var plantButton = element(by.id('plantType'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantButton), 25000);
    plantButton.click();
    browser.sleep(2000);
    expect(browser.getCurrentUrl()).toBe(urlsPage.allPlants)

});

describe(' All Plants Page Test cases: ', function () {

    beforeAll(function () {
        browser.get(urlsPage.allPlants);
        browser.sleep(5000);
    });


    it(" New Plant Button Text and Tooltip testCase ", function () {
        var newButtonn = element(by.id('newPlant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonn), 15000);
        expect(newButtonn.getText()).toEqual('New Plant');
        expect(newButtonn.getAttribute('title')).toEqual('New Plant');
        browser.sleep(5000);
    });
    

     //---------Check New Plant button-----------
     it('New Plant button click -> open new Plant Sidepanel test case', function () {
        
        var newPlantButton = element(by.id('newPlant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newPlantButton), 15000);
        newPlantButton.click();
        browser.sleep(3000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(3000);
        expect(newSidePanelTitle.getText()).toBe('New Plant');
        browser.sleep(5000);
    });

    // ----------Check Close(X) button on the New Plant screen-----------
    it("Click Close(X) button -> close the New Plant sidepanel screen test case", function () {
        var closeButton = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeButton), 15000);
        closeButton.click();
        browser.sleep(1000);

        var newPlantButton = element(by.id('newPlant'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newPlantButton), 15000);
        expect(newPlantButton.getText()).toEqual('New Plant');
        browser.sleep(5000);

    });

});

