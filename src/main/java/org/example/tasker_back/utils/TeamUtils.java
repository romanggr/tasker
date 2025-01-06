package org.example.tasker_back.utils;

import org.example.tasker_back.dto.team.CreateTeamRequest;
import org.springframework.stereotype.Component;

@Component
public class TeamUtils {

    public void validateCreateTeam(CreateTeamRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or null");
        }

        if (request.getCreatorEmail() == null || request.getCreatorEmail().isEmpty()) {
            throw new IllegalArgumentException("Creator email cannot be empty or null");
        }

        if (!isValidEmail(request.getCreatorEmail())) {
            throw new IllegalArgumentException("Invalid creator email format");
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }


}
