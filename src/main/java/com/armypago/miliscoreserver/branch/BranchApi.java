package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.BranchDetailDto;
import com.armypago.miliscoreserver.branch.dto.BranchListDto;
import com.armypago.miliscoreserver.branch.validator.NameValidator;
import com.armypago.miliscoreserver.config.auth.LoginUser;
import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@RequestMapping(BranchApi.BRANCH_URL)
@RestController
public class BranchApi {

    static final String BRANCH_URL = "/api/v1/branch";

    private final BranchRepository branchRepository;
    private final BranchQueryRepository branchQueryRepository;
    private final NameValidator nameValidator;

    // TODO : APP 용 오류 메시지, ResponseBody or ResponseEntity 형식으로 변경
    // TODO : 예외 처리 추가
    // TODO : 로그인 인증 상태 확인 (DELETE, UPDATE)
    // TODO : 조건부 검색 추가

    @InitBinder("name")
    public void nameInitBinder(WebDataBinder webDataBinder){
        webDataBinder.addValidators(nameValidator);
    }
    
    @PostMapping
    public Long create(@RequestBody BranchDetailDto.Request request){
        // TODO error handling : 일단 저장은 됨
        branchRepository.findByName(request.getName()).ifPresent(b -> {
            throw new IllegalArgumentException("이미 존재하는 병과명입니다.");
        });
        return branchRepository.save(request.toEntity()).getId();
    }

    @PutMapping("/{id}")
    public Long update(@PathVariable Long id,
                       @RequestBody BranchDetailDto.Request branchRequestDto){
        // TODO error handling : 일단 저장은 됨
        branchRepository.findByName(branchRequestDto.getName()).ifPresent(b -> {
            throw new IllegalArgumentException("이미 존재하는 병과명입니다.");
        });
        return branchRepository.findById(id).map(branch -> {
            branch.changeInfo(branchRequestDto.getName());
            return branch;
        }).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 병과입니다. id="+id)).getId();
    }

    @GetMapping("/{id}")
    public BranchDetailDto.Response get(@PathVariable Long id){
        return branchQueryRepository.findById(id);
    }

    @GetMapping
    public List<BranchListDto> getList(){
        return branchRepository.findAll().stream().map(BranchListDto::new).collect(toList());
    }

    @DeleteMapping("/{id}")
    public Long delete(@PathVariable Long id){
        branchRepository.deleteById(id);
        return id;
    }
}
