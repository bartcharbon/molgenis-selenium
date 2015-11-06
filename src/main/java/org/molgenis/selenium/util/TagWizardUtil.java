package org.molgenis.selenium.util;

import java.util.List;
import java.util.stream.Collectors;

import org.molgenis.selenium.model.MenuModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TagWizardUtil
{
	private static final String MAIN_MENU = "Data Integration";
	private static final String SUB_MENU = "Tag Wizard";

	private static final int BUTTON_CLICK_SLEEP_TIME = 3000;
	private static final int MANNUALLY_TAGGING_TRANSACTION_TIME = 5000;
	private static final int TAGGING_TRANSACTION_TIME = 10000;
	public static final Logger LOG = LoggerFactory.getLogger(TagWizardUtil.class);

	public static void openTagWizard(WebDriver driver) throws InterruptedException
	{
		MenuModel.openPageByClickOnSubMenuItem(MAIN_MENU, SUB_MENU, driver);
	}

	public static String getSelectedEntityName(WebDriver driver) throws InterruptedException
	{
		WebElement findElement = driver.findElement(By
				.xpath("//a[@class='select2-choice']/span[@class='select2-chosen']"));
		return findElement.getText();
	}

	public static void clickAutomaticTaggingButton(WebDriver driver) throws InterruptedException
	{
		WebElement automaticTagButtonElement = driver.findElement(By.id("automatic-tag-btn"));
		automaticTagButtonElement.click();
		Thread.sleep(TAGGING_TRANSACTION_TIME);
	}

	private static void clickClealAllTagsButton(WebDriver driver) throws InterruptedException
	{
		WebElement automaticTagButtonElement = driver.findElement(By.id("clear-all-tags-btn"));
		automaticTagButtonElement.click();
		Thread.sleep(BUTTON_CLICK_SLEEP_TIME);
	}

	public static void clearAllTagsConfirmationModalAndCancel(WebDriver driver) throws InterruptedException
	{
		clickClealAllTagsButton(driver);

		WebElement cancelButton = driver
				.findElement(By
						.xpath("//div[@class='bootbox-body' and text() = 'Are you sure you want to remove all tags?']/../../div[@class='modal-footer']/button[text()='Cancel']"));
		cancelButton.click();
		Thread.sleep(BUTTON_CLICK_SLEEP_TIME);
	}

	public static void clearAllTagsConfirmationModalAndOK(WebDriver driver) throws InterruptedException
	{
		clickClealAllTagsButton(driver);

		WebElement okButton = driver
				.findElement(By
						.xpath("//div[@class='bootbox-body' and text() = 'Are you sure you want to remove all tags?']/../../div[@class='modal-footer']/button[text()='OK']"));
		okButton.click();
		Thread.sleep(TAGGING_TRANSACTION_TIME);
	}

	public static void saveSearchedTags(WebDriver driver) throws InterruptedException
	{
		WebElement saveTagButton = driver.findElement(By.id("save-tag-selection-btn"));
		saveTagButton.click();
		Thread.sleep(MANNUALLY_TAGGING_TRANSACTION_TIME);
	}

	public static void clickOnEditTagButtonByRowIndex(int rowIndex, WebDriver driver) throws InterruptedException
	{
		WebElement editTagButton = driver.findElement(By.xpath("//table[@id='tag-mapping-table']/tbody/tr[" + rowIndex
				+ "]/td[3]/button"));
		editTagButton.click();
		Thread.sleep(BUTTON_CLICK_SLEEP_TIME);
	}

	public static void clickOnTheExistingTagBasedOnName(int rowIndex, String tagName, WebDriver driver)
			throws InterruptedException
	{
		List<WebElement> tags = driver.findElements(By.xpath("//table[@id='tag-mapping-table']/tbody/tr[" + rowIndex
				+ "]/td[2]/button"));
		for (WebElement tag : tags)
		{
			if (tag.getText().trim().equalsIgnoreCase(tagName))
			{
				tag.click();
				Thread.sleep(MANNUALLY_TAGGING_TRANSACTION_TIME);
			}
		}
	}

	public static List<String> getExistingTagNamesByRowIndex(int rowIndex, WebDriver driver)
			throws InterruptedException
	{
		List<String> tagNames = driver
				.findElements(By.xpath("//table[@id='tag-mapping-table']/tbody/tr[" + rowIndex + "]/td[2]/button"))
				.stream().map(WebElement::getText).collect(Collectors.toList());
		return tagNames;
	}
}
