package org.molgenis.selenium.test;

import org.molgenis.DriverType;
import org.molgenis.JenkinsConfig;
import org.molgenis.data.rest.client.MolgenisClient;
import org.molgenis.selenium.model.mappingservice.AttributeMappingScreenModel;
import org.molgenis.selenium.model.mappingservice.MappingProjectAddSourceDataModel;
import org.molgenis.selenium.model.mappingservice.MappingProjectImportDataModel;
import org.molgenis.selenium.model.mappingservice.MappingProjectOverviewModel;
import org.molgenis.selenium.model.mappingservice.TagWizardScreenModel;
import org.molgenis.selenium.util.RestApiV1Util;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@ContextConfiguration(classes = JenkinsConfig.class)
public class MappingServiceAppModelTest extends AbstractTestNGSpringContextTests
{
	private static final Logger LOG = LoggerFactory.getLogger(MappingServiceAppModelTest.class);

	private WebDriver driver;
	private AttributeMappingScreenModel attributeMappingScreenModel;
	private MappingProjectAddSourceDataModel mappingProjectAddSourceDataModel;
	private MappingProjectImportDataModel mappingProjectImportDataModel;
	private MappingProjectOverviewModel mappingProjectOverviewModel;
	private TagWizardScreenModel tagWizardScreenModel;

	@Value("${test.baseurl}")
	private String baseURL;

	@Value("${test.uid}")
	private String uid;

	@Value("${test.pwd}")
	private String pwd;

	@BeforeClass
	public void beforeSuite() throws InterruptedException
	{
		MolgenisClient molgenisClient = RestApiV1Util.createMolgenisClientApiV1(baseURL, LOG);

		this.driver = DriverType.FIREFOX.getDriver();
		this.attributeMappingScreenModel = new AttributeMappingScreenModel(driver, molgenisClient);
		this.mappingProjectAddSourceDataModel = new MappingProjectAddSourceDataModel(driver, molgenisClient);
		this.mappingProjectImportDataModel = new MappingProjectImportDataModel(driver, molgenisClient);
		this.mappingProjectOverviewModel = new MappingProjectOverviewModel(driver, molgenisClient);
		this.tagWizardScreenModel = new TagWizardScreenModel(driver);
	}

	@Test
	public void test() throws InterruptedException
	{
		LOG.info("Delete all test data sets.");
		this.deleteAllTestDataSetsFromTheApp();

		LOG.info("Sign in.");
		SignUtil.signIn(driver, baseURL, uid, pwd);

		LOG.info("Import mapping service test data.");
		this.importMappingServiceTestData();

		LOG.info("Test tag wizard.");
		this.testTagWizard();

		LOG.info("Test add one project.");
		this.testAddOneProject();

		LOG.info("Test Add Lifelines Source to mapping project.");
		this.testAddLifeLinesSourceToMappingProject();

		LOG.info("Test basic functionalities in attribute mapping screen.");
		this.testBasicFunctionalitiesInAttributeMappingScreen();

		LOG.info("Test integrate data for Lifelines.");
		this.testIntegrateDataForLifeLines();

		LOG.info("Sign out.");
		SignUtil.signOut(driver);

		LOG.info("Delete all data sets.");
		this.deleteAllTestDataSetsFromTheApp();
	}

	public void deleteAllTestDataSetsFromTheApp() throws InterruptedException
	{
		mappingProjectImportDataModel.deleteMappingServiceTestData(uid, pwd);

	}

	public void importMappingServiceTestData() throws InterruptedException
	{
		mappingProjectImportDataModel.importMappingServiceTestData();
	}

	public void testTagWizard() throws InterruptedException
	{
		tagWizardScreenModel.testAllTagFunctionalities();
	}

	public void testAddOneProject() throws InterruptedException
	{
		mappingProjectOverviewModel.addOneMappingProject();

		mappingProjectOverviewModel.cancelAddMappingProject();

		mappingProjectOverviewModel.addOneMappingProjectWithoutName();

		mappingProjectOverviewModel.removeTestMappingProject();

		mappingProjectOverviewModel.addOneMappingProject();
	}

	public void testAddLifeLinesSourceToMappingProject() throws InterruptedException
	{
		mappingProjectOverviewModel.openOneMappingProject();

		mappingProjectAddSourceDataModel.cancelAddLifeLinesSourceToMappingProject();

		mappingProjectAddSourceDataModel.addLifeLinesSourceToMappingProject();

		mappingProjectAddSourceDataModel.cancelRemoveLifeLinesSourceToMappingProject();

		mappingProjectAddSourceDataModel.removeLifeLinesSourceToMappingProject();

		mappingProjectAddSourceDataModel.addLifeLinesSourceToMappingProject();

		mappingProjectAddSourceDataModel.cancelRemoveFastingGlucoseAttributeForLifeLinesSource();

		mappingProjectAddSourceDataModel.removeFastingGlucoseAttributeForLifeLinesSource();
	}

	public void testBasicFunctionalitiesInAttributeMappingScreen() throws InterruptedException
	{
		mappingProjectOverviewModel.openOneMappingProject();

		attributeMappingScreenModel.clickGenderAttributeForLifeLinesSource();

		attributeMappingScreenModel.clickFastingGlucoseAttributeForLifeLinesSource();
	}

	public void testIntegrateDataForLifeLines() throws InterruptedException
	{
		mappingProjectAddSourceDataModel.integrateSourceData();
	}

	@AfterClass
	public void afterClass() throws InterruptedException
	{
		// Clear cookies
		this.driver.manage().deleteAllCookies();

		// Clear cookies
		this.driver.close();
	}
}
