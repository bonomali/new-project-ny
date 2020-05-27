package org.google.callmeback.api;

public class ReservationWindow {
  /** 
   * The Window with the expected, minimum and maximum call times using a naive
   * calculation of the average call wait time.
  */
  public Window naiveWindow;
  /** 
   * The Window with the expected, minimum and maximum call times using a moving
   * average call wait time calculation.
   */
  public Window movingAvgWindow;
}