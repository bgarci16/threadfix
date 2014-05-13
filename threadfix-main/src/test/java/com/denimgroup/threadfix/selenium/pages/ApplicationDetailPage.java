////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2014 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.selenium.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicationDetailPage extends BasePage {

    WebDriverWait wait = new WebDriverWait(driver, 10);

    public ApplicationDetailPage(WebDriver webdriver) {
        super(webdriver);
    }

    public ApplicationDetailPage clickAddDefectTrackerButton() {
        driver.findElementById("addDefectTrackerButton").click();
        sleep(1000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setUsername(String dtName) {
        driver.findElementById("username").clear();
        driver.findElementById("username").sendKeys(dtName);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickEditDefectTrackerButton() {
        driver.findElementById("editDefectTrackerButton").click();
        sleep(2000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setPassword(String dtPass) {
        driver.findElementById("password").clear();
        driver.findElementById("password").sendKeys(dtPass);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickTestConnection() {
        waitForElement(driver.findElementById("getProductNames"));
        driver.findElementById("getProductNames").click();
        sleep(5000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage selectProduct(String product) {
        sleep(4000);
        new Select(driver.findElementById("productNameSelect"))
                .selectByVisibleText(product);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage selectDefectTracker(String defectTracker) {
        waitForElement(driver.findElementById("defectTrackerId"));
        new Select(driver.findElementById("defectTrackerId"))
                .selectByVisibleText(defectTracker);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage addDefectTracker(String defectTracker, String username,
                                                  String password, String productName) {
        clickEditDeleteBtn()
                .clickAddDefectTrackerButton()
                .selectDefectTracker(defectTracker)
                .setUsername(username)
                .setPassword(password)
                .clickTestConnection()
                .selectProduct(productName)
                .clickUpdateApplicationButton()
                .clickUpdateApplicationButton();

        sleep(2000);

        return new ApplicationDetailPage(driver);

    }

    public ApplicationDetailPage addWaf(String wafName) {
        Select s = new Select(driver.findElementById("wafSelect"));
        s.selectByVisibleText(wafName);
        sleep(4000);
        return this;
    }

    public ApplicationDetailPage saveWafAdd() {
        driver.findElementById("submit").click();
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickActionButton() {
        waitForElement(driver.findElementById("actionButton1"));
        driver.findElementById("actionButton1").click();
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickViewPermUsers() {
        clickActionButton();
        driver.findElementById("userListModelButton").click();
        waitForElement(driver.findElementById("usersModal"));
        return new ApplicationDetailPage(driver);
    }

    public int getNumPermUsers() {
        return driver.findElementById("userTableBody").findElements(By.className("bodyRow")).size();
    }

    public ApplicationDetailPage clickEditDeleteBtn() {
        clickActionButton();
        sleep(2000);
        waitForElement(driver.findElementById("editApplicationModalButton"));
        driver.findElementById("editApplicationModalButton").click();
        waitForElement(driver.findElementById("deleteLink"));
        return new ApplicationDetailPage(driver);
    }

    public String getWafText() {
        waitForElement(driver.findElementById("wafName"));
        return driver.findElementById("wafName").getText();
    }

    public String getNameText() {
        return driver.findElementById("nameText").getText();
    }

    public String getUrlText() {
        return driver.findElementById("urlInput").getAttribute("value");
    }

    public ApplicationDetailPage clickScansTab() {
        sleep(1000);
        driver.findElementByLinkText("1 Scan").click();
        waitForElement(driver.findElementByLinkText("Delete Scan"));
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickAddWaf() {
        driver.findElementById("addWafButton").click();
        waitForElement(driver.findElementById("wafSelect"));
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setNameInput(String appName2) {
        driver.findElementById("nameInput").clear();
        driver.findElementById("nameInput").sendKeys(appName2);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setUrlInput(String url) {
        driver.findElementById("urlInput").clear();
        driver.findElementById("urlInput").sendKeys(url);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickUpdateApplicationButton() {
        sleep(5000);
        driver.findElementById("submit").click();
        sleep(5000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickUpdateApplicationButtonInvalid() {
        sleep(1000);
        driver.findElementById("submit").click();
        sleep(1000);
        return new ApplicationDetailPage(driver);
    }

    public String getNameError() {
        return driver.findElementById("applicationNameInputNameError").getText();
    }

    public String getNameRequiredError() {
        return driver.findElementById("applicationNameInputRequiredError").getText();
    }

    public String getUrlError() {
        return driver.findElementById("applicationUrlInputInvalidUrlError").getText().trim();
    }

    public ApplicationDetailPage clickDeleteScanButton() {
        driver.findElementByLinkText("Delete Scan").click();
        handleAlert();
        return new ApplicationDetailPage(driver);
    }

    public int modalNumber() {
        String s = driver.findElementByClassName("modal").getAttribute("id");
        Pattern pattern = Pattern.compile("^\\D+([0-9]+)$");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    public String getAlert() {
        return driver.findElementByClassName("alert-success").getText();
    }

    public ApplicationDetailPage clickDynamicSubmit() {
        driver.findElementById("submit").click();
        sleep(1000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setCWE(String Status) {
        driver.findElementById("txtSearch").clear();
        driver.findElementById("txtSearch").sendKeys(Status);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setURL(String Status) {
        driver.findElementById("urlDynamicSearch").clear();
        driver.findElementById("urlDynamicSearch").sendKeys(Status);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setParameter(String Status) {
        driver.findElementById("parameterInput").clear();
        driver.findElementById("parameterInput").sendKeys(Status);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setDescription(String Status) {
        driver.findElementById("descriptionInput").clear();
        driver.findElementById("descriptionInput").sendKeys(Status);
        return new ApplicationDetailPage(driver);
    }

    public String selectSeverityList(String text) {
        Select severity = new Select(driver.findElementById("severityInput"));
        severity.selectByVisibleText(text);
        return severity.getFirstSelectedOption().getText();
    }

    public ApplicationDetailPage clickCloseAppInvalid() {
        driver.findElementByLinkText("Close").click();
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickScanAgentTasksTab(int count) {
        driver.findElementById(count + " Scan Agent Tasks").click();
        sleep(1000);
        waitForElement(driver.findElementById("scanQueueTable"));
        return new ApplicationDetailPage(driver);
    }

    public int scanQueueCount() {
        WebElement scanQueueTab;
        try {
            scanQueueTab = driver.findElementById("scanQueueTabLink");
        } catch (NoSuchElementException e) {
            return 0;
        }

        String scanText = scanQueueTab.getText().trim();
        Pattern pattern = Pattern.compile("^\\s*(\\d+)");
        Matcher matcher = pattern.matcher(scanText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    public ApplicationDetailPage clickAddNewScanTask() {
        driver.findElementById("addScanQueueLink").click();
        waitForElement(driver.findElementById("submit"));
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setDocFileInput(String file) {
        driver.findElementById("docInput" + modalNumber()).sendKeys(file);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage submitScanQueue() {
        driver.findElementById("submit").click();
        sleep(1000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickDocumentTab() {
        driver.findElementById("documentsTab").click();
        waitForElement(driver.findElementById("uploadDocModalLink"));
        return new ApplicationDetailPage(driver);
    }

    public int docsCount() {
        WebElement scanQueueTab;
        try {
            scanQueueTab = driver.findElementById("documentsTab");
        } catch (NoSuchElementException e) {
            return 0;
        }

        String scanText = scanQueueTab.getText().trim();
        Pattern pattern = Pattern.compile("^\\s*(\\d+)");
        Matcher matcher = pattern.matcher(scanText);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return -1;
    }

    public ApplicationDetailPage clickUploadDocLink() {
        driver.findElementById("uploadDocModalLink" + modalNumber()).click();
        waitForElement(driver.findElementById("uploadDoc" + modalNumber()));
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setScanQueueType(String scanQueueType) {
        new Select(driver.findElementById("scanner"))
                .selectByVisibleText(scanQueueType);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage submitDoc() {
        driver.findElementById("submitDocModal" + modalNumber()).click();
        sleep(3000);
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage setTeam(String team) {
        new Select(driver.findElementById("organizationId")).selectByVisibleText(team);
        sleep(2000);
        return new ApplicationDetailPage(driver);
    }

    public String specificVulnerabilityCount(String level) {
        List<WebElement> headers = driver.findElementsByClassName("vulnSectionHeader");

        for (WebElement header : headers) {
            if (header.getText().contains(level)) {
                String count = header.getText();
                count = count.substring(count.length() - 2, count.length() - 1);
                return count;
            }
        }

        return "0";
    }

    public boolean vulnsFilteredHidden(int count) {
        return driver.findElementByLinkText( count + " Hidden").isDisplayed();
    }

    public boolean vulnsFilteredOpen(int count) {
        return driver.findElementByLinkText( count + " Open Vulnerabilities").isDisplayed();
    }

    public FilterPage clickEditVulnerabilityFilters() {
        driver.findElementById("editVulnerabilityFiltersButton").click();
        return new FilterPage(driver);
    }

    public ApplicationDetailPage clickManualFindingButton() {
        driver.findElementById("addManualFindingModalLink").click();
        waitForElement(driver.findElementById("txtSearch"));
        return new ApplicationDetailPage(driver);
    }

    public ApplicationDetailPage clickStaticRadioButton() {
        driver.findElementById("staticRadioButton").click();
        return new ApplicationDetailPage(driver);
    }

    public ScanDetailPage clickViewScan() {
        driver.findElementByLinkText("View Scan").click();
        return new ScanDetailPage(driver);
    }

    public ApplicationDetailPage clickSourceInfo(){
        waitForElement(driver.findElementByLinkText("Source Code Information"));
        driver.findElementByLinkText("Source Code Information").click();
        return new ApplicationDetailPage(driver);
    }

    /*________________ Boolean Functions ________________*/

    public boolean isApplicationNamePresent() {
        return driver.findElementById("nameText").isDisplayed();
    }

    public boolean isBreadcrumbPresent() {
        return driver.findElementByLinkText("Applications Index").isDisplayed();
    }

    public boolean isApplicationBreadcrumbPresent(String applicationName) {
        return driver.findElementByLinkText("Team: " + applicationName).isDisplayed();
    }

    public boolean vulnerabilitiesFiltered(String level, String expected) {
        return specificVulnerabilityCount(level).equals(expected);
    }

    public boolean isScanQueuePresent(String scanner) {
        int rowCnt = driver.findElementsByClassName("bodyRow").size();
        for (int i = 1; i <= rowCnt; i++) {
            if (driver.findElementById("scannerType" + i).getText().trim().equals(scanner)) {
                return true;
            }
        }
        return false;
    }

    public boolean isUserPresentPerm(String user) {
        for (int i = 1; i <= getNumPermUsers(); i++) {
            if (driver.findElementById("name" + i).getText().contains(user)) {
                return true;
            }
        }
        return false;
    }

    public String remoteProvidersScansUploaded() {
        return driver.findElementByClassName("alert-error").getText();
    }

    public boolean isAppTypeDetect() {
        return driver.findElementById("frameworkType").getText().contains("DETECT");
    }

    public boolean isDefectTrackerAttached() {
        if (driver.findElementById("defectTrackerName").isEnabled())
            return true;
        return false;
    }

    public boolean isActionButtonPresent() {
        return driver.findElementById("actionButton1").isDisplayed();
    }

    public boolean isActionButtonClickable() {
        return isClickable("actionButton1");
    }

    public boolean isEditDeletePresent() {
        return driver.findElementById("editApplicationModalButton").isDisplayed();
    }

    public boolean isEditDeleteClickable() {
        return isClickable("editApplicationModalButton");
    }

    public boolean isEditVulnFiltersPresent() {
        return driver.findElementById("editVulnerabilityFiltersButton").isDisplayed();
    }

    public boolean isEditVulnFiltersClickable() {
        return isClickable("editVulnerabilityFiltersButton");
    }

    public boolean isUploadScanPresent() {
        return driver.findElementById("uploadScanModalLink").isDisplayed();
    }

    public boolean isUploadScanClickable() {
        return isClickable("uploadScanModalLink");
    }

    public boolean isAddManualFindingsPresent() {
        return driver.findElementById("addManualFindingModalLink").isDisplayed();
    }

    public boolean isAddManualFindingsClickable() {
        return isClickable("addManualFindingModalLink");
    }

    public boolean isDeleteButtonPresent() {
        return driver.findElementById("deleteLink").isDisplayed();
    }

    public boolean isDeletebuttonClickable() {
        return isClickable("deleteLink");
    }

    public boolean isNameInputPresent() {
        return driver.findElementById("nameInput").isDisplayed();
    }

    public boolean isURLInputPresent() {
        return driver.findElementById("urlInput").isDisplayed();
    }

    public boolean isUniqueIDPresent() {
        return driver.findElementById("uniqueIdInput").isDisplayed();
    }

    public boolean isTeamSelectionPresent() {
        return driver.findElementById("organizationId").isDisplayed();
    }

    public boolean isCritcalityPresent() {
        return driver.findElementById("criticalityId").isDisplayed();
    }

    public boolean isAppTypePresent() {
        return driver.findElementById("frameworkType").isDisplayed();
    }

    public boolean isSourceURLPresent() {
        return driver.findElementById("repositoryUrl").isDisplayed();
    }

    public boolean isSourceFolderPresent() {
        return driver.findElementById("repositoryFolderInput").isDisplayed();
    }

    public boolean isDefectTrackerAddPresent() {
        return driver.findElementById("addDefectTrackerButton").isDisplayed();
    }

    public boolean isDefectTrackerAddClickable() {
        return isClickable("addDefectTrackerButton");
    }

    public boolean isWAFAddButtonPresent() {
        return driver.findElementById("addWafButton").isDisplayed();
    }

    public boolean isWAFAddButtonClickable() {
        return isClickable("addWafButton");
    }

    public boolean isSaveChangesButtonPresent() {
        return driver.findElementById("submitAppModal").isDisplayed();
    }

    public boolean isSaveChangesButtonClickable() {
        return isClickable("submitAppModal");
    }

    public boolean isDynamicRadioPresent() {
        return driver.findElementById("dynamicRadioButton").isDisplayed();
    }

    public boolean isStaticRadioPresent() {
        return driver.findElementById("staticRadioButton").isDisplayed();
    }

    public boolean isCWEInputPresent() {
        return driver.findElementById("txtSearch").isDisplayed();
    }

    public boolean isURLDynamicSearchPresent() {
        return driver.findElementById("urlDynamicSearch").isDisplayed();
    }

    public boolean isURLStaticSearchPresent() {
        return driver.findElementById("urlStaticSearch").isDisplayed();
    }

    public boolean isLineNumberInputPresent() {
        return driver.findElementById("lineNumberInput").isDisplayed();
    }

    public boolean isParameterPresent() {
        return driver.findElementById("parameterInput").isDisplayed();
    }

    public boolean isSeverityPresent() {
        return driver.findElementById("severityInput").isDisplayed();
    }

    public boolean isDescriptionInputPresent() {
        return driver.findElementById("descriptionInput").isDisplayed();
    }

    public boolean isSubmitManualFindingPresent() {
        return driver.findElementById("submit").isDisplayed();
    }

    public boolean isSubmitManualFindingClickable() {
        return isClickable("submit");
    }

    public boolean isManualFindingCloseButtonPresent() {
        return driver.findElementById("closeModalButton").isDisplayed();
    }

    public boolean isManualFindingCloseButtonClickable() {
        return isClickable("closeModalButton");
    }

    public boolean isScanDeleted() {
        return driver.findElementByLinkText("0 Scans").isDisplayed();
    }

}
