import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception{
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("deviceName","AndroidTestDevice");
        capabilities.setCapability("platformVersion","8.0");
        capabilities.setCapability("automationName","Appium");
        capabilities.setCapability("appPackage","org.wikipedia");
        capabilities.setCapability("appActivity",".main.MainActivity");
        capabilities.setCapability("app","C:\\Users\\user\\Automation\\apks\\org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown(){
        driver.quit();
    }

   @Test
   public void  a(){
        System.out.println("aaa");
   }
    @Test
    public void firstTest(){

        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementPresent(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                15
        );

    }

    @Test
    public void testCancelSearch(){

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_container"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/search_src_text"),
                "Cannot find search field",
                5
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/search_close_btn"),
                "Cannot find X to cancel search",
                5
        );

        waitForElementNotPresent(
            By.id("org.wikipedia:id/search_close_btn"),
            "X is still presents on the page",
                5
        );
    }

    @Test
    public void testCompareArticleTitle(){
        waitForElementAndClick(
                By.xpath("//*[contains(@text,'Search Wikipedia')]"),
                "Cannot find Search Wikipedia input",
                5
        );

        waitForElementAndSendKeys(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Java",
                "Cannot find search input",
                5
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Object-oriented programming language']"),
                "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
                5
        );

        WebElement title_element = waitForElementPresent(
                By.id("org.wikipedia:id/view_page_title_text"),
                "Cannot find article title",
                15
        );

        String article_title = title_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected title",
                "Java (programming language)",
                article_title
        );

    }

   @Test
   public void searchFieldHasExpectedText(){

        waitForElementAndClick(

           By.xpath("//*[contains(@text,'Search Wikipedia')]"),
           "Cannot find Search Wikipedia input",
           5
        );

        WebElement search_element = waitForElementPresent(
                By.xpath("//*[contains(@text,'Search…')]"),
                "Cannot find search input",
                5);

        String search_text = search_element.getAttribute("text");

        Assert.assertEquals(
                "We see unexpected text",
                "Search…",
                search_text
        );


   }

   @Test
    public void testCancelSearchingOfArticles(){

        waitForElementAndClick(
               By.id("org.wikipedia:id/search_container"),
               "Cannot find Search Wikipedia input",
               5
       );

       waitForElementAndSendKeys(
               By.xpath("//*[contains(@text,'Search…')]"),
               "Java",
               "Cannot find search input",
               5
       );

       waitForElementPresent(
               By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Island of Indonesia']"),
               "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
               5
       );

       waitForElementPresent(
               By.xpath("//*[@resource-id='org.wikipedia:id/page_list_item_container']//*[@text='Programming language']"),
               "Cannot find 'Object-oriented programming language' topic searching by 'Java'",
               5
       );

       waitForElementAndClick(
         By.id("org.wikipedia:id/search_close_btn"),
         "There is no cross button",
         5
       );

   }

   @Test
   public void allArticleContainSearchValue(){

        String search_value = "Java";

        //Initialize search
        waitForElementAndClick(
               By.id("org.wikipedia:id/search_container"),
               "Cannot find Search Wikipedia input",
               5
       );

        //Search by "Java"
       waitForElementAndSendKeys(
               By.xpath("//*[contains(@text,'Search…')]"),
               search_value,
               "Cannot find search input",
               5
       );

       //Check that the list of search results is presented
       WebElement result = waitForElementPresent(
            By.id("org.wikipedia:id/search_results_list"),
            "No results",
            5
       );

       List result_list = result.findElements(By.id("org.wikipedia:id/page_list_item_title"));

       for (int i = 0; i < result_list.size(); i++){
           WebElement article = (WebElement) result_list.get(i);
           Assert.assertTrue(
                   "Does not contains " + search_value,
                   article.getAttribute("text").contains(search_value)
           );
       }


   }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(by)
        );
    }

    private WebElement waitForElementPresent(By by, String error_message){
        return waitForElementPresent(by, error_message, 5);
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeputInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeputInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeputInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeputInSeconds);
        element.sendKeys(value);;
        return element;
    }


    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds){
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(
                ExpectedConditions.invisibilityOfElementLocated(by)
        );
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds){
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }
}
