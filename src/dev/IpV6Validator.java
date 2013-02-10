package dev;

import java.math.BigInteger;

public class IpV6Validator implements IpAddressValidator {

	private static final String BLOCK_SEPARATOR = ":";

	@Override
	public boolean isValid(String addressToCheck) {
		return normalize(addressToCheck) != null;
	}

	@Override
	public boolean isLocalhost(String addressToCheck) {
		return isValid(addressToCheck)
				&& toNumber(addressToCheck).equals(toNumber("::1"));
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

	private BigInteger toNumber(String address) {
		if (isValid(address)) {
			BigInteger number = new BigInteger("0");
			BigInteger multiplicator = new BigInteger("1");
			BigInteger ffff = new BigInteger("ffff", 16);
			String blocks[] = replaceEmptyBlocks(address).split(BLOCK_SEPARATOR);
			for (int i = 7; i >= 0; --i) {
				BigInteger value = new BigInteger(blocks[i], 16);
				number = number.add(value.multiply(multiplicator));
				multiplicator = multiplicator.multiply(ffff);
			}
			return number;
		}
		return null;
	}

	private boolean validHexadecimalNotation(String input) {
		String address = replaceEmptyBlocks(input);
		String blocks[] = address.split(BLOCK_SEPARATOR);
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

	private String replaceEmptyBlocks(String input) {
		String emptyBlockMarker = BLOCK_SEPARATOR + BLOCK_SEPARATOR;
		if (input.contains(emptyBlockMarker)) {
			String replacement = "";
			switch (countSeparators(input)) {
			case 2:
				replacement = ":0:0:0:0:0:0:";
				break;
			case 3:
				replacement = ":0:0:0:0:0:";
				break;
			case 4:
				replacement = ":0:0:0:0:";
				break;
			case 5:
				replacement = ":0:0:0:";
				break;
			case 6:
				replacement = ":0:0:";
				break;
			default:
				replacement = ":0:";
			}
			String result = input.replace(emptyBlockMarker, replacement);
			if (result.startsWith(":")) {
				result = "0" + result;
			}
			if (result.endsWith(":")) {
				result += "0";
			}
			return result;
		}
		return input;
	}

	private int countSeparators(String input) {
		int cnt = 0;
		for (int i = 0; i < input.length(); ++i) {
			String substr = input.substring(i, i + 1);
			if (BLOCK_SEPARATOR.equals(substr)) {
				++cnt;
			}
		}
		return cnt;
	}
}
