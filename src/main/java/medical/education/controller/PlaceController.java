package medical.education.controller;

import medical.education.dto.PlaceDTO;
import medical.education.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.backend.library.controller.BaseController;

@CrossOrigin
@RestController
@RequestMapping("/place")
public class PlaceController extends BaseController<PlaceDTO, PlaceService> {
  @Autowired
  private PlaceService placeService;

  @Override
  public PlaceService getService() {
    return placeService;
  }
}
