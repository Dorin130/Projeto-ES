package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class BrokerConstructorMethodTest {

	@Test
	public void success() {
		Broker broker = new Broker("BR01", "WeExplore", "123456789", "987654321");

		Assert.assertEquals("BR01", broker.getCode());
		Assert.assertEquals("WeExplore", broker.getName());
		Assert.assertEquals("123456789", broker.getBuyerNIF());
		Assert.assertEquals("987654321", broker.getSellerNIF());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, "WeExplore","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", "WeExplore","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", "WeExplore","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker("BR01", "WeExplore","123456789", "987654321");

		try {
			new Broker("BR01", "WeExploreX","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker("BR01", null,"123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker("BR01", "","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker("BR01", "    ","123456789", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void nullBuyerNif() {
		try {
			new Broker("BR01", "WeExploreX",null, "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyBuyerNif() {
		try {
			new Broker("BR01", "WeExploreX","", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void invalidBuyerNif() {
		try {
			new Broker("BR01", "    ","1234567AB", "987654321");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}


	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
