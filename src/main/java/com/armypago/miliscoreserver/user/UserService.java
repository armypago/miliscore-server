package com.armypago.miliscoreserver.user;

import com.armypago.miliscoreserver.domain.user.Education;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;

    // TODO: Spring Batch로 데이터 초기화 방식 변경
    @Profile("local")
    @PostConstruct
    private void initEducationData(){
        if(educationRepository.count() == 0){
            AtomicInteger idx = new AtomicInteger(1);
            List<Education> educations = Stream.of("고졸", "대재", "대졸", "휴학", "석재", "석졸")
                    .map(s -> Education.builder().priority(idx.getAndIncrement()).name(s).build())
                    .collect(toList());
            educationRepository.saveAll(educations);
        }
    }
}
