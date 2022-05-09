package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.CachedDocInfo;
import aakrasnov.diploma.client.cache.Index;
import aakrasnov.diploma.client.cache.IndexFile;
import aakrasnov.diploma.client.utils.TimeConverter;
import aakrasnov.diploma.common.DocDto;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IndexJson implements Index {
    private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
    private static final Lock READ_LOCK = LOCK.readLock();
    private static final Lock WRITE_LOCK = LOCK.writeLock();

    private final Path indexPath;

    public IndexJson(final String indexName) {
        this("", indexName);
    }

    public IndexJson(final String prefix, final String indexName) {
        this(Paths.get(prefix, indexName));
    }

    public IndexJson(final Path indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    public void cacheDocs(final List<DocDto> docDtos) {
        createIfNotExist();
        WRITE_LOCK.lock();
        try (FileInputStream fis = new FileInputStream(indexFile())) {
            final IndexFile indexFile = new IndexFileJson(fis);
            List<String> docsPaths = docDtos.stream()
                .map(DocDto::getId)
                .map(id -> String.format("%s.json", id))
                .collect(Collectors.toList());
            indexFile.cacheDocs(docDtos, docsPaths);
            // cache doc files
            try (FileOutputStream fos = new FileOutputStream(indexFile())) {
                indexFile.writeTo(fos);
            }
        } catch (IOException exc) {
            log.error(String.format("Failed to cache docs '%s'", docDtos), exc);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    @Override
    public void invalidateByDocId(final String docId) {
        if (!Files.exists(indexPath)) {
            return;
        }
        WRITE_LOCK.lock();
        try (FileInputStream fis = new FileInputStream(indexFile())) {
            final IndexFile indexFile = new IndexFileJson(fis);
            Optional<CachedDocInfo> docInfo = indexFile.removeDoc(docId);
            if (docInfo.isPresent()) {
                Files.delete(Paths.get(getPrefix(), docInfo.get().getPath()));
                try (FileOutputStream fos = new FileOutputStream(indexFile())) {
                    indexFile.writeTo(fos);
                }
            }
        } catch (IOException exc) {
            log.error(String.format("Failed to invalidate doc '%s'", docId), exc);
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    @Override
    public Optional<CachedDocInfo> getCachedDocInfo(String docId) {
        if (!Files.exists(indexPath)) {
            return Optional.empty();
        }
        READ_LOCK.lock();
        try (FileInputStream fis = new FileInputStream(indexFile())) {
            return new IndexFileJson(fis).getCachedDocInfo(docId);
        } catch (IOException exc) {
            log.error(
                String.format("Failed to get info about cached document by id '%s'", docId),
                exc
            );
        } finally {
            READ_LOCK.unlock();
        }
        return Optional.empty();
    }

    @Override
    public String getPrefix() {
        return indexPath.toFile().getParent();
    }

    private void createIfNotExist() {
        if (!Files.exists(indexPath)) {
            try {
                Files.write(indexPath, getEmptyIndex().toString().getBytes());
            } catch (IOException exc) {
                log.error("Failed to create index file '{}'", indexPath);
                throw new UncheckedIOException(exc);
            }
        }
    }

    private static JsonObject getEmptyIndex() {
        JsonObject jsonObj = new JsonObject();
        jsonObj.addProperty(
            IndexFileJson.CACHE_CREATION,
            new TimeConverter(LocalDateTime.now()).asString()
        );
        return jsonObj;
    }

    private File indexFile() {
        return indexPath.toFile();
    }
}
