package dev;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class IpV6ValidatorTest {

	private IpAddressValidator validator;

	@Before
	public void setUp() {
		validator = new IpV6Validator();
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
	}
}
