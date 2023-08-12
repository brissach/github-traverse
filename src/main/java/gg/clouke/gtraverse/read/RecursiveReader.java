package gg.clouke.gtraverse.read;

import gg.clouke.gtraverse.Callback;
import gg.clouke.gtraverse.Entry;
import gg.clouke.gtraverse.TokenMarker;
import gg.clouke.gtraverse.exception.ContentFetchingException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Recursive reader for reading all files in a given directory.
 *
 * @author Clouke
 * @since 12.08.2023 10:04
 * © github-traverse - All Rights Reserved
 */
public class RecursiveReader implements Reader<Entry, Callback>, TokenMarker {

  private String token;

  @Override
  public Callback read(Entry entry) throws IOException, ContentFetchingException {
    OkHttpClient client = new OkHttpClient();
    String owner = entry.owner();
    String repo = entry.repo();
    String path = entry.path();
    String apiUrl = "https://api.github.com/repos/" + owner + "/" + repo + "/contents/" + path;

    Request request = new Request.Builder()
      .url(apiUrl)
      .header("Authorization", "Bearer " + token)
      .build();

    Map<String, String> entries = new HashMap<>();
    try (Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful())
        throw new IOException("Error: " + response.code() + " - " + response.message() + " - " + Optional.ofNullable(response.body())
          .map(b -> {
            try {
              return b.string();
            } catch (IOException e) {
              return null;
            }
          }).orElse("Response body is null"));

      JSONArray jsonArray = new JSONArray(Objects.requireNonNull(response.body(), "body").string());
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject item = jsonArray.getJSONObject(i);
        String name = item.getString("name");
        String type = item.getString("type");

        switch (type) {
          case "file": {
            List<String> endSequences = entry.endSequences();
            boolean noneMatch =
              !endSequences.isEmpty() && endSequences
                .stream()
                .noneMatch(name::endsWith);

            if (noneMatch)
              break;

            String contentUrl = item.getString("download_url");
            String classCode = fetchContent(contentUrl);
            entries.put(name, classCode);
            break;
          }
          case "dir": {
            entries.putAll(read(
              Entry.builder()
                .owner(owner)
                .repo(repo)
                .path(path + "/" + name)
                .endSequences(entry.endSequences())
                .build()
              ));
            break;
          }
        }
      }

      return Callback.of(entries);
    }
  }

  private static String fetchContent(String contentUrl) throws IOException, ContentFetchingException, NullPointerException {
    OkHttpClient client = new OkHttpClient();
    Request request = new Request.Builder()
      .url(contentUrl)
      .build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        return Objects.requireNonNull(
          response.body(),
          "Response body is null"
        ).string();
      }
      throw new ContentFetchingException("Error fetching file content: " + response.code() + " - " + response.message());
    }
  }

  @Override
  public void setToken(String token) {
    this.token = token;
  }
}
