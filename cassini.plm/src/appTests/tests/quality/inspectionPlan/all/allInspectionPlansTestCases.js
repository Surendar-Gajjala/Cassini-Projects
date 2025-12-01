var urlsPage = require('../../../../pages/urlsPage');

it('Check InspectionPlan tab in the side navigation', function () {
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
    var plantButton = element(by.id('inspectionPlans'));
    var EC = protractor.ExpectedConditions;
    browser.wait(EC.presenceOf(plantButton), 25000);
    plantButton.click();
    browser.sleep(5000);
    expect(browser.getCurrentUrl()).toEqual(urlsPage.allInspectionPlan);
    browser.sleep(8000);
});



describe('All Product InspectionPlans Test cases:', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInspectionPlan);
        browser.sleep(5000);
        var selectProduct = element(by.id('productType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectProduct), 15000);
        selectProduct.click();
        browser.sleep(5000);
    });


    it('New Product InspectionPlan Button Text and Title Test case', function () {
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 15000);
        expect(newInspectionPlanButton.getAttribute('title')).toEqual('New Inspection Plan');
        expect(newInspectionPlanButton.getText()).toEqual('New Inspection Plan');
    });

    it('New Product InspectionPlan Button click -> open new Product InspectionPlan creation Side panel test case', function () {
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 10000);
        newInspectionPlanButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Product Inspection Plan');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close new Product InspectionPlan creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newInspectionPlanButton').isDisplayed(true));
        browser.sleep(5000);
    });



    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close Attributes  Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(5000);
    });

});

describe('All Material InspectionPlans Page Test cases', function () {

    beforeAll(function () {
        browser.get(urlsPage.allInspectionPlan);
        browser.sleep(5000);
        var selectMaterial = element(by.id('materialType'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectMaterial), 15000);
        selectMaterial.click();
        browser.sleep(5000);
    })


    it('New Material InspectionPlan Button Text and Title Test case', function () {
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 15000);
        expect(newInspectionPlanButton.getAttribute('title')).toEqual('New Inspection Plan');
        expect(newInspectionPlanButton.getText()).toEqual('New Inspection Plan');
    });

    it('New Material InspectionPlan Button click -> open new Material InspectionPlan creation Side panel test case', function () {
        var newInspectionPlanButton = element(by.id('newInspectionPlanButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newInspectionPlanButton), 10000);
        newInspectionPlanButton.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(newSidePanelTitle.getText()).toBe('New Material Inspection Plan');
        browser.sleep(5000);
    });


    it('Click Close(X) button -> close new Material InspectionPlan creation Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#newInspectionPlanButton').isDisplayed(true));
        browser.sleep(5000);
    });



    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show Attributes');
        browser.sleep(5000);
    });

    it('Attributes Button click -> open Attributes side panel test case', function () {
        var attributeClick = element(by.id('attributesButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        browser.sleep(2000);
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
        browser.sleep(5000);
    });

    it('Click Close(X) button -> close Attributes  Side panel test case', function () {
        var closeRightSidePanel = element(by.id('closeRightSidePanel'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(closeRightSidePanel), 10000);
        closeRightSidePanel.click();
        browser.sleep(2000);
        expect($('#closeRightSidePanel').isDisplayed(false));
        browser.sleep(8000);
    });


});

describe('Prefered page button Testing', function () {

  

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferredPageButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
        browser.sleep(5000);
    });

    // it('preferenPage Button click', function () {
    //     var preferenPageButtonClick = element(by.id('preferredPageButton'));
    //     var EC = protractor.ExpectedConditions;
    //     browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
    //     preferenPageButtonClick.click();
    //     browser.sleep(2000);
    // });

});