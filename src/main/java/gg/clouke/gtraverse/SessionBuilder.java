package gg.clouke.gtraverse;

import gg.clouke.gtraverse.read.Reader;
import gg.clouke.gtraverse.read.RecursiveReader;
import gg.clouke.gtraverse.read.SingleReader;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Clouke
 * @since 12.08.2023 19:45
 * © github-traverse - All Rights Reserved
 */
public class SessionBuilder {

  protected String accessToken;
  protected TimeUnit expiryUnit = TimeUnit.MINUTES;
  protected long expiryTime = 10L;
  protected Reader<Entry, Callback> reader;

  /**
   * Applies the access token to the session.
   *
   * @param accessToken The access token to use.
   * @return This builder for chaining.
   */
  public SessionBuilder accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  /**
   * Sets the expiry time for the cache.
   *
   * @param time The time to expire after.
   * @param unit The unit of time.
   * @return This builder for chaining.
   */
  public SessionBuilder expireAfter(long time, TimeUnit unit) {
    this.expiryTime = time;
    this.expiryUnit = unit;
    return this;
  }

  /**
   * Sets the reader for the session.
   *
   * @param reader The reader to use.
   * @return This builder for chaining.
   */
  public SessionBuilder reader(Reader<Entry, Callback> reader) {
    this.reader = reader;
    return this;
  }

  /**
   * Uses the {@link RecursiveReader} as the reader.
   *
   * @return This builder for chaining.
   */
  public SessionBuilder recursive() {
    return reader(new RecursiveReader());
  }

  /**
   * Uses the {@link SingleReader} as the reader.
   *
   * @return This builder for chaining.
   */
  public SessionBuilder single() {
    return reader(new SingleReader());
  }

  /**
   * Builds the session.
   *
   * @throws NullPointerException If the reader is null.
   * @return The built session.
   */
  public Session build() {
    Objects.requireNonNull(
      reader,
      "Reader cannot be null, Use SessionBuilder#reader(new -> RecursiveReader() | SingleReader()) to set a reader."
    ).setToken(accessToken);
    return new Session(this);
  }

}
