package sept.superfive.authmicroservice;

import sept.superfive.authmicroservice.model.Role;
import sept.superfive.authmicroservice.model.RoleName;
import sept.superfive.authmicroservice.model.Specialty;
import sept.superfive.authmicroservice.model.User;
import sept.superfive.authmicroservice.payload.DoctorSignUp;
import sept.superfive.authmicroservice.repository.RoleRepository;
import sept.superfive.authmicroservice.repository.SpecialtyRepository;
import sept.superfive.authmicroservice.repository.UserRepository;
import sept.superfive.authmicroservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;
    private final AuthService authService;

    protected static final String[] INITIAL_SPECIALTIES = {"General Physician", "Dermatology", "Pediatric"};

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository, SpecialtyRepository specialtyRepository,
                           UserRepository userRepository, AuthService authService) {
        this.roleRepository = roleRepository;
        this.specialtyRepository = specialtyRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {

        // == create initial roles
        createRolesIfNotFound();

        // == create initial specialties
        createSpecialtiesIfNotFound();

        // == create doctors
        createDoctorsIfNotFound();

        // == create admins
        createAdminIfNotFound();
    }

    @Transactional
    void createAdminIfNotFound() {
        if (Boolean.FALSE.equals(
                userRepository.existsByRolesIn(new ArrayList<>(Collections.singletonList((roleRepository.findByName(RoleName.ADMIN))))))) {
            userRepository.save(new User(-1, new ArrayList<>(Collections.singletonList(roleRepository.findByName(RoleName.ADMIN)))));
        }
    }

    @Transactional
    void createRolesIfNotFound() {
        for (RoleName roleName : RoleName.values()) {
            if (Boolean.FALSE.equals(roleRepository.existsByName(roleName))) {
                roleRepository.save(new Role(roleName));
            }
        }

    }

    @Transactional
    void createSpecialtiesIfNotFound() {
        String specialtyName;
        for (String initialSpecialty : INITIAL_SPECIALTIES) {
            specialtyName = initialSpecialty;
            if (Boolean.FALSE.equals(specialtyRepository.existsBySpecialtyName(specialtyName))) {
                specialtyRepository.save(new Specialty(specialtyName));
            }
        }
    }

    @Transactional
    void createDoctorsIfNotFound() {
        // Gets the first specialty ID (may not be '1')
        int specialtyID = specialtyRepository.findAllBy(null).getContent().get(0).getSpecialtyID();

        for (int i = 1; i < 4; ++i) {
            String email = "doc" + i + "@doc.com";
            if (Boolean.FALSE.equals(userRepository.existsByEmail(email))) {
                authService.registerDoctor(new DoctorSignUp("Doc", String.valueOf(i), email, "password",
                        "1999-08-25", specialtyID));
            }
        }
    }
}
