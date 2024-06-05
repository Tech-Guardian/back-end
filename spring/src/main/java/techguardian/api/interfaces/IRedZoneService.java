package techguardian.api.interfaces;

import java.util.List;

import techguardian.api.entity.RedZone;

public interface IRedZoneService {

    List<RedZone> findAll();
    RedZone createRedZone(RedZone createdRedZone);
    RedZone updateRedZone(Long id, RedZone updatedRedZone);
    RedZone deleteRedZone (Long id);
}
