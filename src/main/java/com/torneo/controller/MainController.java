package com.torneo.controller;

import com.torneo.model.*;
import com.torneo.repository.*;
import com.torneo.service.TorneoService;
import com.torneo.util.TextNormalizer;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {

    private final UserRepository userRepository;
    private final ActivityLogRepository logRepository;
    private final SongRepository songRepository;
    private final ChallengeRepository challengeRepository;
    private final AppConfigRepository appConfigRepository; // <--- Añadido repositorio
    private final TorneoService torneoService;

    public MainController(UserRepository userRepository, ActivityLogRepository logRepository,
                          SongRepository songRepository, ChallengeRepository challengeRepository,
                          AppConfigRepository appConfigRepository, TorneoService torneoService) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.songRepository = songRepository;
        this.challengeRepository = challengeRepository;
        this.appConfigRepository = appConfigRepository;
        this.torneoService = torneoService;
    }

    // Método de ayuda para obtener el día desde la BBDD
    private int getActiveDay() {
        AppConfig config = appConfigRepository.findById("config").orElse(null);
        if (config == null) {
            config = new AppConfig("config", 1); // Por defecto empieza en 1
            appConfigRepository.save(config);
        }
        return config.getActiveDay();
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        int activeDay = getActiveDay(); // Leer de BBDD

        model.addAttribute("user", loggedUser);
        model.addAttribute("activeDay", activeDay);

        if (loggedUser != null) {
            model.addAttribute("userSport", torneoService.getChosenSport(loggedUser.getId()));
        }

        model.addAttribute("subgroupScores", torneoService.getSubgroupScores());
        model.addAttribute("users", userRepository.findAll());

        model.addAttribute("todaySongsTutiorodri", songRepository.findByDayNumberAndTeam(activeDay, "TUTIORODRI"));
        model.addAttribute("todaySongsPapichechu", songRepository.findByDayNumberAndTeam(activeDay, "PAPICHECHU"));
        model.addAttribute("topMvps", torneoService.getTopMvpPlayers());
        model.addAttribute("torneoService", torneoService);

        model.addAttribute("logs", logRepository.findAll());

        // GESTIÓN DE RETOS DIARIOS
        List<Challenge> dailyChallenges = challengeRepository.findByDayNumber(activeDay);
        
        // 1. Todos los retos del día (para la burbuja)
        model.addAttribute("allDailyChallenges", dailyChallenges);
        
        // 2. Retos disponibles (excluye completados)
        List<Challenge> availableChallenges = new ArrayList<>();
        if (loggedUser != null) {
            for (Challenge c : dailyChallenges) {
                if (!c.isCompletedFor(loggedUser)) {
                    availableChallenges.add(c);
                }
            }
        }
        model.addAttribute("availableChallenges", availableChallenges);

        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String name, @RequestParam String password, HttpSession session) {
        String normName = TextNormalizer.normalize(name);
        String normPwd = TextNormalizer.normalize(password);

        List<User> allUsers = userRepository.findAll();
        for (User u : allUsers) {
            if (TextNormalizer.normalize(u.getName()).equals(normName) &&
                TextNormalizer.normalize(u.getPassword()).equals(normPwd)) {
                session.setAttribute("loggedUser", u);
                break;
            }
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/log/activity")
    public String registerActivity(@RequestParam String type,
                                   @RequestParam(value = "amount", defaultValue = "0") double amount,
                                   @RequestParam(defaultValue = "") String gameOrDetail,
                                   HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser == null) return "redirect:/";

        int activeDay = getActiveDay();
        torneoService.registrarActividad(loggedUser, type, amount, gameOrDetail, activeDay);

        return "redirect:/";
    }

    @PostMapping("/admin/set-day")
    public String setDay(@RequestParam int day, HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null && loggedUser.isAdmin()) {
            // Guardar el nuevo día en la BBDD
            AppConfig config = appConfigRepository.findById("config").orElse(new AppConfig("config", 1));
            config.setActiveDay(day);
            appConfigRepository.save(config);
        }
        return "redirect:/";
    }

    @PostMapping("/admin/challenge")
    public String createChallenge(@RequestParam String title,
                                  @RequestParam String description,
                                  @RequestParam double points,
                                  @RequestParam String targetType,
                                  HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null && loggedUser.isAdmin()) {
            int activeDay = getActiveDay();
            Challenge challenge = new Challenge(title, description, points, activeDay, targetType);
            challengeRepository.save(challenge);
        }
        return "redirect:/";
    }

    @PostMapping("/admin/reset")
    public String resetData(HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null && loggedUser.isAdmin()) {
            logRepository.deleteAll();
            challengeRepository.deleteAll();
        }
        return "redirect:/";
    }

    @PostMapping("/admin/ajustar-puntos")
    public String ajustarPuntos(@RequestParam String userId,
                                @RequestParam double amount,
                                @RequestParam String motivo,
                                HttpSession session) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        if (loggedUser != null && loggedUser.isAdmin()) {
            int activeDay = getActiveDay();
            Optional<User> targetUser = userRepository.findById(userId);
            targetUser.ifPresent(user -> 
                torneoService.registrarAjusteAdmin(user, amount, motivo, activeDay)
            );
        }
        return "redirect:/";
    }
}