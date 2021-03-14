package de.fhkiel.seg.auction;



import static org.assertj.core.api.Assertions.assertThat;


import io.github.artsok.RepeatedIfExceptionsTest;
import org.junit.jupiter.api.Test;

class LetterGeneratorTest {

  @RepeatedIfExceptionsTest(repeats = 3)
  void getRandLetter() {
    LetterGenerator generatorUnderTest = new LetterGenerator();

    assertThat(generatorUnderTest.getRandLetter())
        .isInstanceOf(String.class)
        .hasSize(1)
        .isNotEqualTo(generatorUnderTest.getRandLetter());
  }

  @Test
  void getCurrentLetter() {
    LetterGenerator generatorUnderTest = new LetterGenerator();

    assertThat(generatorUnderTest.getCurrentLetter())
        .isInstanceOf(String.class)
        .hasSize(1)
        .isEqualTo(generatorUnderTest.getCurrentLetter());

  }

  @RepeatedIfExceptionsTest(repeats = 3)
  void getNextLetter() {
    LetterGenerator generatorUnderTest = new LetterGenerator();

    String firstLetter = generatorUnderTest.getCurrentLetter();

    assertThat(generatorUnderTest.getNextLetter())
        .isInstanceOf(String.class)
        .hasSize(1)
        .isEqualTo(generatorUnderTest.getCurrentLetter())
        .isNotEqualTo(firstLetter)
        .isNotEqualTo(generatorUnderTest.getNextLetter());

  }
}