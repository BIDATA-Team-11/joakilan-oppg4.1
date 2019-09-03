package no.iskra.ccalculator;

import no.iskra.ccalculator.Currency;
import no.iskra.ccalculator.exchangeApiException;
import no.iskra.ccalculator.CurrencyRates;
import java.util.Scanner;
import java.util.InputMismatchException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

class CurrencyCalculator {
  static JSONObject rates;

  static Scanner input;

  static boolean exit;
  static String lastUpdated;
  static String baseCurrency;

  public static void main(String[] args) {
    baseCurrency = "NOK";
    input = new Scanner(System.in);
    exit = false;

    System.out.print("Fetching currency rates... ");
    try {
      rates = CurrencyRates.getCurrencyRates(baseCurrency);
    } catch(exchangeApiException e) {
      System.out.println(e);
      System.out.println("Exiting");
      return;
    }
    System.out.println("OK!\n");

    System.out.println("Iskra Currency Calculator\n");
    helpText();
    System.out.println("Enter a value and two currency symbols, example:");
    System.out.println("25 nok eur\nThis will get the value of 25 NOK and display it in EUR.");

    while (!exit) {
      displayCommandPrompt();
    }

    System.out.println("Bye");
  }

  static void waitForInput() {
    System.out.println();
    System.out.println("Press Enter to continue...");
    try {
      System.in.read();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  static void displayCommandPrompt() {
    // System.out.println("Skriv 'list' for å se hvilke valutaer som kan brukes.");
    String[] command = getStringFromUser().toUpperCase().split(" ");
    if (command.length < 1) {
      System.out.println("No command recognised");
    } else if (command[0].compareToIgnoreCase("help") == 0) {
      helpText();
    } else if (command[0].compareToIgnoreCase("list") == 0) {
      listCurrencies();
      return;
    } else if (command[0].compareToIgnoreCase("exit") == 0) {
      exit = true;
    } else {
      try {
        double val = Double.parseDouble(command[0]);
        String fromCurrency = command[1];
        String toCurrency = command.length == 4 ? command[3] : command[2];
        double val2 = val*rates.getJSONObject("rates").getDouble(toCurrency)/
          rates.getJSONObject("rates").getDouble(fromCurrency);
        System.out.println(String.format("%.2f %s = %2f %s", val, fromCurrency, val2, toCurrency));
      } catch(JSONException e) {
        System.out.println("Command not recognised or unknown currency");
      } catch (NumberFormatException e) {
        System.out.println("Malformed command. Please try again.");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  static void setBaseCurrency(String base) {

  }

  static void listCurrencies() {
    System.out.println("Avaliable currencies and rates in " + baseCurrency + ":");
    //rates.getJSONObject("rates").keySet().forEach(System.out::println);
    JSONObject currentRates = rates.getJSONObject("rates");
    for (String key : currentRates.keySet()) {
      System.out.print(key);
      String rate = String.format("%5.2f", currentRates.getDouble(key));
      System.out.println(" " + rate);
    }
  }

  static void helpText() {
    System.out.println("help\tDisplay this text");
    System.out.println("base\tChange the base currency");
    System.out.println("list\tList all available currencies and rates");
    System.out.println("exit\tYou guess what happens");
    System.out.println();
  }

  static int getIntFromUser() {
    System.out.print("\n> ");
    int retVal = 99;
    try {
      retVal = input.nextInt();
    } catch(InputMismatchException e) {
      // Bruker kan ha gjort noe nasty. Tøm buffer og prøv på nytt.
      input.next();
    }
    return retVal;
  }

  static String getStringFromUser() {
    System.out.print("\n> ");
    String retVal = "";
    try {
      retVal = input.nextLine();
    } catch (InputMismatchException e) {
      System.out.println("Something went wrong. Here's the details:");
      e.printStackTrace();
      input.next();
    } catch(Exception e) {
      e.printStackTrace();
    }
    System.out.println();
    return retVal;
  }
}
