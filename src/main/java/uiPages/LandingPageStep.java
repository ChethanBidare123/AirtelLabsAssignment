package uiPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import superBase.TestBase;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class LandingPageStep extends TestBase {

    @FindBy(className = "homefeatured")
    private WebElement popularTab;

    @FindBy(xpath = "//div[@class='button-container']/span")
    private WebElement continueShoppingButton;

    @FindBy(xpath = "//a[@title='View my shopping cart']")
    private WebElement cartIcon;

    @FindBy(xpath = "//p[@class='product-name']")
    private WebElement cartItem;

    private static final String PRODUCT_PRICE_XPATH = "//*[@id='homefeatured']/li/div/div[2]/div[1]/span[1]";
    private static final String OLD_PRODUCT_PRICE_XPATH = "//*[@id='homefeatured']/li/div/div[2]/div[1]/span[2]";
    private static final String DISCOUNT_XPATH = "//*[@id='homefeatured']/li/div/div[2]/div[1]/span[3]";
    private static final String ADD_TO_CART_XPATH = "//span[contains(text(),'Add to cart')]";
    private static final String ITEM_NAME_XPATH = "//a[@class='product-name']";

    public LandingPageStep(){
        PageFactory.initElements(driver,this);
    }

    /**
     *  Moves To Popular Tab
     */
    public void moveToPopularTab(){
        popularTab.click();
    }

    /**
     *  Gets the Lowest Product Price
     * @return - double
     */
    private double getLowestPriceItem(){
        List<WebElement> productPrices = driver.findElements(By.xpath(PRODUCT_PRICE_XPATH));
        ArrayList<Double> prodPrices = new ArrayList<Double>();
        for (WebElement webElement : productPrices){
            String price = webElement.getAttribute(Constants.INNER_HTML).trim();
            double productPrice = Double.parseDouble(price.substring(1));
            prodPrices.add(productPrice);
        }
        double lowestPrice = prodPrices.get(0);
        for (double price : prodPrices){
            if (price < lowestPrice){
                lowestPrice = price;
            }
        }
        return lowestPrice;
    }

    /**
     *  Adds the Lowest Priced Product and returns Product Name
     *
     * @return - String
     *
     */
    public String addLowestPriceProdAndGetProductName(){
        final double lowestPricedProduct = getLowestPriceItem();
        int totalCount = driver.findElements(By.xpath(PRODUCT_PRICE_XPATH)).size();
        for (int i=1; i<=totalCount; i++){
           String price = driver.findElements(By.xpath(PRODUCT_PRICE_XPATH)).get(i).getAttribute(Constants.INNER_HTML).trim();
            double prodPrice = Double.parseDouble(price.substring(1));
            if (prodPrice == lowestPricedProduct){
                driver.findElements(By.xpath(ADD_TO_CART_XPATH)).get(i).click();
                 return driver.findElements(By.xpath(ITEM_NAME_XPATH)).get(i).getText();
            }
        }
        return Constants.EMPTY_STRING;
    }

    /**
     *  Clicks On COntinue Shopping
     */
    private void clickOnContinueShopping(){
        waitForElementToBeVisible(continueShoppingButton);
        continueShoppingButton.click();
    }

    /**
     * Moves to Cart
     */
    public void moveToCart(){
        clickOnContinueShopping();
        cartIcon.click();
    }

    /**
     *  Gets Product Name from Cart Page
     *
     * @return - String
     */
    public String getItemNameFromCartPage(){
        return cartItem.getAttribute(Constants.INNER_TEXT).trim();
    }

    /**
     * Gets Numbers of products having Discount
     *
     * @return - integer
     */
    public int getNumberOfItemsHavingDiscount(){
       return driver.findElements(By.xpath(DISCOUNT_XPATH)).size();
    }

    /**
     *  Gets Discount Percentage
     *
     * @param itemNumber - Product Number
     *
     * @return - double
     */
    private double getDiscountPercentage(int itemNumber){
        String disc = driver.findElements(By.xpath(DISCOUNT_XPATH)).get(itemNumber-1).getAttribute(Constants.INNER_HTML).trim();
        disc = disc.replaceAll(Constants.PERCENTAGE_SYMBOL,Constants.EMPTY_STRING);
        return Double.parseDouble(disc.substring(1));
    }

    /**
     *  Gets Old Price
     *
     * @param itemNumber - Item Number
     *
     * @return - double
     */
    private double getOldPrice(int itemNumber){
        String oldPrice = driver.findElements(By.xpath(OLD_PRODUCT_PRICE_XPATH)).get(itemNumber-1).getAttribute(Constants.INNER_HTML).trim();
        return Double.parseDouble(oldPrice.substring(1));
    }

    /**
     * Gets Calculated Product price After Discount
     *
     * @param product - Product
     * @return - double
     */
    public double getCalculatedProductPriceAfterDiscount(int product){
        double discPrice = getOldPrice(product) * getDiscountPercentage(product) / 100;
        BigDecimal bd = BigDecimal.valueOf(getOldPrice(product) - discPrice).setScale(2,RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
