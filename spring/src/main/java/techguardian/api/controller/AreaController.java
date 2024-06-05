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

import techguardian.api.entity.Area;
import techguardian.api.service.AreaService;

@RestController
@CrossOrigin
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("/registro/area")
    public List<Area> findAll() {
        return areaService.findAll();
    }

    @GetMapping("/area/{name}") 
    public Area getAreaByName(@PathVariable String name) {
        return areaService.findAreaByName(name);
    }

    @PostMapping("/area")
    public Area createArea(@RequestBody Area createdArea) {
        return areaService.createArea(createdArea);
    }

    @PutMapping("/area/{id}")
    public Area updateArea(@PathVariable Long id, @RequestBody Area updatedArea) {
        return areaService.updateArea(id, updatedArea);
    }

    @DeleteMapping("/area/{id}")
    public Area deleteArea(@PathVariable Long id) {
        return areaService.deleteArea(id);
    }

}
