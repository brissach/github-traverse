package gg.clouke.gtraverse.exception;

/**
 * @author Clouke
 * @since 12.08.2023 10:10
 * © github-traverse - All Rights Reserved
 */
public class ContentFetchingException extends RuntimeException {

  public ContentFetchingException(String message) {
    super(message);
  }

  public ContentFetchingException(String message, Throwable cause) {
    super(message, cause);
  }

}
