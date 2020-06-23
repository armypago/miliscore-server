package com.armypago.miliscoreserver.user;

import com.armypago.miliscoreserver.config.auth.dto.OAuthAttributes;
import com.armypago.miliscoreserver.config.auth.dto.SessionUser;
import com.armypago.miliscoreserver.domain.user.Education;
import com.armypago.miliscoreserver.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Transactional
@Service
public class UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final EducationRepository educationRepository;
    private final UserRepository userRepository;
    private final HttpSession httpSession;

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

    // TODO 계정 생성 화면

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(
                registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveUser(attributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveUser(OAuthAttributes attributes){
        return userRepository.findByEmail(attributes.getEmail())
                .orElseGet(() -> userRepository.save(attributes.toEntity()));
    }
}
