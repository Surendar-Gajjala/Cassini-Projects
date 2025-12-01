var ecoPage = require('../pages/ecoPage');

describe('Eco Url Testing', function () {
    it('Url validation', function () {
        ecoPage.get(ecoPage.allEcos);
        browser.sleep(3000);
        ecoPage.verifyUrl(ecoPage.allEcos);
        browser.sleep(2000);
    });
});


describe('Eco Testing', function () {

    beforeEach(function () {
        browser.get(ecoPage.allEcos);
    });

    it('Page Title', function () {
        var title = element(by.css('.view-toolbar')).element(by.tagName('span'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(title), 8000);
        expect(title.getText()).toEqual('ECOs');
    });

    // ----------- New Button ---------------------

    it('New Button', function () {
        var button = element(by.css('.view-toolbar')).element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(button), 8000);
        expect(button.getText()).toEqual('New ECO');

    });
    
    it('Button Title', function () {
        var buttonTitle = element(by.css('.view-toolbar')).element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(buttonTitle), 8000);
        expect(buttonTitle.getAttribute('title')).toEqual('New ECO');
    });


    it('New Button Click', function () {
        var newButtonClick = element(by.id('newButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(newButtonClick), 10000);
        newButtonClick.click();
        browser.sleep(2000);
        var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(newSidePanelTitle.getText()).toBe('New ECO');

        var selectbuttonClick = element(by.id('select'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectbuttonClick), 5000);
        selectbuttonClick.click();
      
        
        var selectEcoType =  element(by.id('changeTypeTree')).element(by.xpath('li/ul/li/div'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(selectEcoType), 15000);
        selectEcoType.click();


        var owner =  element(by.model('newEcoVm.newECO.ecoOwnerObject'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(owner), 15000);
        owner.click(); 
        element.all(by.css('.ui-select-choices-row-inner div')).first().click();
       

        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.title').sendKeys('ECO 4')), 25000);
        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.description').sendKeys('description')), 30000);
        browser.wait(EC.presenceOf(ecoPage.byModel('newEcoVm.newECO.reasonForChange').sendKeys('reasonForChange')), 35000);
       
       var workflow =  element(by.model('newEcoVm.newECO.workflow'));
       var EC = protractor.ExpectedConditions;
       browser.wait(EC.presenceOf(workflow), 15000);
       workflow.click(); 
       element.all(by.css('.ui-select-choices-row-inner div')).first().click();

        var createButton = element(by.buttonText('Create'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(createButton), 10000);
        createButton.click();
        browser.sleep(5000);
       // expect(browser.getCurrentUrl()).toContain(ecoPage.basic);

       
       browser.getCurrentUrl().then(function(url){
       var eco_id=url.split("eco/")[1].replace("?tab=details.basic","");
        expect(browser.getCurrentUrl())
              .toEqual("http://localhost:8085/#/app/changes/eco/"+eco_id+"?tab=details.basic");
      
      });
    
    });


    // ----------- Search icon ---------------------

    it('Search icon Title', function () {
        var searchTitle = element(by.id('searchIcon'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(searchTitle), 10000);
        expect(searchTitle.getAttribute('title')).toEqual('Search');
    });

    it('Search icon Click', function () {
        var searchClick = element(by.id('searchIcon'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(searchClick), 10000);
        searchClick.click();
        browser.sleep(2000);
        var searchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(searchSidePanelTitle.getText()).toBe('ECOs Search');
    });


    //---------- folder button Testing --------------
    it('dropdown folder click', function () {

        var folderClick = element(by.id('dropdownButton'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(folderClick), 5000);
        folderClick.click();
        browser.sleep(2000);


    });

    //---------- Attribute button Testing --------------
    it('Attributes Button click', function () {
        var attributeClick = element(by.id('attributes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeClick), 10000);
        attributeClick.click();
        browser.sleep(2000);
        var attributeSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(attributeSidePanelTitle.getText()).toBe('Attributes');
    });

    it('Attributes Button Title', function () {
        var attributeButtonTitle = element(by.id('attributes'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(attributeButtonTitle), 10000);
        expect(attributeButtonTitle.getAttribute('title')).toEqual('Show ECO Attributes');
    });


    // ---------- Saved Search button Testing --------------
    it('savedSearch Button click', function () {
        var savedSearchButtonClick = element(by.id('savedSearch'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(savedSearchButtonClick), 10000);
        savedSearchButtonClick.click();
        browser.sleep(2000);
        var savedSearchSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
        expect(savedSearchSidePanelTitle.getText()).toBe('Saved Searches');
    });

    it('Attributes Button Title', function () {
        var savedSearchButtonTitle = element(by.id('savedSearch'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(savedSearchButtonTitle), 10000);
        expect(savedSearchButtonTitle.getAttribute('title')).toEqual('Saved Searches');
    });


    //---------- Prefered Page  button Testing --------------
    it('preferenPage Button click', function () {
        var preferenPageButtonClick = element(by.id('preferedPage'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonClick), 10000);
        preferenPageButtonClick.click();
        browser.sleep(2000);
    });

    it('preferenPage Button Title', function () {
        var preferenPageButtonTitle = element(by.id('preferedPage'));
        var EC = protractor.ExpectedConditions;
        browser.wait(EC.presenceOf(preferenPageButtonTitle), 10000);
        expect(preferenPageButtonTitle.getAttribute('title')).toEqual('Make as preferred start page');
    });

});