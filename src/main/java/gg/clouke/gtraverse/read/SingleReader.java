package gg.clouke.gtraverse.read;

import gg.clouke.gtraverse.Callback;
import gg.clouke.gtraverse.Entry;

import java.io.IOException;

/**
 * @author Clouke
 * @since 12.08.2023 10:05
 * © github-traverse - All Rights Reserved
 */
@Deprecated
public class SingleReader implements Reader<Entry, Callback> {
  @Override
  public void setToken(String token) {

  }

  @Override
  public Callback read(Entry input) throws IOException {
    return null;
  }
}
