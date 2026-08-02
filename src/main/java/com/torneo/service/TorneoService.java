package com.torneo.service;

import com.torneo.model.*;
import com.torneo.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TorneoService {

    private final UserRepository userRepository;
    private final ActivityLogRepository logRepository;
    private final ChallengeRepository challengeRepository;

    private static final List<String> DEPORTES = Arrays.asList("Correr", "Ciclismo", "Futbol");

    public TorneoService(UserRepository userRepository, ActivityLogRepository logRepository, ChallengeRepository challengeRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
        this.challengeRepository = challengeRepository;
    }

    public boolean isFotoAvailable(int subgroup, int activeDay) {
        List<ActivityLog> fotoLogs = logRepository.findBySubgroupAndType(subgroup, "Foto");
        if (fotoLogs.size() >= 3) return false;
        boolean doneToday = fotoLogs.stream().anyMatch(l -> l.getDayNumber() == activeDay);
        if (doneToday) return false;
        if (activeDay > 1) {
            boolean doneYesterday = fotoLogs.stream().anyMatch(l -> l.getDayNumber() == (activeDay - 1));
            if (doneYesterday) return false;
        }
        return true;
    }

    public String getChosenSport(String userId) {
        List<ActivityLog> logs = logRepository.findByUserId(userId);
        return logs.stream()
                .map(ActivityLog::getType)
                .filter(DEPORTES::contains)
                .findFirst()
                .orElse(null);
    }

    public ActivityLog registrarActividad(User user, String type, double amount, String gameOrDetail, int activeDay) {
        if (DEPORTES.contains(type)) {
            String deporteElegido = getChosenSport(user.getId());
            if (deporteElegido != null && !deporteElegido.equalsIgnoreCase(type)) {
                return null;
            }
        }

        if ("Foto".equalsIgnoreCase(type)) {
            if (!isFotoAvailable(user.getSubgroup(), activeDay)) {
                return null;
            }
            amount = 1.0; 
        }

        // --- MANEJO EXCLUSIVO DEL RETO ---
        if ("Reto".equalsIgnoreCase(type)) {
            try {
                Long challengeId = Long.parseLong(gameOrDetail);
                Optional<Challenge> optChallenge = challengeRepository.findById(challengeId);
                if (optChallenge.isEmpty()) return null;

                Challenge reto = optChallenge.get();

                // Evitar trampas (volver a enviarlo si ya lo completaron)
                if (reto.isCompletedFor(user)) return null;

                // Marcar completado en la BBDD de Retos
                if (!reto.getTargetType().equalsIgnoreCase("INDIVIDUAL")) {
                    reto.addCompletedBy("SUBGROUP:" + user.getSubgroup());
                } else {
                    reto.addCompletedBy("USER:" + user.getId());
                }
                challengeRepository.save(reto);

                // Sobrescribimos 'amount' y 'gameOrDetail' con los valores reales del reto
                amount = reto.getPoints();
                gameOrDetail = reto.getTitle(); 
            } catch (Exception e) {
                return null; // Si el ID que nos llega no es parseable o válido
            }
        }

        double puntosAntes = getCappedUserPoints(user);

        ActivityLog log = new ActivityLog(
                user.getId(), user.getName(), user.getTeam(), user.getSubgroup(),
                type, amount, gameOrDetail, 0.0, activeDay
        );

        logRepository.save(log);

        double puntosDespues = getCappedUserPoints(user);
        double incrementoReal = Math.max(0.0, puntosDespues - puntosAntes);
        double puntosOtorgados = Math.round(incrementoReal * 100.0) / 100.0;

        log.setPointsMvp(puntosOtorgados);
        return logRepository.save(log);
    }

    public double calculateMvpPoints(String type, double amount) {
        return switch (type) {
            case "Correr" -> amount * 0.6;
            case "Ciclismo" -> amount * 0.4;
            case "Futbol" -> amount * 0.1;
            case "Aguadillas" -> amount * 0.5;
            case "Cartas" -> amount * 1.25;
            case "Piscina" -> amount * 7.5;
            case "Cancion" -> amount * 12.5;
            case "Foto" -> amount * 8.0;
            case "Reto" -> amount;
            case "AjusteAdmin" -> amount;
            default -> 0.0;
        };
    }

    public Map<Integer, Double> getSubgroupScores() {
        Map<Integer, Double> scores = new HashMap<>();
        for (int i = 1; i <= 6; i++) {
            scores.put(i, calculateSubgroupScore(i));
        }
        return scores;
    }

    public double calculateSubgroupScore(int subgroupNum) {
        List<User> members = userRepository.findBySubgroup(subgroupNum);
        if (members.isEmpty()) return 0.0;

        double individualSum = 0.0;
        for (User u : members) {
            individualSum += getCappedUserPoints(u);
        }

        double multiplier = (members.size() == 5) ? 0.8 : 1.0;
        return (individualSum * multiplier);
    }

    public double getCappedUserPoints(User user) {
        List<ActivityLog> logs = logRepository.findByUserId(user.getId());
        
        Map<Integer, List<ActivityLog>> logsByDay = new HashMap<>();
        for (ActivityLog l : logs) {
            int dayKey = (l.getDay() != null) ? l.getDay() : 1;
            logsByDay.computeIfAbsent(dayKey, k -> new ArrayList<>()).add(l);
        }

        double runKm = 0.0, bikeKm = 0.0, futbolMins = 0.0;
        double totalAguadillas = 0.0, totalCartasWins = 0.0, totalPiscinaDays = 0.0;
        double totalCanciones = 0.0, totalFotos = 0.0, totalRetos = 0.0, totalAjustes = 0.0; 

        for (Map.Entry<Integer, List<ActivityLog>> entry : logsByDay.entrySet()) {
            double dailyCartas = 0.0, dailyAguadillas = 0.0;
            for (ActivityLog l : entry.getValue()) {
                switch (l.getType()) {
                    case "Correr" -> runKm += l.getAmount();
                    case "Ciclismo" -> bikeKm += l.getAmount();
                    case "Futbol" -> futbolMins += l.getAmount();
                    case "Aguadillas" -> dailyAguadillas += l.getAmount();
                    case "Cartas" -> dailyCartas += l.getAmount();
                    case "Piscina" -> totalPiscinaDays += Math.min(l.getAmount(), 1.0);
                    case "Cancion" -> totalCanciones += Math.min(l.getAmount(), 1.0);
                    case "Foto" -> totalFotos += l.getAmount();
                    case "Reto" -> totalRetos += l.getAmount();
                    case "AjusteAdmin" -> totalAjustes += l.getAmount(); 
                }
            }
            double maxCartasDaily = "🃏".equals(user.getTrait()) ? 3.0 : 1.0;
            totalCartasWins += Math.min(dailyCartas, maxCartasDaily);

            double maxAguadillasDaily = "🌊".equals(user.getTrait()) ? 5.0 : 2.0;
            totalAguadillas += Math.min(dailyAguadillas, maxAguadillasDaily);
        }

        double points = 0.0;

        if ("🏃‍♂️".equals(user.getTrait())) {
            double baseKm = Math.min(runKm, 10.0);
            double extraKm = Math.min(Math.max(0, runKm - 10.0), 10.0);
            points += (baseKm * 0.6) + (extraKm * 0.3);
        } else {
            points += Math.min(runKm, 10.0) * 0.6;
        }

        if ("🚲".equals(user.getTrait())) {
            double baseKm = Math.min(bikeKm, 25.0);
            double extraKm = Math.min(Math.max(0, bikeKm - 25.0), 25.0);
            points += (baseKm * 0.4) + (extraKm * 0.2);
        } else {
            points += Math.min(bikeKm, 25.0) * 0.4;
        }

        if ("⚽".equals(user.getTrait())) {
            double baseMins = Math.min(futbolMins, 90.0);
            double extraMins = Math.min(Math.max(0, futbolMins - 90.0), 90.0);
            points += (baseMins * 0.1) + (extraMins * 0.05);
        } else {
            points += Math.min(futbolMins, 90.0) * 0.1;
        }

        double maxAguadillasGlobal = "🌊".equals(user.getTrait()) ? 20.0 : 10.0;
        points += Math.min(totalAguadillas, maxAguadillasGlobal) * 0.5;

        double maxCartasGlobal = "🃏".equals(user.getTrait()) ? 15.0 : 5.0;
        points += Math.min(totalCartasWins, maxCartasGlobal) * 1.25;

        if ("⏰".equals(user.getTrait())) {
            if (totalPiscinaDays >= 1) {
                double extraDays = Math.min(totalPiscinaDays - 1, 4.0);
                points += 7.5 + (extraDays * 0.8);
            }
        } else {
            if (totalPiscinaDays >= 1) points += 7.5;
        }

        if ("🎤".equals(user.getTrait())) {
            if (totalCanciones >= 1) {
                double extraSongs = Math.min(totalCanciones - 1, 2.0);
                points += 12.5 + (extraSongs * 2.0);
            }
        } else {
            if (totalCanciones >= 1) points += 12.5;
        }

        points += totalFotos * 8.0;
        points += totalRetos;
        points += totalAjustes; 

        return points;
    }

    public List<User> getTopMvpPlayers() {
        List<User> allUsers = userRepository.findAll();
        allUsers.sort((u1, u2) -> Double.compare(getMvpScore(u2.getId()), getMvpScore(u1.getId())));
        return allUsers;
    }

    public double getMvpScore(String userId) {
        return userRepository.findById(userId)
                .map(this::getCappedUserPoints)
                .orElse(0.0);
    }
    
    public ActivityLog registrarAjusteAdmin(User user, double amount, String motivo, int activeDay) {
        ActivityLog log = new ActivityLog(
                user.getId(), user.getName(), user.getTeam(), user.getSubgroup(),
                "AjusteAdmin", amount, motivo, amount, activeDay
        );
        return logRepository.save(log);
    }
}