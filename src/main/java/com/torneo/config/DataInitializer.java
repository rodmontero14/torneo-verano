package com.torneo.config;

import com.torneo.model.*;
import com.torneo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SongRepository songRepository;

    public DataInitializer(UserRepository userRepository, SongRepository songRepository) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // ==========================================
            // 🔴 PAPICHECHU (14 Jugadores)
            // ==========================================
            // ID, Nombre, PIN, order, Equipo, SUBGRUPO REAL, Icono, isAdmin
            userRepository.save(new User("1", "Manuel", "140107", 1, "PAPICHECHU", 4, "⚽", false));
            userRepository.save(new User("2", "Juan", "200803", 2, "PAPICHECHU", 5, "⚽", false));
            userRepository.save(new User("3", "Iker", "280907", 3, "PAPICHECHU", 4, "🚲", false));
            userRepository.save(new User("4", "Ismael", "150909", 4, "PAPICHECHU", 4, "🌊", false));
            userRepository.save(new User("5", "Vega", "011602", 5, "PAPICHECHU", 4, "🏃‍♂️", false));

            userRepository.save(new User("6", "Mencía", "061709", 6, "PAPICHECHU", 3, "🃏", false));
            userRepository.save(new User("7", "Manuel", "311903", 7, "PAPICHECHU", 5, "⚽", false));
            userRepository.save(new User("8", "Carla", "112208", 8, "PAPICHECHU", 1, "🃏", false));
            userRepository.save(new User("9", "Pablo", "242401", 9, "PAPICHECHU", 2, "⚽", false));
            userRepository.save(new User("10", "Carla", "102503", 10, "PAPICHECHU", 5, "🎤", false));

            userRepository.save(new User("11", "Paula", "012607", 11, "PAPICHECHU", 3, "🌊", false));
            userRepository.save(new User("12", "Julia", "213012", 12, "PAPICHECHU", 4, "🃏", false));
            userRepository.save(new User("13", "Sergio", "123308", 13, "PAPICHECHU", 1, "🚲", false));
            userRepository.save(new User("14", "Ian", "121212", 14, "PAPICHECHU", 1, "🚲", false));

            // ==========================================
            // 🔵 TUTIORODRI (15 Jugadores)
            // ==========================================
            userRepository.save(new User("15", "Inés", "290203", 15, "TUTIORODRI", 2, "🃏", false));
            userRepository.save(new User("16", "Juandi", "110402", 16, "TUTIORODRI", 6, "🌊", false));
            userRepository.save(new User("17", "Gonzalo", "120606", 17, "TUTIORODRI", 5, "🌊", false));
            userRepository.save(new User("18", "Diego", "170703", 18, "TUTIORODRI", 3, "⏰", false));
            userRepository.save(new User("19", "Sabina", "071208", 19, "TUTIORODRI", 5, "🃏", false));

            userRepository.save(new User("20", "Rodrigo", "lucia", 20, "TUTIORODRI", 6, "🎤", true)); // ADMIN
            userRepository.save(new User("21", "Paula", "181510", 21, "TUTIORODRI", 6, "🎤", false));
            userRepository.save(new User("22", "Carlos", "242005", 22, "TUTIORODRI", 3, "⚽", false));
            userRepository.save(new User("23", "Sofia", "072108", 23, "TUTIORODRI", 6, "🃏", false));
            userRepository.save(new User("24", "Sofia", "103904", 24, "TUTIORODRI", 2, "🎤", false));
            userRepository.save(new User("25", "Berta", "194712", 25, "TUTIORODRI", 1, "🎤", false));

            userRepository.save(new User("26", "David", "216908", 26, "TUTIORODRI", 1, "🌊", false));
            userRepository.save(new User("27", "Sergio", "307204", 27, "TUTIORODRI", 3, "🚲", false));
            userRepository.save(new User("28", "Lorea", "287605", 28, "TUTIORODRI", 2, "🚲", false));
            userRepository.save(new User("29", "Alonso", "manana", 29, "TUTIORODRI", 6, "⚽", false));
        }

        if (songRepository.count() == 0) {
            // ==========================================
            // 🔵 TUTIORODRI (Días 1 a 7)
            // ==========================================
            // Día 1 (Lunes)
            songRepository.save(new Song("Believer", "Imagine Dragons", "TUTIORODRI", 1));
            songRepository.save(new Song("Pan y mantequilla", "Efecto Pasillo", "TUTIORODRI", 1));
            songRepository.save(new Song("Wonderwall", "Oasis", "TUTIORODRI", 1));
            songRepository.save(new Song("Baila baila baila-remix", "Ozuna", "TUTIORODRI", 1));

            // Día 2 (Martes)
            songRepository.save(new Song("Como has estau?", "Mora", "TUTIORODRI", 2));
            songRepository.save(new Song("Stereo hearts", "Gym Class Heroes ft. Adam Levine", "TUTIORODRI", 2));
            songRepository.save(new Song("Papaoutai", "Stromae", "TUTIORODRI", 2));
            songRepository.save(new Song("Rosas", "La Oreja de Van Gogh", "TUTIORODRI", 2));

            // Día 3 (Miércoles)
            songRepository.save(new Song("Un verano sin ti", "Bad Bunny", "TUTIORODRI", 3));
            songRepository.save(new Song("Thunder", "Imagine Dragons", "TUTIORODRI", 3));
            songRepository.save(new Song("Un beso y una flor", "Nino Bravo", "TUTIORODRI", 3));
            songRepository.save(new Song("Amor con hielo", "Morat", "TUTIORODRI", 3));

            // Día 4 (Jueves)
            songRepository.save(new Song("I wonder", "Kanye West", "TUTIORODRI", 4));
            songRepository.save(new Song("Enseñame a bailar", "Bad Bunny", "TUTIORODRI", 4));
            songRepository.save(new Song("Cuídate", "La Oreja de Van Gogh", "TUTIORODRI", 4));
            songRepository.save(new Song("Bokete", "Bad Bunny", "TUTIORODRI", 4));

            // Día 5 (Viernes)
            songRepository.save(new Song("Sirenas", "Taburete", "TUTIORODRI", 5));
            songRepository.save(new Song("Me jalo", "Grupo Frontera", "TUTIORODRI", 5));
            songRepository.save(new Song("Bones", "Imagine Dragons", "TUTIORODRI", 5));
            songRepository.save(new Song("Bzrp Music Sessions vol.50", "Bizarrap, Duki", "TUTIORODRI", 5));

            // Día 6 (Sábado)
            songRepository.save(new Song("Runaway", "Kanye West, Pusha T", "TUTIORODRI", 6));
            songRepository.save(new Song("Animals", "Maroon 5", "TUTIORODRI", 6));
            songRepository.save(new Song("Me rehusó", "Danny Ocean", "TUTIORODRI", 6));
            songRepository.save(new Song("Levitating", "Dua Lipa", "TUTIORODRI", 6));

            // Día 7 (Domingo)
            songRepository.save(new Song("I gotta feeling", "Black Eyed Peas", "TUTIORODRI", 7));
            songRepository.save(new Song("My way", "Calvin Harris", "TUTIORODRI", 7));
            songRepository.save(new Song("Thunderstruck", "AC/DC", "TUTIORODRI", 7));
            songRepository.save(new Song("Kiss me", "Sixpence None The Richer", "TUTIORODRI", 7));

            // ==========================================
            // 🔴 PAPICHECHU (Días 1 a 7)
            // ==========================================
            // Día 1 (Lunes)
            songRepository.save(new Song("Scandic", "Quevedo", "PAPICHECHU", 1));
            songRepository.save(new Song("Volando remix", "Mora", "PAPICHECHU", 1));
            songRepository.save(new Song("La inocente", "Mora", "PAPICHECHU", 1));
            songRepository.save(new Song("El Baifo", "Quevedo", "PAPICHECHU", 1));

            // Día 2 (Martes)
            songRepository.save(new Song("De lejitos remix", "Jay Wheeler", "PAPICHECHU", 2));
            songRepository.save(new Song("Bandolera", "Anuel AA", "PAPICHECHU", 2));
            songRepository.save(new Song("No tiene sentido", "Beele", "PAPICHECHU", 2));
            songRepository.save(new Song("El patio", "Pepe Yvizio", "PAPICHECHU", 2));

            // Día 3 (Miércoles)
            songRepository.save(new Song("Mal de amores", "Juan Magan, J Balvin", "PAPICHECHU", 3));
            songRepository.save(new Song("Polaris remix", "Saiko, Quevedo", "PAPICHECHU", 3));
            songRepository.save(new Song("Manos rotas", "Morad, Dellafuente", "PAPICHECHU", 3));
            songRepository.save(new Song("Una locura", "Ozuna, J Balvin", "PAPICHECHU", 3));

            // Día 4 (Jueves)
            songRepository.save(new Song("Una noti", "Omar Courtz", "PAPICHECHU", 4));
            songRepository.save(new Song("Callaita", "Bad Bunny", "PAPICHECHU", 4));
            songRepository.save(new Song("120", "Bad Bunny", "PAPICHECHU", 4));
            songRepository.save(new Song("Yo y tú", "Quevedo, Beele", "PAPICHECHU", 4));

            // Día 5 (Viernes)
            songRepository.save(new Song("Bandida", "Mora", "PAPICHECHU", 5));
            songRepository.save(new Song("Buenas noches", "Quevedo", "PAPICHECHU", 5));
            songRepository.save(new Song("Soleao", "Myke Towers, Quevedo", "PAPICHECHU", 5));
            songRepository.save(new Song("Dame la verde", "Dei V, Luar La L", "PAPICHECHU", 5));

            // Día 6 (Sábado)
            songRepository.save(new Song("Tuchat", "Quevedo", "PAPICHECHU", 6));
            songRepository.save(new Song("Forever tu gangtel", "Omar Courtz, Ñengo Flow", "PAPICHECHU", 6));
            songRepository.save(new Song("Q vas a hacer hoy?", "Omar Courtz, Dellarose", "PAPICHECHU", 6));
            songRepository.save(new Song("Mas finas", "Cosculluela", "PAPICHECHU", 6));

            // Día 7 (Domingo)
            songRepository.save(new Song("Ley seca", "Anuel AA", "PAPICHECHU", 7));
            songRepository.save(new Song("Droga", "Mora", "PAPICHECHU", 7));
            songRepository.save(new Song("She dont give fo", "Duki", "PAPICHECHU", 7));
            songRepository.save(new Song("Buenas", "Quevedo", "PAPICHECHU", 7));
        }
    }
}