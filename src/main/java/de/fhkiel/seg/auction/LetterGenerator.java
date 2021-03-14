package de.fhkiel.seg.auction;

import java.util.Random;

/**
 * Letter generator class to gernerte letters to auction.
 * Uses no special distribution
 */
public class LetterGenerator {

  /**
   * Instantiates a new Letter generator.
   */
  public LetterGenerator() {
    currentLetter = getRandLetter();
  }

  private String currentLetter = "";

  private String[] availableLetters = new String[]
    {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
      "S", "T", "U", "V", "W", "X", "Y", "Z"};

  /**
   * Gets a rand letter.
   *
   * @return the rand letter
   */
  public String getRandLetter() {
    return availableLetters[new Random().nextInt(availableLetters.length)];
  }

  /**
   * Gets the current letter.
   *
   * @return the current letter
   */
  public String getCurrentLetter() {
    return currentLetter;
  }

  /**
   * Gets the next letter. This letter will become the current letter.
   *
   * @return the next letter
   */
  public String getNextLetter() {
    currentLetter = getRandLetter();
    return getCurrentLetter();
  }
}
