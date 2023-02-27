package me.javac.blog.controller;

import lombok.RequiredArgsConstructor;
import me.javac.blog.service.IOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
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

    @GetMapping("/set")
    public Object set( String key,  String value) {
        return ResponseEntity.ok(optionService.updateByKey(key, value));
    }

    @GetMapping("/addView")
    public Object addView() {
        return ResponseEntity.ok(optionService.addViewCount());
    }

}
