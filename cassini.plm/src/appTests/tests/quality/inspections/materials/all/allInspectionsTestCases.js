var urlsPage = require('../../../../../pages/urlsPage');

it('Check Inspection tab in the side navigation', function () {
    var sideManuButton = element(by.id('sideToggler'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(sideManuButton), 15000);
    sideManuButton.click();
    browser.sleep(2000);
    var manufacturingButton = element(by.id('quality'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(manufacturingButton), 15000);
    manufacturingButton.click();
    browser.sleep(2000);
    var plantButton = element(by.id('inspections'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantButton), 25000);
    plantButton.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allInspections);
    browser.sleep(8000);
});

describe('All Material Inspections Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInspections);
        browser.sleep(5000);
        var selectMaterial = element(by.id('materialType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMaterial), 15000);
        selectMaterial.click();
        browser.sleep(3000);
    });


    it('New Inspection Button Text and Title test cases', function () {
        var newInspectionButton = element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionButton), 15000);
        expect(newInspectionButton.getAttribute('title')).toEqual('New Inspection');
        browser.sleep(2000);
        expect(newInspectionButton.getText()).toEqual('New Inspection');
        browser.sleep(8000);
    });

    it('New Inspection Button click -> open new Inspection creation Side panel test case', function () {
        var newInspectionButton = element(by.id('newInspectionButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionButton), 10000);
        newInspectionButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Material Inspection');
        browser.sleep(8000);
    });


    it('Click Close(X) button -> close new Inspection creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newInspectionButton').isDisplayed(true));
        browser.sleep(5000);
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
    });

    it('Attributes Button click-> open Attributes Side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
    });

    it('Click Close(X) button -> close Attributes Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });


});

