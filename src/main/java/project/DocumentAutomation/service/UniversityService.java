package project.DocumentAutomation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.DocumentAutomation.domain.University;
import project.DocumentAutomation.dto.UniversityMemberDto;
import project.DocumentAutomation.repository.UniversityRepository;

@Service
@RequiredArgsConstructor
public class UniversityService {

    private final UniversityRepository universityRepository;

    @Transactional
    public void addMemberToUniversity(String universityName, UniversityMemberDto memberDto) {
        // universityName으로 대학교 찾기
        University university = universityRepository.findById(universityName)
                .orElseThrow(() -> new IllegalArgumentException("University not found: " + universityName));

        // 부원 추가
        university.addMember(memberDto.getName(), memberDto.getBirthDate(), memberDto.getPhoneNumber(),
                memberDto.getEmail(), memberDto.getGender(), memberDto.getHabitatId(),
                memberDto.getWaiverSubmitted(), memberDto.getConsentSubmitted());
    }
}
