package dev;

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
			long addressValue = toLong(addressToCheck);
			long minLocalhost = toLong("127.0.0.1");
			long maxLocalhost = toLong("127.255.255.254");
			return ((addressValue >= minLocalhost) && (addressValue <= maxLocalhost));
		}
		return false;
	}

	private boolean isIntegerRepresentation(String str) {
		try {
			long value = Long.parseLong(str, 10);
			return value >= 0 && value < exp(256, 4);
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	private long exp(int value, int exponent) {
		long result = 1L;
		for (int i = 0; i < exponent; ++i) {
			result *= value;
		}
		return result;
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

	private long toLong(String addressToCheck) {
		String[] parts = splitInParts(addressToCheck);
		return Integer.parseInt(parts[0]) * exp(256, 3) //
				+ Integer.parseInt(parts[1]) * exp(256, 2) //
				+ Integer.parseInt(parts[2]) * exp(256, 1) //
				+ Integer.parseInt(parts[3]);
	}

	private String[] splitInParts(String addressToCheck) {
		return addressToCheck.split(SPLIT_PARTS_PATTERN);
	}
}
