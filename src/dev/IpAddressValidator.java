package dev;

import java.math.BigInteger;

interface IpAddressValidator {

	boolean isValid(String testInput);

	boolean isLocalhost(String string);

	BigInteger toNumber(String address);
}
