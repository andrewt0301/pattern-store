package aakrasnov.diploma.client.cache.impl;

import aakrasnov.diploma.client.cache.Index;
import aakrasnov.diploma.common.DocDto;
import java.util.List;

public class IndexJson implements Index {
    @Override
    public void cacheDocs(final List<DocDto> docDtos) {
        // get write lock
    }

    @Override
    public void invalidateByDocId(final String docId) {
        // get write lock
    }

    @Override
    public boolean isDocActual(final String docId) {
        // get read Lock
        // use ActualDoc
        return false;
    }
}
