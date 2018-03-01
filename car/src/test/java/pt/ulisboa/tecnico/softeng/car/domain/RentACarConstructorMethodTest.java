package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class RentACarConstructorMethodTest 
    extends TestCase
{
	private static final String RENTACAR_NAME = "CompanyName";
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RentACarConstructorMethodTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(RentACarConstructorMethodTest.class);
    }
    
    @Test
    public void success() {
		RentACar rentacar = new RentACar(RENTACAR_NAME);

		Assert.assertEquals(RENTACAR_NAME, rentacar.getName());
		Assert.assertEquals(1, RentACar.rentacars.size());
	}

	@Test(expected = RentACarException.class)
	public void nullName() {
		new RentACar(null);
	}

	@Test(expected = RentACarException.class)
	public void emptyName() {
		new RentACar("");
	}
	
	@After
	public void tearDown() {
		RentACar.rentacars.clear();
	}

}
