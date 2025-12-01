var urlsPage = require('../../pages/urlsPage.js');

var changeFileDirectiveTestCases= function(){

    var path = require('path');

    this.filesTab = function(){
    
        it(' click here to add files button upload files TestCase',function(){
            var fileToUpload= '../../../TestUpload/111.JPG';
        
            var absolutePath = path.resolve(__dirname,fileToUpload);
            element(by.css("[ng-click='selectFiles()']")).sendKeys(absolutePath);
            browser.sleep(2000);
        });
        
        it('New Folder Button click -> open New folder sidepanel and Click close(X) button -> close  New folder sidepanel Test cases', function () {
            var clickonPlusButton = element(by.css('#itemFilesTable .la-plus'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonPlusButton), 15000);
            clickonPlusButton.click();
            browser.sleep(1500);
            var clickonNewFolderButton = element(by.css("[ng-click='addItemFolder(null)']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonNewFolderButton), 15000);
        
            expect(clickonNewFolderButton.getText()).toBe('New Folder');
        
            clickonNewFolderButton.click();
            browser.sleep(1500);
        
            var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
            expect(newSidePanelTitle.getText()).toBe('New Folder');
            browser.sleep(1000);
            element(by.id('closeRightSidePanel')).click();
        });
        
        it('Check New Folder creation->without entering mandatory fields data', function () {
            var clickonPlusButton = element(by.css('#itemFilesTable .la-plus'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonPlusButton), 15000);
            clickonPlusButton.click();
            browser.sleep(1500);
            var clickonNewFolderButton = element(by.css("[ng-click='addItemFolder(null)']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonNewFolderButton), 15000);
        
            clickonNewFolderButton.click();
            browser.sleep(1500);
        
            var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
            expect(newSidePanelTitle.getText()).toBe('New Folder');
        
        
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(1500);
            var plantWarnMes = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
            expect(plantWarnMes).toEqual('Please enter name');
            browser.sleep(5000);
            element(by.id('closeRightSidePanel')).click();
        
        });
        
        it('Check New Folder creation->with entering mandatory fields data', function () {
            var clickonPlusButton = element(by.css('#itemFilesTable .la-plus'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonPlusButton), 15000);
            clickonPlusButton.click();
            browser.sleep(1500);
            var clickonNewFolderButton = element(by.css("[ng-click='addItemFolder(null)']"));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonNewFolderButton), 15000);
        
            clickonNewFolderButton.click();
            browser.sleep(1500);
        
            var newSidePanelTitle = element(by.id('rightSidePanel')).element(by.tagName('h3'));
            expect(newSidePanelTitle.getText()).toBe('New Folder');
        
            browser.wait(EC.presenceOf(urlsPage.byModel('newFolderVm.newFolder.name').sendKeys('Folder103')), 35000);
            browser.sleep(3500);
        
            browser.wait(EC.presenceOf(urlsPage.byModel('newFolderVm.newFolder.description').sendKeys('description')), 35000);
        
        
            var createButton = element(by.buttonText('Create'));
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(createButton), 10000);
            createButton.click();
            browser.sleep(4800);
            var plantWarnMes = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
            expect(plantWarnMes).toEqual('Folder created successfully');
            browser.sleep(8000);
        
        });

        it('Check on Delete button -> Delete the folder successfully', function () {
           
            var clickonActionButton = element.all(by.css('.fa-ellipsis-h')).first();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonActionButton), 15000);
            clickonActionButton.click();
            browser.sleep(1500);
            var clickonDeleteButton = element.all(by.css("[ng-click='deleteFolder(file)']")).first();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(clickonDeleteButton), 15000);
            clickonDeleteButton.click();
            browser.sleep(2500);
        
            var deletePopUpWindow = element(by.css('.modal-header')).element(by.tagName('h4'));
            browser.sleep(1500);
            expect(deletePopUpWindow.getText()).toBe('Delete folder');
        
            var deleteButtonYes = element(by.css("[ng-click='confirmVm.onOk()']"));
            browser.sleep(1500);
            expect(deleteButtonYes.getText()).toBe('OK');
            deleteButtonYes.click();
            browser.sleep(4800);
            var plantWarnMes = element(by.id('alertMessage')).getText();
            var EC = protractor.ExpectedConditions;
            browser.wait(EC.presenceOf(plantWarnMes), 5000, "Text is not something I've expected");
            expect(plantWarnMes).toEqual('Folder deleted successfully');
            browser.sleep(8000);
        
        });
    }

}
module.exports = new changeFileDirectiveTestCases();




