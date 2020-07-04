package com.armypago.miliscoreserver.user;

import com.armypago.miliscoreserver.branch.BranchRepository;
import com.armypago.miliscoreserver.branch.dto.BranchSimple;
import com.armypago.miliscoreserver.domain.branch.Branch;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.MilitaryServiceStatus;
import com.armypago.miliscoreserver.domain.user.User;
import com.armypago.miliscoreserver.user.dto.UserCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final EducationRepository educationRepository;

    public User initiateUser(Long userId, UserCreate.Request request){
        Optional<Education> education = educationRepository.findById(request.getEducationId());
        Optional<Branch> branch = branchRepository.findById(request.getBranchId());
        User user = userRepository.findById(userId).orElseThrow(IllegalArgumentException::new);
        if(education.isPresent() && branch.isPresent()){
            user.initialize(request.getName(), request.getEmail(), request.getStatus(),
                    education.get(), branch.get(), request.getMajor());
        }
        return user;
    }

    public UserCreate.Form loadForm(Long userId){
        UserCreate.Form form = new UserCreate.Form();
        form.setUserId(userId);
        form.setStatus(MilitaryServiceStatus.getList());
        form.setEducation(educationRepository.findAll().stream()
                .map(UserCreate.EducationSimple::new).collect(toList()));
        form.setBranch(branchRepository.findAll().stream()
                .map(BranchSimple::new).collect(toList()));
        return form;
    }

    public User loadUser(String token){
        return userRepository.findByToken(token)
                .orElseGet(() -> createUser(token));
    }

    private User createUser(String token){
        User user = User.builder()
                .token(token).build();
        return userRepository.save(user);
    }

//    public OAuth2User loadUser(OAuth2UserRequest userRequest)
//            throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
//                .getUserInfoEndpoint().getUserNameAttributeName();
//
//        OAuthAttributes attributes = OAuthAttributes.of(
//                registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        User user = saveUser(attributes);
//        httpSession.setAttribute("user", new SessionUser(user));
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
//    }
}
