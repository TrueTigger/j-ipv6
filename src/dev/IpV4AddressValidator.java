package dev;

import java.math.BigInteger;

public class IpV4AddressValidator implements IpAddressValidator {

	private static final String SPLIT_PARTS_PATTERN = "\\.";

	@Override
	public boolean isValid(String addressToCheck) {
		return (addressToCheck != null)
				&& (isIntegerRepresentation(addressToCheck) || isDottedDecimalRepresentation(addressToCheck));
	}

	@Override
	public boolean isLocalhost(String addressToCheck) {
		if (isValid(addressToCheck)) {
			long addressValue = toNumber(addressToCheck).longValue();
			long minLocalhost = toNumber("127.0.0.1").longValue();
			long maxLocalhost = toNumber("127.255.255.254").longValue();
			return ((addressValue >= minLocalhost) && (addressValue <= maxLocalhost));
		}
		return false;
	}

	@Override
	public BigInteger toNumber(String address) {
		if (isValid(address)) {
			if (isIntegerRepresentation(address)) {
				return new BigInteger(address);
			}
			String[] parts = splitInParts(address);
			return new BigInteger(Long.toString( //
					Integer.parseInt(parts[0]) * exp(256, 3) //
					+ Integer.parseInt(parts[1]) * exp(256, 2) //
					+ Integer.parseInt(parts[2]) * exp(256, 1) //
					+ Integer.parseInt(parts[3])));
		}
		return null;
	}
	
	private boolean isIntegerRepresentation(String str) {
		try {
			long value = Long.parseLong(str, 10);
			return value >= 0 && value < exp(256, 4);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}


	private boolean isDottedDecimalRepresentation(String str) {
		String[] parts = splitInParts(str);
		if (parts.length != 4) {
			return false;
		}
		for (String part : parts) {
			try {
				int value = Integer.parseInt(part, 10);
				if ((value < 0) || (value > 255)) {
					return false;
				}
			} catch (NumberFormatException nfe) {
				return false;
			}
		}
		return true;
	}

	private String[] splitInParts(String addressToCheck) {
		return addressToCheck.split(SPLIT_PARTS_PATTERN);
	}
	
	private long exp(int value, int exponent) {
		long result = 1L;
		for (int i = 0; i < exponent; ++i) {
			result *= value;
		}
		return result;
	}
}
