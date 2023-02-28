package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.entity.Option;
import me.javac.blog.entity.Tag;
import me.javac.blog.service.IArticleService;
import me.javac.blog.service.IOptionService;
import me.javac.blog.service.ITagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author shironekoa3
 * @since 2022-11-15
 */
@RestController
@RequestMapping("/option")
@RequiredArgsConstructor
public class OptionController {

    private final IOptionService optionService;

    @GetMapping("/list")
    public Object list() {
        return ResponseEntity.ok(optionService.list());
    }
    @GetMapping("/listHome")
    public Object listHome() {
        return ResponseEntity.ok(optionService.listHomePageOptions());
    }
    @PostMapping("/change")
    public Object change(@RequestBody List<Option> optionList) {
        return ResponseEntity.ok(optionService.updateOptions(optionList));
    }

}
