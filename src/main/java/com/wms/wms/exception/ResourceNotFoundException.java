package com.wms.wms.exception;



//── 404: product or warehouse ID not found ────────────────────────────────────
public class ResourceNotFoundException extends RuntimeException {
 public ResourceNotFoundException(String message) {
     super(message);
 }
}
