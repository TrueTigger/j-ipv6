package dev;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class IpV6ValidatorTest {

	private IpAddressValidator validator;

	@Before
	public void setUp() {
		validator = new IpV6AddressValidator();
	}

	@Test
	public void testNullIsInvalid() {
		assertFalse("Null invalid", validator.isValid(null));
		assertFalse("Empty string is invalid", validator.isValid(""));
	}

	@Test
	public void testValidLongIpV6Addresses() {
		assertTrue("First valid address",
				validator.isValid("0000:0000:0000:0000:0000:0000:0000:0000"));
		assertTrue("Last valid address",
				validator.isValid("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff"));
	}

	@Test
	public void testInvalidLongIpV6Addresses() {
		assertFalse("Negative numbers invalid",
				validator.isValid("ABBA:DEAD:BEEF:CAFE:BABE:0042:0000:-1"));
		assertFalse("Only hex numbers supported",
				validator.isValid("0000:0000:0000:0000:0000:0000:0000:GCBA"));
		assertFalse("Too few blocks", validator.isValid("0000:0000"));
		assertFalse("Too many blocks", validator.isValid("0:1:2:3:4:5:6:7:8"));
		assertFalse("Single block larger than FFFF",
				validator.isValid("0000:0000:0000:0000:0000:0000:0000:1FFFF"));
	}

	@Test
	public void testValidShortIpV6Addresses() {
		assertTrue("Skip leading zeros", validator.isValid("1:2:3:4:5:6:7:8"));
		assertTrue(":: for missing block", validator.isValid("1:2:3::4:5:6:7"));
		assertTrue(":: is also valid", validator.isValid("::"));
		assertTrue(":: on the end", validator.isValid("::1:2"));
		assertTrue(":: on the start", validator.isValid("1:2:3::"));
		assertTrue(":: with five blocks", validator.isValid("1:2::3:4:5"));
		assertTrue(":: with six blocks", validator.isValid("1:2:3:4:5::6"));
	}

	@Test
	public void testInvalidShortIpV6Addresses() {
		assertFalse(":: only allowed once", validator.isValid("::1::"));
		assertFalse("too much blocks", validator.isValid("1:2:3:4::5:6:7:8"));
	}

	@Test
	public void testLocalHost() {
		assertTrue("ShortVersion", validator.isLocalhost("::1"));
		assertTrue("LongVersion", validator.isLocalhost("0:0:0:0:0:0:0:1"));
		assertTrue("FullVersion", validator //
				.isLocalhost("0000:0000:0000:0000:0000:0000:0000:0001"));
		assertFalse("Smaller than localhost", validator.isLocalhost("::"));
		assertFalse("Larger than localhost", validator.isLocalhost("::2"));
		assertFalse("typical address",
				validator.isLocalhost("2001:db8:32::231:31"));
		assertFalse("Invalid address never localhost",
				validator.isLocalhost(""));
	}

	@Test
	public void testToNumber() {
		assertNull("Invalid inpiut = null", validator.toNumber("192.168.1.1"));
		assertEquals("localhost = 1", BigInteger.ONE, validator.toNumber("::1"));
		assertEquals("Short IPv6 address converted correctly",
				new BigInteger("AFFE0000CAFEBABE0000000000000042", 16),
				validator.toNumber("AFFE:0:CAFE:BABE::42"));
		assertEquals("Max IPv6 address converted correctly",
				new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF", 16),
				validator.toNumber("FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
	}
}
