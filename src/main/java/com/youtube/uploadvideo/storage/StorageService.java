package com.youtube.uploadvideo.storage;

import org.springframework.core.io.Resource;
import org.springframework.http.codec.multipart.FilePart;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void store(FilePart file);

    Stream<Path> loadAll();

    Path load(String filename);

    String resolvedFilename(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
