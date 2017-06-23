package isys.duplicatefilter.services;

import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.repositories.IArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public abstract class ArticleService<T extends Article> {

    private final IArticleRepository<T> articleRepository;

    public ArticleService(IArticleRepository<T> articleRepository) {
        this.articleRepository = articleRepository;
    }

    public void save(T article) {
        articleRepository.save(article);
    }

    public T fetch(String id) {
        return articleRepository.findById(id);
    }

    public List<T> findAll() {
        return articleRepository.findAll();
    }

    public Page<T> findAll(Pageable pageable) {
        return articleRepository.findAll(pageable);
    }
}
