package gg.clouke.gtraverse;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Clouke
 * @since 12.08.2023 08:32
 * © github-traverse - All Rights Reserved
 */
public class Entry {

  public static Builder builder() {
    return new Builder();
  }

  public static Entry direct(@Nonnull String owner, @Nonnull String repo, @Nonnull String path) {
    return Entry.builder()
      .owner(owner)
      .repo(repo)
      .path(path)
      .build();
  }

  public static Entry direct(@Nonnull String owner, @Nonnull String repo) {
    return Entry.builder()
      .owner(owner)
      .repo(repo)
      .build();
  }

  private final String owner;
  private final String repo;
  private final String path;
  private final List<String> endSequences = new ArrayList<>();

  Entry(@Nonnull String owner, @Nonnull String repo, @Nonnull String path, @Nonnull List<String> endSequences) {
    this.owner = owner;
    this.repo = repo;
    this.path = path;
    this.endSequences.addAll(endSequences);
  }

  /**
   * Gets the owner of this entry.
   *
   * @return Returns the owner of this entry.
   */
  public String owner() {
    return owner;
  }

  /**
   * Gets the repository of this entry.
   *
   * @return Returns the repository of this entry.
   */
  public String repo() {
    return repo;
  }

  /**
   * Gets the path of this entry.
   *
   * @return Returns the path of this entry.
   */
  public String path() {
    return path;
  }

  /**
   * Gets the ending sequences of this entry.
   *
   * @return Returns the end sequences of this entry.
   */
  public List<String> endSequences() {
    return endSequences;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Entry) {
      Entry entry = (Entry) obj;
      return entry.owner.equals(owner)
        && entry.repo.equals(repo)
        && entry.path.equals(path);
    }
    return false;
  }

  @Override
  public String toString() {
    return "Entry{" +
      "owner='" + owner + '\'' +
      ", repo='" + repo + '\'' +
      ", path='" + path + '\'' +
      '}';
  }

  public static class Builder {
    private String owner;
    private String repo;
    private String path = "";
    private List<String> endSequences = new ArrayList<>();

    public Builder owner(@Nonnull String owner) {
      this.owner = owner;
      return this;
    }

    public Builder repo(@Nonnull String repo) {
      this.repo = repo;
      return this;
    }

    public Builder path(@Nonnull String path) {
      this.path = path;
      return this;
    }

    public Builder endSequences(@Nonnull List<String> endSequences) {
      this.endSequences = endSequences;
      return this;
    }

    public Builder endSequences(@Nonnull String... endSequences) {
      this.endSequences = Arrays.asList(endSequences);
      return this;
    }

    public Entry build() {
      return new Entry(owner, repo, path, endSequences);
    }
  }

}
