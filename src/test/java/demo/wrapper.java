package demo;

import java.time.Duration;  
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class wrapper {
    private WebDriver driver;
    private WebDriverWait wait;

    public wrapper(WebDriver driver,Duration timeout){
        this.driver=driver;
        wait = new WebDriverWait(driver, timeout);
    }

    public boolean advanceSendKeys(By locator,String keysToSend){
        try{
            WebElement elem = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            elem.clear();
            elem.sendKeys(keysToSend);
            elem.sendKeys(Keys.ENTER);
            return true;
        }
        catch(Exception e){
            System.out.println("Element is not Present");
            return false;
        }
    }

    public boolean advanceClick(By locator){
        try{
            WebElement elem = wait.until(ExpectedConditions.elementToBeClickable(locator));
            elem.click();
            return true;
        }
        catch(Exception e){
            System.out.println("Element is not clickable or not present");
            return false;
        }
    }

    public void clickRight(By locator){
        WebElement rightArrow = driver.findElement(locator);
        while(rightArrow.isDisplayed()){
            rightArrow.click();
        }
    }

    public int likesSum(List<WebElement> likes){
        int sum = 0;
        for(int i=0;i<3;i++){
            String likeText = likes.get(i).getText();
            int like=0;
            if(likeText==""){
                like=0;
            }else{
                like=Integer.parseInt(likeText);
            }
            sum+=like;
        }
        return sum;
    }

    public int scroll(List<WebElement> views){
        JavascriptExecutor js = (JavascriptExecutor)driver;
        int count = 0;
        for(WebElement view:views){
            if(count>=100000000){
                return count;
            }else{
                js.executeScript("arguments[0].scrollIntoView(true)", view);
                String text = view.getText();
                if(text.contains("lakh")){
                    text = text.replace(".", "");
                    String[] splitText = text.split("\\W+");
                    splitText[0]+="00000";
                    count+=Integer.parseInt(splitText[0]);
                }else if(text.contains("K")){
                    text=text.replace(".", "");
                    String[] splitText = text.split("\\W+");
                    splitText[0] = splitText[0].substring(0,splitText[0].length()-1);
                    splitText[0]+="000";
                    count+=Integer.parseInt(splitText[0]);
                }else if(text.contains("crore")){
                    text = text.replace(".", "");
                    String[] splitText = text.split("\\W+");
                    splitText[0]+="0000000";
                    count+=Integer.parseInt(splitText[0]);
                }else if(text.contains("M")){
                    text = text.replace(".", "");
                    String[] splitText = text.split("\\W+");
                    splitText[0] = splitText[0].substring(0,splitText[0].length()-1);
                    splitText[0]+="000000";
                    count+=Integer.parseInt(splitText[0]);
                }
                else if(text.contains("B")){
                    text = text.replace(".", "");
                    String[] splitText = text.split("\\W+");
                    splitText[0] = splitText[0].substring(0,splitText[0].length()-1);
                    splitText[0]+="000000000";
                    count+=Integer.parseInt(splitText[0]);
                }
            }
        }
        return count;
    }
}