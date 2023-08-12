package gg.clouke.gtraverse;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Clouke
 * @since 12.08.2023 08:33
 * © github-traverse - All Rights Reserved
 */
public class Callback extends HashMap<String, String> {

  /**
   * Constructs a new {@link Callback} from the given {@link String}s.
   *
   * @param file The file to construct the callback from.
   * @param content The content to construct the callback from.
   * @return The constructed callback.
   */
  public static Callback of(@Nonnull String file, @Nonnull String content) {
    return new Callback(file, content);
  }

  /**
   * Constructs a new {@link Callback} from the given {@link Map}.
   *
   * @param map The map to construct the callback from.
   * @return The constructed callback.
   */
  public static Callback of(@Nonnull Map<String, String> map) {
    return new Callback(map);
  }

  Callback(Map<String, String> map) {
    super(map);
  }

  Callback(@Nonnull String file, @Nonnull String content) {
    super(); put(file, content);
  }

}
