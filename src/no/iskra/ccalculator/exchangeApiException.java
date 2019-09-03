package no.iskra.ccalculator;

public class exchangeApiException extends Exception {
  public exchangeApiException(String errorMessage, Throwable err) {
    super(errorMessage, err);
  }
}
