package com.gestion.eventos.api.data;

import com.gestion.eventos.api.domain.Category;
import com.gestion.eventos.api.domain.Role;
import com.gestion.eventos.api.domain.Speaker;
import com.gestion.eventos.api.domain.User;
import com.gestion.eventos.api.repository.CategoryRepository;
import com.gestion.eventos.api.repository.RoleRepository;
import com.gestion.eventos.api.repository.SpeakerRepository;
import com.gestion.eventos.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;
    private final SpeakerRepository speakerRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // --- 1. Crear y Guardar Roles si no existen ---
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet( () -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return roleRepository.save(newRole);
                });

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet( () -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        // --- 2. Crear un usuario ADMIN si no existe ---
        if(userRepository.findByUsername("admin").isEmpty()){
            User admin = new User();
            admin.setName("Administrador");
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin1234"));

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            adminRoles.add(userRole);
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            System.out.println("Usuario 'admin' creado.");
        }

        // --- 3. Crear un usuario USER si no existe ---
        if(userRepository.findByUsername("user").isEmpty()){
            User user = new User();
            user.setName("Usuario Normal");
            user.setUsername("user");
            user.setEmail("user@example.com");
            user.setPassword(passwordEncoder.encode("123456"));

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(userRole);
            user.setRoles(userRoles);

            userRepository.save(user);
            System.out.println("Usuario 'user' creado.");
        }

        // --- 4. Crear y Guardar Categorías si no existen ---
        if (!categoryRepository.existsByName("Conferencia")) {
            Category conferencia = new Category(null, "Conferencia", "Eventos de gran escala con múltiples oradores.");
            categoryRepository.save(conferencia);
        }
        if (!categoryRepository.existsByName("Taller")) {
            Category taller = new Category(null, "Taller", "Eventos interactivos y prácticos.");
            categoryRepository.save(taller);
        }
        if (!categoryRepository.existsByName("Webinar")) {
            Category webinar = new Category(null, "Webinar", "Seminarios online en vivo.");
            categoryRepository.save(webinar);
        }

        // --- 5. Crear y Guardar Oradores si no existen ---
        if (!speakerRepository.existsByEmail("john.doe@example.com")) {
            Speaker john = new Speaker(null, "John Doe", "john.doe@example.com", "Experto en desarrollo de software.", new HashSet<>());
            speakerRepository.save(john);
        }
        if (!speakerRepository.existsByEmail("jane.smith@example.com")) {
            Speaker jane = new Speaker(null, "Jane Smith", "jane.smith@example.com", "Especialista en marketing digital.", new HashSet<>());
            speakerRepository.save(jane);
        }

    }
}
