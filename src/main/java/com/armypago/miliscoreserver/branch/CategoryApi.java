package com.armypago.miliscoreserver.branch;

import com.armypago.miliscoreserver.branch.dto.CategoryDetail;
import com.armypago.miliscoreserver.domain.branch.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.armypago.miliscoreserver.branch.CategoryApi.CATEGORY_URL;

@RequiredArgsConstructor
@RequestMapping(CATEGORY_URL)
@RestController
public class CategoryApi {

    static final String CATEGORY_URL = "/api/v1/category";

    private final CateogryRepository cateogryRepository;
    private final CategoryQueryRepository categoryQueryRepository;

    // TODO 중복 검사

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CategoryDetail.Request request, Errors errors){
        CategoryDetail.Response response = null;
        if(!errors.hasErrors()){
            Category category = cateogryRepository.save(request.toEntity());
            response = new CategoryDetail.Response(category, null);
        }
        return response != null ?
                ResponseEntity.status(HttpStatus.OK).body(response) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(errors.getAllErrors().get(0).getDefaultMessage());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id){
        CategoryDetail.Response response = categoryQueryRepository.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<?> getList(){
        return ResponseEntity.status(HttpStatus.OK).body(cateogryRepository.findAll());
    }
}
