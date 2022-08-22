package com.math;

/**
 * Class that performs base62 math
 */
public class Base62 {

  public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  public static final int BASE = ALPHABET.length();

  /**
   * Converts an int from base10 to base62
   * @param pBase10Number number to be converted
   * @return base62 in string format
   */
  public static String fromBase10(int pBase10Number) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (pBase10Number == 0) {
      return "a";
    }
    while (pBase10Number > 0) {
      pBase10Number = fromBase10(pBase10Number, stringBuilder);
    }
    return stringBuilder.reverse().toString();
  }

  /**
   * Converts an int from base10 to base62.
   * Passes in a string builder to append base62 characters
   * Returns the resulting int from dividing by 62
   * @param pBase10Number number to be converted
   * @param pStringBuilder base62 string being built
   * @return result of base10 number / 62
   */
  private static int fromBase10(int pBase10Number, final StringBuilder pStringBuilder) {
    int remainder = pBase10Number % BASE;
    pStringBuilder.append(ALPHABET.charAt(remainder));
    return pBase10Number / BASE;
  }

  /**
   * Converts base62 string back to base 10 int
   * @param pBase62String base62 string
   * @return base10 int
   */
  public static int toBase10(String pBase62String) {
    return toBase10(new StringBuilder(pBase62String).reverse().toString().toCharArray());
  }

  /**
   * Converts base62 chars to int
   * @param pBase62Chars array of base62 chars
   * @return base10 int
   */
  private static int toBase10(char[] pBase62Chars) {
    int base62IntForConversion = 0;
    for (int index = pBase62Chars.length - 1; index >= 0; index--) {
      base62IntForConversion += toBase10(ALPHABET.indexOf(pBase62Chars[index]), index);
    }
    return base62IntForConversion;
  }

  /**
   * Math to work out base10 int based on integer passed in
   * @param pIndex base62 index
   * @param pPowerOf int to raise to the power of
   * @return base10 integer
   */
  private static int toBase10(int pIndex, int pPowerOf) {
    return pIndex * (int) Math.pow(BASE, pPowerOf);
  }
}
