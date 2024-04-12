package demo;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestCases {
    static ChromeDriver driver;
    static WebDriverWait wait;

    @BeforeTest
    public static void startTest(){
        WebDriverManager.chromedriver().timeout(30).setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    @AfterTest
    public static void endTest(){
        System.out.println("End Test: TestCases");
        driver.quit();
    }

    @Test(description = "Printing the message on the About screen")
    public static void testCase01(){
        wrapper wrap = new wrapper(driver,Duration.ofSeconds(30));
        driver.get("https://www.youtube.com/");
        //Using Hard Assert to check wether the url is correct or not
        Assert.assertEquals(driver.getCurrentUrl(), "https://www.youtube.com/");
        //Scrolling the sidebar till about link is visible
        driver.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.xpath("//a[text()='About']")));
        //Clicking on About hyperlink.
        wrap.advanceClick(By.xpath("//a[text()='About']"));
        //Applied wait to load the page.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("ytabout__content")));
        // Scrolling the page till the message is visible
        driver.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.className("ytabout__content")));
        //Fetching the message from page.
        WebElement message = driver.findElement(By.className("ytabout__content"));
        //Printing the About message
        System.out.println("Message on About Page:-");
        System.out.println(message.getText());
    }

    @Test(description = "Asserting whether Movie is marked as A and comedy/animation or not")
    public static void testCase02(){
        wrapper wrap = new wrapper(driver,Duration.ofSeconds(30));
        SoftAssert soft = new SoftAssert();
        driver.get("https://www.youtube.com/");
        //Clicking on Movies link.
        wrap.advanceClick(By.xpath("//yt-formatted-string[text()='Movies']"));
        //Applided wait to load the page.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Movies']")));
        // clickRight method used for scrolling right in the section.
        wrap.clickRight(By.xpath("//div[@id='right-arrow']/ytd-button-renderer"));
        String a = driver.findElement(By.xpath("//span[contains(text(),'Wolf')]/ancestor::a/following-sibling::ytd-badge-supported-renderer//p[text()='A']")).getText();
        //Asserting whether Movie is marked as A or not
        soft.assertEquals(a,"A","Movie is not Mature."); 
        String text = driver.findElement(By.xpath("//span[contains(text(),'Wolf')]/parent::h3/following::span")).getText();
        //Asserting whether the movie is Comedy or Animation.
        soft.assertTrue(text.equals("Comedy")||text.equals("Animation"),"Movie is not Comedy or Animation");
    }

    @Test(description = "Printing the name of playlist and asserting whether no of tracks is less than or equal to 50")
    public static void testCase03(){
        wrapper wrap = new wrapper(driver, Duration.ofSeconds(30));
        SoftAssert soft = new SoftAssert();
        driver.get("https://www.youtube.com/");
        //Clicking on Music link
        wrap.advanceClick(By.xpath("//yt-formatted-string[text()='Music']"));
        //Applied wait to load the page.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//yt-formatted-string[@id='title'and text()='Music']")));
        // Scrolling till Biggest Hit section is visible
        driver.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.xpath("//span[@id='title' and contains(text(),'Biggest Hits')]")));
        //clickRight method used for scrolling to extreme right in the section
        wrap.clickRight(By.xpath("//div[@id='right-arrow']/ytd-button-renderer"));
        //Fetching and Printing the Playlist name
        System.out.println("Playlist:- " + driver.findElement(By.xpath("//h3[contains(text(),'Bollywood Dance Hitlist')]")).getText());
        //Fetching tracks text
        String text = driver.findElement(By.xpath("//h3[contains(text(),'Bollywood Dance Hitlist')]/following-sibling::p[@id='video-count-text']")).getText();
        //Separating the count and converting it into integer.
        int tracks = Integer.parseInt(text.replaceAll("\\D+",""));
        //Applied Soft Assert to check whether no of tracks listed is less than or equal to 50.
        soft.assertTrue(tracks<=50, "Tracks is less than 50");
    }

    @Test(description = "Printing the titles of 1st three news post and sum of likes")
    public static void testCase04(){
        // startTest();
        wrapper wrap = new wrapper(driver, Duration.ofSeconds(30));
        driver.get("https://www.youtube.com/");
        //Applied wait to load the News link
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@title='News']")));
        //Locating the News link
        WebElement news = driver.findElement(By.xpath("//a[@title='News']"));
        //Scrolling the sidebar till the News link is visible.
        driver.executeScript("arguments[0].scrollIntoView(true)", news);
        //Using Wrapper to click on News link.
        wrap.advanceClick(By.xpath("//a[@title='News']"));
        //Waiting until the page loads.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='News']")));
        //Scrolling till the Latest posts are visible.
        driver.executeScript("arguments[0].scrollIntoView(true)", driver.findElement(By.xpath("//span[text()='Show more']")));
        //Fetching the Latest posts
        List<WebElement> titles = driver.findElements(By.xpath("//yt-formatted-string[@id='home-content-text']"));
        //Fetching the first three post's Titles.
        for(int i=0;i<3;i++){
            String title = titles.get(i).getText();
            System.out.print("Title of "+ (i+1) + " post:- ");
            System.out.println(title);
        }
        //Fetching the likes of posts.
        List<WebElement> likes = driver.findElements(By.xpath("//ytd-toggle-button-renderer[@id='like-button']/following-sibling::span[@id='vote-count-middle']"));
        //likesSum method used for fetching the sum of first 3 post's likes
        int sum = wrap.likesSum(likes);
        System.out.println("Sum of 1st three post likes:- " + sum);
    }

    @Test(description = "Printing the sum of views",dataProvider = "search",dataProviderClass = DP.class)
    public static void testCase05(String searchString){
        wrapper wrap = new wrapper(driver, Duration.ofSeconds(30));
        driver.get("https://www.youtube.com/");
        //Used wrapper to send keys to search textbox
        wrap.advanceSendKeys(By.xpath("//input[@placeholder='Search']"), searchString);
        //Waiting till the page loads.
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Filters']")));
        //Fetching the views count from videos.
        List<WebElement> views = driver.findElements(By.xpath("//div[@id='metadata-line']/span[1]"));
        //scroll method used for scrolling the page till the count reaches 10 crore.
        wrap.scroll(views);
    }
}