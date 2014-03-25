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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class RemoteProvidersIndexPage extends BasePage {
	/*private List<WebElement> name = new ArrayList<WebElement>();
	private List<WebElement> userName = new ArrayList<WebElement>();
	private List<WebElement> apiKey = new ArrayList<WebElement>();
	private List<WebElement> configureButton = new ArrayList<WebElement>();

	private List<WebElement> appName = new ArrayList<WebElement>();
	private List<WebElement> appTeam = new ArrayList<WebElement>();
	private List<WebElement> appApplication = new ArrayList<WebElement>();
	private List<WebElement> appEditLink = new ArrayList<WebElement>();
	private List<WebElement> appImportScanLinks = new ArrayList<WebElement>();
	private List<WebElement> updateAppsLink = new ArrayList<WebElement>();
	private List<WebElement> clearConfig = new ArrayList<WebElement>();
*/
	public RemoteProvidersIndexPage(WebDriver webDriver) {
		super(webDriver);
/*
		for (int i = 1; i <= getNumEdit(); i++) {
			name.add(driver.findElementById("name" + i));
			userName.add(driver.findElementById("username" + i));
			apiKey.add(driver.findElementById("apiKey" + i));
			configureButton.add(driver.findElementById("configure" + i));
			
			if (!driver.findElementById("username"+i).getText().equals("")
					|| !driver.findElementById("apiKey"+i).getText().equals("")) {
				
				updateAppsLink.add(driver.findElementById("updateApps"+i));
				clearConfig.add(driver.findElementById("clearConfig"+i));
				for (int j = 1; j <= getNumRows(); j++) {
					
					appName.add(driver.findElementById("provider" + i + "appid" + j));
					appTeam.add(driver.findElementById("provider" + i + "tfteamname"
							+ j));
					appApplication.add(driver.findElementById("provider" + i + "tfappname" + j));
					appEditLink.add(driver.findElementById("provider" + i + "updateMapping" + j));

					if (driver.findElementById("provider" + i + "tfteamname" + j)
							.getText().equals("")) {

					} else
						appImportScanLinks.add(driver
								.findElementById("provider" + i + "import" + j));

				}
			}
		}*/
	}
	/*-------------- click functions ---------------*/
	public RemoteProvidersIndexPage clickConfigureQualys(){
		driver.findElementById("configure1").click();
		waitForElement(driver.findElementById("remoteProviderEditModal3"));
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickConfigureVeracode(){
		driver.findElementById("configure2").click();
		waitForElement(driver.findElementById("remoteProviderEditModal2"));
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickConfigureWhiteHat(){
		driver.findElementById("configure3").click();
		waitForElement(driver.findElementById("remoteProviderEditModal1"));
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveQualys(){
		driver.findElementById("submitRemoteProviderFormButton3").click();
		waitForInvisibleElement(driver.findElementById("remoteProviderEditModal3"));
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveQualysInvalid(){
		driver.findElementById("submitRemoteProviderFormButton3").click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage closeModal(){
		driver.findElementByClassName("modal-footer").findElement(By.className("btn")).click();
		waitForInvisibleElement(driver.findElementByClassName("modal"));
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveVera(){
		driver.findElementById("submitRemoteProviderFormButton2").click();
        sleep(6000);
		waitForInvisibleElement(driver.findElementById("remoteProviderEditModal3"));
        sleep(6000);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveVeraInvalid(){
		driver.findElementById("submitRemoteProviderFormButton2").click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveWhiteHat(){
		driver.findElementById("submitRemoteProviderFormButton1").click();
		sleep(5000);
		waitForInvisibleElement(driver.findElementById("remoteProviderEditModal3"));
		sleep(2000);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage saveWhiteHatInvalid(){
		driver.findElementById("submitRemoteProviderFormButton1").click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickEditMapping(String appName){
		int ids[] = getAppProviderandAppId(appName);
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return null;
		}
		driver.findElementById("provider"+ids[0]+"updateMapping"+ids[1]).click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickSaveMapping(String appName){
		String id =  getAppMapModalId(appName);
		if(id.equals(""))
			return null;
		driver.findElementById(id).findElement(By.id("submitRemoteProviderFormButton")).click();
		sleep(2000);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickCancelMapping(String appName){
		String id =  getAppMapModalId(appName);
		if(id.equals(""))
			return null;
		driver.findElementById(id).findElement(By.className("modal-footer")).findElements(By.className("btn")).get(0).click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public ApplicationDetailPage clickImportScan(String appName){
		int ids[] = getAppProviderandAppId(appName);
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return null;
		}
		
		driver.findElementById("provider"+ids[0]+"import"+ids[1]).click();
		sleep(8000);
		return new ApplicationDetailPage(driver);
	}
	
	public RemoteProvidersIndexPage clickRemoveWhiteHatConfig(){
		int ids[] = getAppProviderandAppId("Demo Site BE");
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return null;
		}
		driver.findElementById("clearConfig"+ids[0]).click();
		handleAlert();
		return new RemoteProvidersIndexPage(driver);
	}
	
	/*-------------- set functions ---------------*/
	public RemoteProvidersIndexPage setQualysUsername(String user){
		driver.findElementsById("usernameInput").get(0).clear();
		driver.findElementsById("usernameInput").get(0).sendKeys(user);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setQualysPassword(String password){
		driver.findElementsById("passwordInput").get(0).clear();
		driver.findElementsById("passwordInput").get(0).sendKeys(password);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setQualysUS(){
		driver.findElementById("isEuropean1").click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setQualysEU(){
		driver.findElementById("isEuropean2").click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setVeraUsername(String user){
		driver.findElementsById("usernameInput").get(1).clear();
		driver.findElementsById("usernameInput").get(1).sendKeys(user);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setVeraPassword(String password){
		driver.findElementsById("passwordInput").get(1).clear();
		driver.findElementsById("passwordInput").get(1).sendKeys(password);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setWhiteHatAPI(String api){
		driver.findElementById("apiKeyInput").clear();
		driver.findElementById("apiKeyInput").sendKeys(api);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setTeamMapping(String mappingAppName, String teamName){
		int ids[] = getAppProviderandAppId(mappingAppName);
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return null;
		}
		new Select(driver.findElementById("orgSelect"+ids[0]+"-"+ids[1])).selectByVisibleText(teamName);
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage setAppMapping(String mappingAppName, String appName){
		int ids[] = getAppProviderandAppId(mappingAppName);
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return null;
		}
		new Select(driver.findElementById("appSelect"+ids[0]+"-"+ids[1])).selectByVisibleText(appName);
		return new RemoteProvidersIndexPage(driver);
	}
	/*-------------- get functions ---------------*/
	public int[] getAppProviderandAppId(String appName){
		//ids[0] = provider id, ids[1] = appid


        int[] ids = {-1,-1};
        /*
		String id = "";
		String pattern = "^provider([0-9]+)appid([0-9]+)$";
		List<WebElement> tableData = driver.findElements(By.tagName("td"));
		for(int i = 0; i<tableData.size(); i++){
			if(tableData.get(i).getText().equals(appName)){
				id = tableData.get(i).getAttribute("id");
				break;
			}
		}
		if(!id.equals("")){
			Pattern p = Pattern.compile(pattern);
			Matcher m = p.matcher(id);
			if(m.find()){
				ids[0] = Integer.parseInt(m.group(1));
				ids[1] = Integer.parseInt(m.group(2));
			}
			
		}
        */
        //for tesing purposes Thomas is removing the regex stuff and hard coding
        //the ids to be set to 1
        ids[0] = 1;
        ids[1] = 1;

		return ids;
	}
	
	public String getAppMapModalId(String appName){
		int ids[] = getAppProviderandAppId(appName);
		if(ids[0] == -1 ||  ids[1] == -1 ){
			return "";
		}
		String pattern = "#(remoteProviderApplicationMappingModal[0-9]+)$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(driver.findElementById("provider"+ids[0]+"updateMapping"+ids[1]).getAttribute("href"));
		if(m.find())
			return m.group(1);
		
		return "";
	}
	
	/*-------------- action functions ---------------*/
	public RemoteProvidersIndexPage mapWhiteHatToTeamAndApp(int appRow, String teamName, String appName){
		clickEditWhiteHatButton(appRow);
		selectWhiteHatTeamMapping(teamName, appRow);
		selectWhiteHatAppMapping(appName, appRow);
		clickUpdateMappings();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public RemoteProvidersIndexPage clickEditWhiteHatButton(int row){
		driver.findElementById("provider1updateMapping" + row).click();
		return new RemoteProvidersIndexPage(driver);
	}
	
	public UserIndexPage selectWhiteHatTeamMapping(String teamName, int appRow){
		WebElement a = driver.findElementById("orgSelect1-" + appRow);
		new Select(a).selectByVisibleText(teamName);
		return new UserIndexPage(driver);
	}

	public UserIndexPage selectWhiteHatAppMapping(String appName, int appRow){
		WebElement a = driver.findElementById("appSelect1-" + appRow);
		new Select(a).selectByVisibleText(appName);
		return new UserIndexPage(driver);
	}

    public ApplicationDetailPage clickWhiteHatImportScan(int appRow) {
        driver.findElementById("provider1import" + appRow).click();
        return new ApplicationDetailPage(driver);
    }

    public RemoteProvidersIndexPage mapVeracodeToTeamAndApp(int appRow, String teamName, String appName) {
        clickVeraCodeEditMappingButton(appRow);
        selectVeracodeTeamMapping(teamName, appRow);
        selectVeracodeAppMapping(appName, appRow);
        clickUpdateMappings();
        sleep(6000);
        return new RemoteProvidersIndexPage(driver);
    }

    public RemoteProvidersIndexPage clickVeraCodeEditMappingButton(int appRow) {
        driver.findElementById("provider2updateMapping" + appRow).click();
        return new RemoteProvidersIndexPage(driver);
    }

    public RemoteProvidersIndexPage selectVeracodeTeamMapping(String teamName, int appRow) {
        WebElement teamSelect = driver.findElementById("orgSelect2-" + appRow);
        new Select(teamSelect).selectByVisibleText(teamName);
        return new RemoteProvidersIndexPage(driver);
    }

    public RemoteProvidersIndexPage selectVeracodeAppMapping(String appName, int appRow) {
        WebElement appSelect = driver.findElementById("appSelect2-" + appRow);
        new Select(appSelect).selectByVisibleText(appName);
        return new RemoteProvidersIndexPage(driver);
    }

    public RemoteProvidersIndexPage clickUpdateMappings(){
        //driver.findElementById("submitRemoteProviderFormButton").click();
        driver.findElementByLinkText("Update Application").click();
        return new RemoteProvidersIndexPage(driver);
    }

    public ApplicationDetailPage clickVeracodeImportScan(int appRow) {
        driver.findElementById("provider2import" + appRow).click();
        return new ApplicationDetailPage(driver);
    }

	public String getErrorMessage(){
		return driver.findElementByClassName("alert-error").getText();
	}
	
	public RemoteProvidersIndexPage clearWhiteHat(){
		driver.findElementById("clearConfig1").click();
		handleAlert();
		return new RemoteProvidersIndexPage(driver);
	}

    public RemoteProvidersIndexPage clearVeraCode() {
        driver.findElementById("clearConfig2").click();
        handleAlert();
        return new RemoteProvidersIndexPage(driver);
    }
	
	public String successAlert(){
        waitForElement(driver.findElementByClassName("alert-success"));
		return driver.findElementByClassName("alert-success").getText().trim();
	}
	
//old methods
/*	
	public int getNumEdit() {
		return driver.findElementsByLinkText("Configure").size();

	}

	public int getNumRows() {
		return driver.findElementsByLinkText("Edit Mapping").size();
	}

	public String getNames(int num) {
		return name.get(num).getText();

	}

	public String getUsernames(int num) {
		return userName.get(num).getText();

	}

	public String getAPIKey(int num) {
		return apiKey.get(num).getText();

	}

	public RemoteProviderCredentialsPage clickConfigure(int Row) {
		configureButton.get(Row).click();
		sleep(1000);
		return new RemoteProviderCredentialsPage(driver);
	}

	public EditMappingPage clickEdit(int Row) {
		appEditLink.get(Row).click();
		sleep(1000);
		return new EditMappingPage(driver);
	}

	public void clickImport(int Row) {
		appImportScanLinks.get(Row).click();
		sleep(1000);
	}

	public void clickUpdate(int Row) {
		//UpdateAppsLink = driver.findElementById("updateApps1");
		updateAppsLink.get(Row).click();
		sleep(1000);
	}

	public RemoteProvidersIndexPage clickClearConfigButton(int rowNumber) {
		clearConfig.get(rowNumber).click();
		
		Alert alert = driver.switchTo().alert();
		alert.accept();
		
		return new RemoteProvidersIndexPage(driver);
	}
*/
}