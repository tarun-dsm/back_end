package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.pet_info.*;
import toyproject.syxxn.back_end.entity.pet_image.PetImage;
import toyproject.syxxn.back_end.entity.pet_image.PetImageRepository;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.S3Util;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {

    private final AccountRepository accountRepository;
    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository petImageRepository;

    private final AuthenticationUtil authenticationFacade;
    private final PostUtil postUtil;
    private final UserUtil userUtil;
    private final S3Util s3Util;

    @Transactional
    public void deletePost(Integer postId) {
        Post post = postUtil.getPost(postId);
        postUtil.postIsMine(userUtil.getLocalConfirmAccount(), post);

        for (PetImage petImage : post.getPetImages()) {
            s3Util.delete(petImage.getSavedPath());
        }

        postRepository.delete(post);
    }

    @Transactional
    public Integer writePost(PostRequest request) {
        Account account = userUtil.getLocalConfirmAccount();
        String startDate = request.getProtectionStartDate();
        String endDate = request.getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);
        Integer postId = 0;
        try {
             Post post = postRepository.save(new Post(request, account, false));
             petInfoRepository.save(post.getPetInfo());
             postId = post.getId();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return postId;
    }

    public Integer updatePost(Integer postId, PostRequest request) {
        Account account = userUtil.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId);
        postUtil.postIsMine(account, post);

        String startDate = request.getProtectionStartDate();
        String endDate = request.getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);

        post.update(request);
        post.getPetInfo().update(request);
        postRepository.save(post);
        petInfoRepository.save(post.getPetInfo());

        return post.getId();
    }

    public PostDetailsResponse getPostDetails(Integer postId) {
        Account me = userUtil.getLocalConfirmAccount();

        Post post = postUtil.getPost(postId);
        Account writer = post.getAccount();
        PetInfo petInfo = post.getPetInfo();

        List<PetImage> petImages = petImageRepository.findAllByPost(post);

        return PostDetailsResponse.builder()
                .writerId(writer.getId())
                .rating(writer.getRating().getRating())
                .isMine(postUtil.postIsMine(me, post))
                .isApplied(isApplied(me, post))
                .nickname(post.getAccount().getNickname())
                .pet(PostDetailsResponse.PetDetailsDto.builder()
                        .petName(petInfo.getPetName())
                        .petSpecies(petInfo.getPetSpecies())
                        .petSex(petInfo.getPetSex().toString())
                        .animalType(petInfo.getAnimalType().toString())
                        .filePaths(petImages.stream()
                                .map(img -> s3Util.getS3ObjectUrl(img.getSavedPath()))
                                .collect(Collectors.toList()))
                        .build())
                .post(PostDetailsResponse.PostDetailsDto.builder()
                        .title(post.getTitle())
                        .description(post.getDescription())
                        .protectionStartDate(post.getProtectionStartDate())
                        .protectionEndDate(post.getProtectionEndDate())
                        .applicationEndDate(post.getApplicationEndDate())
                        .contactInfo(post.getContactInfo())
                        .createdAt(post.getCreatedAtToString())
                        .isApplicationEnd(post.getIsApplicationEnd())
                        .isUpdated(post.getIsUpdated())
                        .build())
                .build();
    }

    public PostResponse getPosts() {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(() -> UserNotFoundException.EXCEPTION);
        if (!account.getIsLocationConfirm()) {
            return new PostResponse(Optional.of(postRepository.findAllByPetImagesNotNullOrderByCreatedAtDesc()).orElse(new ArrayList<>()).stream()
                    .map(this::getPost).collect(Collectors.toList()));
        }

        return new PostResponse(postRepository.findAllByIsApplicationEndFalseAndPetImagesNotNullOrderByCreatedAtDesc().stream()
                .filter(post -> distance(account, post.getAccount()) <= 2.0)
                .map(this::getPost).collect(Collectors.toList())
        );
    }

    private void startDateAfterEndDate(String startDate, String endDate) {
        if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
            throw InvalidScheduleSettingException.EXCEPTION;
        }
    }

    private double distance(Account account, Account writer) {
        double writerLon = writer.getLongitude().doubleValue();
        double writerLat = writer.getLatitude().doubleValue();
        double myLon = account.getLongitude().doubleValue();
        double myLat = account.getLatitude().doubleValue();

        double theta = Math.abs(writerLon - myLon);
        double distance = Math.sin(degreeToRadian(myLat)) * Math.sin(degreeToRadian(writerLat)) + Math.cos(degreeToRadian(myLat)) * Math.cos(degreeToRadian(writerLat)) * Math.cos(degreeToRadian(theta));

        distance = Math.acos(distance);
        distance = radianToDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;

        return distance;
    }

    private double degreeToRadian(double deg) {
        return (deg * Math.PI / 180);
    }

    private double radianToDegrees(double rad) {
        return (rad * 180 / Math.PI);
    }
    
    private PostResponse.PostResponseDto getPost(Post post) {
        return PostResponse.PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .animalType(post.getPetInfo().getAnimalType().toString())
                .administrationDivision(post.getAccount().getAdministrationDivision())
                .firstImagePath(s3Util.getS3ObjectUrl(post.getFirstImagePath()))
                .protectionStartDate(post.getProtectionStartDate())
                .protectionEndDate(post.getProtectionEndDate())
                .build();
    }

    private boolean isApplied(Account account, Post post) {
        if (postUtil.postIsMine(account, post)) return false;
        return applicationRepository.findByPostAndApplicant(post, account).isPresent();
    }

}
