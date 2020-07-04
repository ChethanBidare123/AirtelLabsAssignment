import org.testng.Assert;
import org.testng.annotations.Test;
import superBase.TestBase;
import uiPages.LandingPageStep;

public class TC001_VerifyProductAddedSuccessfully extends TestBase {

    /**
     *  Verify that product is Successfully added
     */
    @Test
    private void verifyProductAddedSuccessfullyToCart(){
        LandingPageStep landingPageStep = new LandingPageStep();
        landingPageStep.moveToPopularTab();
        final String expectedProductName = landingPageStep.addLowestPriceProdAndGetProductName();
        landingPageStep.moveToCart();
        landingPageStep.captureScreenshotRuntime();
        final String actualProductNameInCart = landingPageStep.getItemNameFromCartPage();
        Assert.assertEquals(actualProductNameInCart,expectedProductName);
    }
}
