package dev;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
	public void testNullIsInvalid() {
		assertFalse("Null is invalid", validator.isValid(null));
		assertFalse("Empty string is invalid", validator.isValid(""));
	}

	@Test
	public void testValidDottedDecimalNotation() {
		assertTrue("First valid address", validator.isValid("0.0.0.0"));
		assertTrue("Last valid address", validator.isValid("255.255.255.255"));
		assertTrue("Localhost", validator.isValid("127.0.0.1"));
		assertTrue("A private address", validator.isValid("10.11.12.240"));
		assertTrue("A public address", validator.isValid("213.13.51.87"));
	}

	@Test
	public void testInvalidDottedDecimalNotation() {
		assertFalse("Too few bytes", validator.isValid("1.2.3"));
		assertFalse("Too many bytes", validator.isValid("0.0.0.0.0"));
		assertFalse("Byte too small", validator.isValid("-1.2.3.4"));
		assertFalse("Byte too large", validator.isValid("1.256.3.4"));
		assertFalse("Not decimal number", validator.isValid("0x10.22.33.44"));
	}

	@Test
	public void testValidIntegerNotation() {
		assertTrue("First valid address", validator.isValid("0"));
		assertTrue("Last valid address", validator.isValid("4294967295"));
		assertTrue("192.168.0.1 as integer", validator.isValid("3232235521"));
	}

	@Test
	public void testInvalidIntegerNotation() {
		assertFalse("Too small: <0", validator.isValid("-1"));
		assertFalse("Too large: >=2^32", validator.isValid("4294967296"));
		assertFalse("Fractional numbers not allowed", validator.isValid("1.1"));
	}

	@Test
	public void testLocalhost() {
		assertTrue("Usually localhost", validator.isLocalhost("127.0.0.1"));
		assertTrue("Max localhost", validator.isLocalhost("127.255.255.254"));
		assertFalse("Whole localhost net", validator.isLocalhost("127.0.0.0"));
		assertFalse("Broadcast", validator.isLocalhost("127.255.255.255"));
		assertFalse("Wrong range", validator.isLocalhost("128.0.0.1"));
		assertFalse("Invalid address", validator.isLocalhost(null));
	}

	@Test
	public void testToNumber() {
		assertNull("Invalid ip returns null", validator.toNumber(null));
		assertEquals("Dotted decimal notation correct converted", 3232235521L, //
				validator.toNumber("192.168.0.1").longValue());
		assertEquals("Integer notation correct converted", 3232235521L, //
				validator.toNumber("3232235521").longValue());
	}
}
