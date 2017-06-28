package isys.duplicatefilter.controllers;

import isys.duplicatefilter.dto.ErrorMessage;
import isys.duplicatefilter.dto.Pages;
import isys.duplicatefilter.services.ArticleService;
import isys.duplicatefilter.services.FilteredArticleService;
import isys.duplicatefilter.services.RawArticleService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Preconditions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final FilteredArticleService filteredArticleService;
    private final RawArticleService rawArticleService;

    @GetMapping("/articles/page")
    public ResponseEntity getWithoutPageNumber() {
        return new ResponseEntity<>(
                new ErrorMessage("Missing page number. " +
                        "The number of pages can be found at GET /articles/pages or /filteredArticles/pages. " +
                        "The first page is 0."),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/filteredArticles/page")
    public ResponseEntity getFilteredWithoutPageNumber() {
        return new ResponseEntity<>(
                new ErrorMessage("Missing page number. " +
                        "The number of pages can be found at GET /articles/pages or /filteredArticles/pages. " +
                        "The first page is 0."),
                HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/articles/page/{pageNumber}")
    public ResponseEntity getArticles(@PathVariable Integer pageNumber) {
        return retrievePage(pageNumber, rawArticleService);
    }

    @GetMapping("/duplicates")
    public ModelAndView getDuplicates() {
        return new ModelAndView("redirect:/filteredArticles/page/<pageNumber>");
    }

    @GetMapping(path = "/filteredArticles/page/{pageNumber}")
    public ResponseEntity getFilteredArticles(@PathVariable Integer pageNumber) throws IOException, URISyntaxException {
        return retrievePage(pageNumber, filteredArticleService);
    }

    @GetMapping("/{articleType}/pages")
    public ResponseEntity getNumberOfPages(@PathVariable String articleType) {
        if (articleType.equals("articles")) {
            return new ResponseEntity<>(new Pages(rawArticleService.getNumberOfPages()), HttpStatus.OK);
        }
        if (articleType.equals("filteredArticles")) {
            return new ResponseEntity<>(new Pages(rawArticleService.getNumberOfPages()), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorMessage("Allowed resources are /articles/pages and /filteredArticles/pages"), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity retrievePage(Integer pageNumber, ArticleService<?> service) {
        Preconditions.checkArgument(pageNumber >= 0 && pageNumber <service.getNumberOfPages(), "Invalid page number " + pageNumber + ".");
        List<?> page = service.getPage(pageNumber);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
}
