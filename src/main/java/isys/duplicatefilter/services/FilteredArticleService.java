package isys.duplicatefilter.services;

import isys.duplicatefilter.dto.FilteredArticle;
import isys.duplicatefilter.repositories.IFilteredArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FilteredArticleService extends ArticleService<FilteredArticle> {

    @Autowired
    public FilteredArticleService(IFilteredArticleRepository articleRepository) {
        super(articleRepository);
    }
}
