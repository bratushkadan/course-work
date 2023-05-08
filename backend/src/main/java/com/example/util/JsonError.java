package com.example.util;

public class JsonError {
  private String message;
  private boolean error = true;

  public JsonError(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public boolean getError() {
    return error;
  }
}
