package isys.duplicatefilter.controllers;

import isys.duplicatefilter.dto.Article;
import isys.duplicatefilter.exceptions.BadRequestException;
import isys.duplicatefilter.services.ArticleService;
import isys.duplicatefilter.services.FilteredArticleService;
import isys.duplicatefilter.services.RawArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final FilteredArticleService filteredArticleService;
    private final RawArticleService rawArticleService;

    @GetMapping("/articles")
    public ResponseEntity getArticles(@RequestParam(required = false) Integer page) {
        return getResponseEntity(page, rawArticleService);
    }

    @GetMapping("/duplicates")
    public ModelAndView getDuplicates() {
        return new ModelAndView("redirect:/articles?page=1");
    }

    @GetMapping(path = "/filteredArticle")
    public ResponseEntity getFilteredArticles(@RequestParam Integer page) throws IOException, URISyntaxException {
        return getResponseEntity(page, filteredArticleService);
    }

    private ResponseEntity getResponseEntity(@RequestParam Integer page, ArticleService<?> service) {
        return Optional.ofNullable(page)
                .map(pageNumber -> {
                    Page<? extends Article> all = getPage(pageNumber, service);
                    return new ResponseEntity<>(newArrayList(all), HttpStatus.OK);
                }).orElseThrow(() -> {
                    Page<? extends Article> defaultPage = getPage(1, service);
                    String message = MessageFormat.format(
                            "There are {0} pages, choose one. {1}",
                            defaultPage.getTotalPages(),
                            "Example: GET /articles?page=3"
                    );
                    return new BadRequestException(message);
                });
    }


    private Page<? extends Article> getPage(Integer pageNumber, ArticleService repository) {
        Pageable pageable = new PageRequest(pageNumber, 100);
        return repository.findAll(pageable);
    }
}
