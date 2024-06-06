package com.example.Teller_Automation.BACKEND.AdminModule.GeneralLedger;

import com.example.Teller_Automation.BACKEND.CustomerModule.Utils.EntityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/gl")
public class GlController {
    private final GlService glService;
    @Autowired
    public GlController(GlService glService) {
        this.glService = glService;
    }

    @PostMapping("create")
    public EntityResponse<?> create(@RequestBody Gl gl) {
        return glService.create(gl);
    }

    @GetMapping("findById")
    public EntityResponse<?> findById(@RequestParam Long id) {
        return glService.findById(id);
    }

    @GetMapping("getAll")
    public EntityResponse<?> getAll() {
        return glService.findAll();
    }

    @PostMapping("link")
    public EntityResponse<?> link(@RequestParam Long id,@RequestParam Long tellerId){
        return glService.link(id,tellerId);
    }
}
