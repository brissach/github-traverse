package gg.clouke.gtraverse.read;

import gg.clouke.gtraverse.TokenMarker;

import java.io.IOException;

/**
 * @author Clouke
 * @since 12.08.2023 10:04
 * © github-traverse - All Rights Reserved
 */
public interface Reader<I, O> extends TokenMarker {
  /**
   * Basic IO operation reading from input and returning output
   *
   * @param input The given input
   * @throws IOException If an error occurs during the reading process
   * @return The output after reading process
   */
  O read(I input) throws IOException;

  @Override
  void setToken(String token);
}
