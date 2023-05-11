package hawk.recall.controller;

import document.Document;
import hawk.recall.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/cloud-recall/")
public class CloudRecall {

    @Resource
    SearchService searchService;

    @RequestMapping(value = "/recall/{mode}/{query}", method = RequestMethod.GET)
    @ResponseBody
    public List<Document> search(@PathVariable String query, @PathVariable String mode){
        return searchService.search(query, mode);
    }

    @RequestMapping(value = "/recall/range/{left}/{right}", method = RequestMethod.GET)
    @ResponseBody
    public List<Document> rangeSearch(@PathVariable Double left, @PathVariable Double right) {
        return searchService.rangeSearch(left, right);
    }

}
