package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.Area;

public interface IAreaService {

    List<Area> findAll();
    Area createArea(Area createdArea);
    Area updateArea(Long id, Area updatedArea);
    Area deleteArea (Long id);
}
