package org.molgenis.selenium.model.mappingservice;

import static java.util.Arrays.asList;

import java.util.List;
import java.util.NoSuchElementException;

import org.molgenis.selenium.model.MenuModel;
import org.molgenis.selenium.model.component.Select2Model;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class TagWizardModel extends MenuModel
{
	private static final Logger LOG = LoggerFactory.getLogger(TagWizardModel.class);

	private Select2Model tagSelectionModel;
	private Select2Model ontologySelectionModel;
	private Select2Model entitySelectionModel;

	@FindBy(css = ".alert-success button.close")
	private WebElement closeSuccessAlert;

	@FindBy(id = "automatic-tag-btn")
	private WebElement automatedTaggingButton;

	@FindBy(id = "clear-all-tags-btn")
	private WebElement clearTagsButton;

	@FindBy(xpath = "//div[@class='bootbox-body' and text() = 'Are you sure you want to remove all tags?']/../../div[@class='modal-footer']/button[text()='OK']")
	private WebElement okButton;

	@FindBy(id = "save-tag-selection-btn")
	private WebElement saveTagSelectionButton;

	@FindBy(xpath = "//table[@id='tag-mapping-table']/tbody/tr")
	List<WebElement> attributeTableRows;

	public TagWizardModel(WebDriver driver)
	{
		super(driver);
		tenSecondWait = new WebDriverWait(driver, 10);
		tagSelectionModel = new Select2Model(driver, "tag-dropdown", true);
		ontologySelectionModel = new Select2Model(driver, "ontology-select", true);
		entitySelectionModel = new Select2Model(driver, "select-target", false);
	}

	public TagWizardModel selectEntity(String name)
	{
		LOG.info("select entity {}...", name);
		entitySelectionModel.select(name);
		Predicate<WebDriver> blah = d -> name.equals(getSelectedEntity());
		tenSecondWait.until(blah);
		return this;
	}

	public String getSelectedEntity()
	{
		return entitySelectionModel.getSelectedLabels().get(0);
	}

	public List<List<String>> getAttributeTags()
	{
		return Lists.transform(getTableData(attributeTableRows), row -> asList(row.get(0), row.get(3)));
	}

	public TagWizardModel clearTags()
	{
		LOG.info("clear tags...");
		clearTagsButton.click();
		okButton.click();
		closeSuccessAlert.click();
		return this;
	}

	public TagWizardModel doAutomatedTagging()
	{
		LOG.info("do automated tagging...");
		automatedTaggingButton.click();
		closeSuccessAlert.click();
		return this;
	}

	public TagWizardModel tagAttributeWithTerms(String attributeName, String... terms)
	{
		LOG.info("tag attribute {} with terms {}", attributeName, asList(terms));
		int rowIndex = findTableRowIndexForAttribute(attributeName);
		WebElement editButton = driver
				.findElement(By.xpath("//table[@id='tag-mapping-table']/tbody/tr[" + rowIndex + "]/td[3]/button"));
		editButton.click();
		tagSelectionModel.select(terms);
		saveTagSelectionButton.click();
		waitForSpinner();
		return this;
	}

	private int findTableRowIndexForAttribute(String attributeName)
	{
		List<List<String>> data = getAttributeTags();
		for (int i = 0; i < data.size(); i++)
		{
			if (attributeName.equals(data.get(i).get(0).split("\n")[0].trim()))
			{
				// allow for the extra header row
				return i + 1;
			}
		}
		throw new NoSuchElementException(attributeName);
	}

	public TagWizardModel selectOntologies(String... ontologies)
	{
		LOG.info("select ontologies {}", asList(ontologies));
		ontologySelectionModel.clearSelection();
		ontologySelectionModel.select(ontologies);
		return this;
	}

}
