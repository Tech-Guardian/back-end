package techguardian.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import techguardian.api.entity.RedZone;
import techguardian.api.service.RedZoneService;

@RestController
@CrossOrigin
public class RedZoneController {

    @Autowired
    private RedZoneService redZService;

    @GetMapping("/registro/redzone")
    public List<RedZone> findAll() {
        return redZService.findAll();
    }

    @PostMapping("/redzone")
    public RedZone createRedZone(@RequestBody RedZone createdRedZone) {
        return redZService.createRedZone(createdRedZone);
    }

    @PutMapping("/{id}")
    public RedZone updateRedZone(@PathVariable Long id, @RequestBody RedZone updatedRedZone) {
        return redZService.updateRedZone(id, updatedRedZone);
    }

    @DeleteMapping("/{id}")
    public RedZone deleteRedZone(@PathVariable Long id) {
        return redZService.deleteRedZone(id);
    }
}