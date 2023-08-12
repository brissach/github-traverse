package gg.clouke.gtraverse;

import gg.acai.acava.cache.Cache;
import gg.acai.acava.cache.CacheBuilder;
import gg.acai.acava.cache.CacheType;
import gg.acai.acava.scheduler.AsyncPlaceholder;
import gg.acai.acava.scheduler.Schedulers;
import gg.clouke.gtraverse.read.Reader;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * <p>Example Usage:
 * <pre>
 *   {@code
 *    Session session = new SessionBuilder()
 *      .accessToken("github access token") // find your access token at My Account -> Settings -> Developer Settings -> Personal Access Tokens
 *      .recursive() // should the reader read recursively?
 *      .expireAfter(10L, TimeUnit.MINUTES) // when should the cache expire?
 *      .build();
 *
 *    session.read(
 *       Entry.builder()
 *         .owner("Clouke") // example owner
 *         .repo("github-traverse") // example repository
 *         .endSequences(".java", ".kt") // ending file sequences
 *         .build()
 *     ).whenComplete(callback -> callback.forEach((k,v) -> System.out.println(k + " -> " + v))); // callback accepted asynchronously when the read is complete
 *   }
 * </pre>
 * @author Clouke
 * @since 12.08.2023 08:34
 * © github-traverse - All Rights Reserved
 */
public class Session {

  private final Reader<Entry, Callback> reader;
  private final Cache<String, Callback> cache;

  /**
   * Constructs a new session with the given builder.
   *
   * @param builder The given {@link SessionBuilder}
   */
  public Session(SessionBuilder builder) {
    this.reader = builder.reader;
    this.cache =
      new CacheBuilder<>()
        .asType(CacheType.DEFAULT)
        .useLock(new ReentrantLock())
        .expireAfterWrite(
          builder.expiryTime,
          builder.expiryUnit
        )
        .build();
  }

  /**
   * Reads the entry with a future callback, does not support failure.
   *
   * @param entry The entry to read
   * @return Returns the callback promise
   */
  public AsyncPlaceholder<Callback> read(Entry entry) {
    return read(
      entry,
      null
    );
  }

  /**
   * Reads the entry with a future callback, supports failure.
   * <p>
   *   <b>NOTE:</b> Loads from cache if available.
   * </p>
   * <p>Example Usage:
   * <pre>
   *   {@code
   *    Session session = new SessionBuilder()
   *      .accessToken("github access token")
   *      .recursive()
   *      .expireAfter(10L, TimeUnit.MINUTES)
   *      .build();
   *
   *    session.read(
   *       Entry.builder()
   *         .owner("Clouke")
   *         .repo("github-traverse")
   *         .endSequences(".java", ".kt") // etc
   *         .build()
   *     ).whenComplete(callback -> callback.forEach((k,v) -> System.out.println(k + " -> " + v)));
   *   }
   * </pre>
   *
   * @param entry The entry to read
   * @param failure The failure consumer if the read fails
   * @return Returns the callback promise
   */
  public AsyncPlaceholder<Callback> read(Entry entry, Consumer<? super Throwable> failure) {
    return Schedulers
      .supplyAsync(() -> {
        try {
          String meta = entry.owner() + ":" + entry.repo();
          Callback callback = cache
            .get(meta)
            .orElse(reader.read(entry));

          cache.put(
            meta,
            callback
          );

          return callback;
        } catch (Throwable throwable) {
          if (failure != null)
            failure.accept(throwable);
        }
        return null;
      });
  }

  /**
   * Gets the current session cache.
   *
   * @return Returns the session cache
   */
  public Cache<String, Callback> cache() {
    return cache;
  }
}
