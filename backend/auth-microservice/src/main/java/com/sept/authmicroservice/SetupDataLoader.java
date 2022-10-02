package com.sept.authmicroservice;

import com.sept.authmicroservice.model.Role;
import com.sept.authmicroservice.model.RoleName;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.model.User;
import com.sept.authmicroservice.repository.RoleRepository;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import com.sept.authmicroservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;
    private final UserRepository userRepository;

    private boolean alreadySetup = false;

    protected static final String[] INITIAL_SPECIALTIES = { "General Physician", "Dermatology", "Pediatric" };

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository, SpecialtyRepository specialtyRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.specialtyRepository = specialtyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // == create initial roles
        createRolesIfNotFound();

        // == create initial specialties
        createSpecialtiesIfNotFound();

        // == create admins
        createAdminIfNotFound();
    }

    @Transactional
    void createAdminIfNotFound() {
        if (Boolean.FALSE.equals(userRepository.existsByRolesIn(new ArrayList<>(Arrays.asList((roleRepository.findByName(RoleName.ADMIN))))))) {
            userRepository.save(new User(-1, new ArrayList<>(Arrays.asList(roleRepository.findByName(RoleName.ADMIN)))));
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
        for (int i = 0; i < INITIAL_SPECIALTIES.length; ++i) {
            specialtyName = INITIAL_SPECIALTIES[i];
            if (Boolean.FALSE.equals(specialtyRepository.existsBySpecialtyName(specialtyName))) {
                specialtyRepository.save(new Specialty(specialtyName));
            }
        }

    }
}
