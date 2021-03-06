package se.kth.tablespoon.client.events;

public enum Comparator {
  GREATER_THAN(">"),
  GREATER_THAN_OR_EQUAL("≥"),
  LESS_THAN("<"),
  LESS_THAN_OR_EQUAL("≤");
  public String symbol;

  private Comparator(String symbol) {
    this.symbol = symbol;
  }
}