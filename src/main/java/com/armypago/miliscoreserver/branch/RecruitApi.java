package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.RecruitCreate;
import com.armypago.miliscoreserver.domain.branch.Recruit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.armypago.miliscoreserver.branch.RecruitApi.*;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(RECRUIT_URL)
@RestController
public class RecruitApi {

    static final String RECRUIT_URL = "/api/v1/recruit";

    private final RecruitRepository recruitRepository;
    private final BranchRepository branchRepository;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody RecruitCreate request){
        List<Recruit> recruits = request.getDatas().stream()
                .map(data -> branchRepository.findByName(data.getBranchName())
                        .map(data::toEntity).orElseThrow(IllegalArgumentException::new))
                .collect(toList());
        recruitRepository.saveAll(recruits);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
