package dev;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class IpV4ValidatorTest {

	private IpAddressValidator validator;

	@Before
	public void setUp() {
		validator = new IpV4AddressValidator();
	}

	@Test
	public void testValidIpAddresses() {
		assertTrue("First valid address", validator.isValid("0.0.0.0"));
		assertTrue("Last valid address", validator.isValid("255.255.255.255"));
		assertTrue("Localhost", validator.isValid("127.0.0.1"));
		assertTrue("A private address", validator.isValid("10.11.12.240"));
		assertTrue("A public address", validator.isValid("213.13.51.87"));
	}

	@Test
	public void testInvalidIpAddresses() {
		assertFalse("Too few bytes", validator.isValid("1.2.3"));
		assertFalse("Too many bytes", validator.isValid("0.0.0.0.0"));
		assertFalse("Byte too small", validator.isValid("-1.2.3.4"));
		assertFalse("Byte too large", validator.isValid("1.256.3.4"));
		assertFalse("Not decimal number", validator.isValid("0x10.22.33.44"));
	}

	@Test
	public void testLocalhost() {
		assertTrue("Usually localhost", validator.isLocalhost("127.0.0.1"));
		assertTrue("Max localhost", validator.isLocalhost("127.255.255.254"));
		assertFalse("Whole localhost net", validator.isLocalhost("127.0.0.0"));
		assertFalse("Broadcast", validator.isLocalhost("127.255.255.255"));
		assertFalse("Wrong range", validator.isLocalhost("128.0.0.1"));
	}
}
