package com.wms.wms.exception;

//── 409: warehouse is full, no bin has available space ────────────────────────
public class NoBinAvailableException extends RuntimeException {
	private static final long serialVersionUID = 1L;
 public NoBinAvailableException(String message) {
     super(message);
 }
}
