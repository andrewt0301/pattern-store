package aakrasnov.diploma.service.repo;

import aakrasnov.diploma.common.Filter;
import aakrasnov.diploma.service.domain.Doc;
import java.util.List;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class DocRepoImpl implements CustomDocRepo {

    private final MongoTemplate mongoTemplate;

    public DocRepoImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Doc> filteredDocuments(final List<Filter> filters) {
        // for nested objects
        // db.inventory.find( { "size.uom": "in" } )
        Query query = new Query();
        filters.forEach(
            filter -> query.addCriteria(Criteria.where(filter.key()).is(filter.value()))
        );
        return mongoTemplate.find(query, Doc.class);
    }
}
