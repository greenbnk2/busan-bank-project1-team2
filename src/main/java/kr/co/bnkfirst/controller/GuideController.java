package kr.co.bnkfirst.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info/guide")
public class GuideController {

    @GetMapping("/ceoGreeting")
    public String ceoGreeting(){
        return "info/guide/ceoGreeting/info_ceoGreeting";
    }

    @GetMapping("/coreValues")
    public String coreValue() {
        return "info/guide/coreValues/info_coreValues";
    }

    @GetMapping("/groupMission")
    public String groupMission() {
        return "info/guide/groupMission/info_groupMission";
    }

    @GetMapping("/groupVision")
    public String groupVision() {
        return "info/guide/groupVision/info_groupVision";
    }

    @GetMapping("/missionStrategy")
    public String missionStrategy() {
        return "info/guide/missionStrategy/info_missionStrategy";
    }

    @GetMapping("/strategySlogan")
    public String strategySlogan() {
        return "info/guide/strategySlogan/info_strategySlogan";
    }

    @GetMapping("")
    public String guideIndex() {
        return "redirect:info/guide/info_ceoGreeting"; // templates/info/guide/index.html
    }


}
