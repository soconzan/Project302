package org.example.project302.univ;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityService {
    private final UniversityRepository univRepository;

    public List<University> getAllUniv() {
        return univRepository.findAll();
    }


}
