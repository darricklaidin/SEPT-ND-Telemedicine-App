package com.sept.authmicroservice;

import com.sept.authmicroservice.model.Role;
import com.sept.authmicroservice.model.RoleName;
import com.sept.authmicroservice.model.Specialty;
import com.sept.authmicroservice.repository.RoleRepository;
import com.sept.authmicroservice.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final RoleRepository roleRepository;
    private final SpecialtyRepository specialtyRepository;

    private boolean alreadySetup = false;

    protected static final String[] INITIAL_SPECIALTIES = { "General Physician", "Dermatology", "Pediatric" };

    @Autowired
    public SetupDataLoader(RoleRepository roleRepository, SpecialtyRepository specialtyRepository) {
        this.roleRepository = roleRepository;
        this.specialtyRepository = specialtyRepository;
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
