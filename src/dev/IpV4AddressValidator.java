package dev;

public class IpV4AddressValidator implements IpAddressValidator {

	private static final String SPLIT_PARTS_PATTERN = "\\.";

	@Override
	public boolean isValid(String addressToCheck) {
		String[] parts = splitInParts(addressToCheck);
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

	@Override
	public boolean isLocalhost(String addressToCheck) {
		if (isValid(addressToCheck)) {
			long addressValue = toLong(addressToCheck);
			long minLocalhost = 256L * 256 * 256 * 127 + 1;
			long maxLocalhost = 256L * 256 * 256 * 128 - 2;
			return ((addressValue >= minLocalhost) && (addressValue <= maxLocalhost));
		}
		return false;
	}

	private long toLong(String addressToCheck) {
		String[] parts = splitInParts(addressToCheck);
		return 256L * 256 * 256 * Integer.parseInt(parts[0]) //
				+ 256L * 256L * Integer.parseInt(parts[1]) //
				+ 256L * Integer.parseInt(parts[2]) //
				+ Integer.parseInt(parts[3]);
	}

	private String[] splitInParts(String addressToCheck) {
		return addressToCheck.split(SPLIT_PARTS_PATTERN);
	}
}
