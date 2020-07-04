package superBase;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class TestBase {


    protected static WebDriver driver ;
    private static WebDriverWait wait ;
    private static String testName ;
    private final Properties OR = new Properties();



    /**
     *  Loads property file
     */
    private void loadFromORproperties() {
        String path = System.getProperty("user.dir")+"//src//main//resources//" ;
        File file = new File(path+"test-selenium.properties");
        try {
            FileInputStream fis = new FileInputStream(file);
            OR.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("File Not found in the specified location");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  Intializes Logs, Property Files, Browser and Driver
     */
    private void init() {
        loadFromORproperties();
        selectBrowser(OR.getProperty("BrowserName"));
        waitForElementToLoad();
        getBaseUrl(OR.getProperty("baseURL"));
    }

    /**
     *  Selects Browser based on User specified in Property file
     *  @param browserName - Browser Name
     */
    private void selectBrowser(final String browserName){
        if(browserName.equalsIgnoreCase("firefox")){
            System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir")+"//Drivers//geckodriver.exe");
            driver = new FirefoxDriver();
            wait = new WebDriverWait(driver, 120);
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(""))));
            driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
            waitForElementToLoad();
        }
        else if(browserName.equalsIgnoreCase("chrome")){
            System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"//Drivers//chromedriver.exe");
            driver = new ChromeDriver();
            wait = new WebDriverWait(driver, 120);
            waitForElementToLoad();

        }
    }

    /**
     * Gets URL from property file
     */
    private void getBaseUrl(String BaseUrl){
        try {
            driver.get(BaseUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void waitForElementToLoad(){
        driver.manage().timeouts().implicitlyWait(60,TimeUnit.SECONDS);
    }

    /**
     *  Waits for Element To Be Visible
     * @param webElement - WebElement
     */
    protected void waitForElementToBeVisible(WebElement webElement){
        wait.until(ExpectedConditions.visibilityOf(webElement));
    }


    /**
     * Selects value from the Auto suggested Dropdown
     * This method will get the List Of web elements By the tag Name and searches each element with the text
     *
     * @param text - text
     */
    public void SelectFromAutoSuggestionSearch(String text){
        List<WebElement> AutoSuggestionItemList = driver.findElements(By.tagName("li"));
        for(WebElement we : AutoSuggestionItemList){
            if(we.getText().contains(text)){
                we.click();
                break ;
            }
        }
    }


    /**
     *  Takes Screenshot during run time
     */
    public void captureScreenshotRuntime(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_YYYY_HH_mm_ss");
        try {
            File srcFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            File file = new File(System.getProperty("user.dir")+"//Output//");
            file.mkdirs();
            String reportDirectory = System.getProperty("user.dir")+"//Output//" + testName  ;
            String destination = reportDirectory  +"_" +formatter.format(calendar.getTime())+ ".png";
            File destFile = new File(destination);
            FileHandler.copy(srcFile, destFile);
        } catch (WebDriverException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @BeforeMethod()
    public void beforeMethod(Method Result) {
        testName = Result.getName();
        init();
    }

    @AfterClass(alwaysRun=true)
    public void endTest(){
        try {
            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
