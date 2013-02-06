package dev;

public class IpV6Validator implements IpAddressValidator {

	private static final String SPLIT_PARTS_PATTERN = ":";

	@Override
	public boolean isValid(String addressToCheck) {
		return normalize(addressToCheck) != null;
	}

	@Override
	public boolean isLocalhost(String addressToCheck) {
		return isValid(addressToCheck);
	}

	public String normalize(String input) {
		String output = input;
		if (output != null) {
			if (!validHexadecimalNotation(output)) {
				output = null;
			}
		}
		return output;
	}

	private boolean validHexadecimalNotation(String output) {
		String blocks[] = output.split(SPLIT_PARTS_PATTERN);
		if (blocks.length != 8) {
			return false;
		}
		for (String block : blocks) {
			try {
				int value = Integer.parseInt(block, 16);
				if ((value < 0) || (value > 65535)) {
					return false;
				}
			} catch (NumberFormatException nfe) {
				return false;
			}
		}
		return true;
	}
}
