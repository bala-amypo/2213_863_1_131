public interface ZoneService {

    Zone createZone(Zone zone);

    Zone updateZone(Long id, Zone zone);

    void deactivateZone(Long id);

    Zone getById(Long id);

    List<Zone> getAllZones();
}
