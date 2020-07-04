import org.testng.Assert;
import org.testng.annotations.Test;
import superBase.TestBase;
import uiPages.Constants;
import uiPages.LandingPageStep;

public class TC002_VerifyFinalPriceAfterDiscount extends TestBase {

    /**
     * Check for the number of items which have a discount if available and also verify that the
     * final price after discount is correct or not.
     */
    @Test
    public void verifyFinalPriceAfterDiscount(){
        LandingPageStep landingPageStep = new LandingPageStep();
        landingPageStep.moveToPopularTab();
        int countOfDiscountItems = landingPageStep.getNumberOfItemsHavingDiscount();
        Assert.assertTrue(countOfDiscountItems !=0);
        double expectedPriceForProduct1 = landingPageStep.getCalculatedProductPriceAfterDiscount(1);
        double expectedPriceForProduct2 = landingPageStep.getCalculatedProductPriceAfterDiscount(2);
        Assert.assertEquals(Constants.ACTUAL_PRICE1,expectedPriceForProduct1);
        Assert.assertEquals(Constants.ACTUAL_PRICE,expectedPriceForProduct2);

    }
}
