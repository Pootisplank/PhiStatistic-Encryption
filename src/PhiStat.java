
/**
* Class: CSE 11 Introduction to Computer Science: Object Oriented
* Programming (Java)
* Instructor: Dr. Robert August
* Description: Given a text file from the user, uses the phi-statistic to determine whether or not the text in the file is most likely encrypted or in English.
* Due: 05/13/2019
* Author: Alex Lin
* Student ID: A15571460
* 
*/
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.io.FileInputStream;

public class PhiStat
{
  public static void main(String[] args) throws IOException
  {
    FileInputStream textFile;
    Scanner scnr;
    Scanner file;

    String fileName;
    String textLine = "";
    int[] lineCharFrequency = new int[26];
    int[] totalCharFrequency = new int[26];
    int charCount = 0;

    // Gets the name of the text file to process from the user
    System.out.println("Please enter the name of the file to process: ");
    scnr = new Scanner(System.in);
    fileName = scnr.nextLine();
    scnr.close();

    // Takes the file into the input stream
    textFile = new FileInputStream(fileName);
    // Creates a scanner to parse the text file
    file = new Scanner(textFile);

    while (file.hasNext())
    {
      textLine = file.nextLine().toUpperCase().replaceAll("[^A-Z]", "");

      // Finds the total frequency of each character in each line
      lineCharFrequency = charFrequency(textLine);
      // Computes the total frequency of each letter in the whole file
      totalCharFrequency = addArrays(lineCharFrequency, totalCharFrequency);

      // Computes the total number of characters in the text
      charCount = textLine.length() + charCount;
    }

    file.close();
    double englishPhi = phiEnglishExpected(charCount);
    double randomPhi = phiRandomExpected(charCount);
    int phiCalc = calculatePhi(totalCharFrequency);

    // Printing out results
    printCharFrequency(totalCharFrequency);
    System.out.println("Phi statistic is " + phiCalc);
    System.out.println(charCount + " characters total");
    System.out.printf("Expected English value is %.2f\n", englishPhi);
    System.out.printf("Expected Random  value is %.2f\n", randomPhi);
    System.out.println("The input is probably " + englishOrEncrypted(randomPhi, englishPhi, phiCalc));

  }

  /**
   * Calculates the expected phi-statistic if the text is in English
   * 
   * @param numChar total number of characters in the text
   * @return the expected phi-statistic if the text is English
   */
  public static double phiEnglishExpected(int numChar)
  {
    double phiEnglishExpected = 0.0661 * numChar * (numChar - 1);
    return phiEnglishExpected;
  }

  /**
   * Calculates the expected phi-statistic if the text is a random sequence of
   * characters
   * 
   * @param numChar total number of characters in the text
   * @return the expected phi-statistic if the text is a random sequence of
   *         characters
   */
  public static double phiRandomExpected(int numChar)
  {
    double phiRandomExpected = (0.0385 * numChar) * (numChar - 1);
    return phiRandomExpected;
  }

  /**
   * Calculates the phi-statistic of the text using the frequency of each
   * character.
   * 
   * @param charFrequency an array containing the frequency of each character
   * @return the phi-statistic of the text
   */
  public static int calculatePhi(int[] charFrequency)
  {
    int phiStatistic = 0;
    for (int i = 0; i < charFrequency.length; i++)
    {
      int frequency = charFrequency[i];
      phiStatistic = phiStatistic + frequency * (frequency - 1);
    }
    return phiStatistic;
  }

  /**
   * Computes the frequency of each capital alphabetic letter in a given string
   * 
   * @param text string of text to be analyzed
   * @return an int array containing the frequency of each capital alphabetic
   *         letter in the given string
   */
  public static int[] charFrequency(String text)
  {
    // Creates an array with 26 elements, each containing a letter in the alphabet
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] alphabetArray = alphabet.toCharArray();

    int[] alphabetCounts = new int[26];
    int charFrequency = 0;

    // Compares each letter of the alphabet to each character of the given string
    // and counts the frequency of each letter.
    for (int alphabetChar = 0; alphabetChar < alphabetArray.length; alphabetChar++)
    {
      charFrequency = 0;
      for (int textChar = 0; textChar < text.length(); textChar++)
      {
        if (alphabetArray[alphabetChar] == text.charAt(textChar))
        {
          charFrequency++;
        }
      }
      alphabetCounts[alphabetChar] = charFrequency;
    }
    return alphabetCounts;
  }

  /**
   * Sums together the corresponding elements of two arrays. Arrays given must be
   * the same size.
   * 
   * @param array1 one array to be summed
   * @param array2 one array to be summed
   * @return a new array with the elements of array1 summed with the corresponding
   *         elements of array2
   */
  public static int[] addArrays(int[] array1, int[] array2)
  {
    int arrayLength = array1.length;
    int elementSum;
    int[] arraySum = new int[arrayLength];

    for (int arrayElement = 0; arrayElement < arrayLength; arrayElement++)
    {
      elementSum = array1[arrayElement] + array2[arrayElement];
      arraySum[arrayElement] = elementSum;
    }
    return arraySum;
  }

  /**
   * Given an array containing the frequency of alphabetic letters, matches the
   * frequency with the letter and prints the results.
   * 
   * @param charFrequency array containing the frequencies of alphabetic letters
   */
  public static void printCharFrequency(int[] charFrequency)
  {
    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    char[] alphabetArray = alphabet.toCharArray();
    for (int i = 0; i < charFrequency.length; i++)
    {
      System.out.println(alphabetArray[i] + " " + charFrequency[i]);
    }
  }

  /**
   * Determines if the text is encrypted or in English by comparing the
   * theoretical random and english phi-statistics with the actual value
   * 
   * @param randomPhi  the expected phi-statistic for a random sequence of letters
   * @param englishPhi the expected phi-statistic if the text is in English
   * @param phiActual  the actual phi-statistic of the text
   * @return the string "English" or "Encrypted" depending on which phi-statistic
   *         is closer to the actual calculated value
   */
  public static String englishOrEncrypted(double randomPhi, double englishPhi, double phiActual)
  {
    double englishDifference = Math.abs(phiActual - englishPhi);
    double randomDifference = Math.abs(phiActual - randomPhi);
    if (englishDifference < randomDifference)
    {
      return "English";
    } else
    {
      return "Encrypted";
    }
  }

}
