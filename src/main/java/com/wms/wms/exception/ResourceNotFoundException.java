package com.wms.wms.exception;



//── 404: product or warehouse ID not found ────────────────────────────────────
public class ResourceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
 public ResourceNotFoundException(String message) {
     super(message);
 }
}
