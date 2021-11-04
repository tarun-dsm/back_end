package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.application.ApplicationRepository;
import toyproject.syxxn.back_end.entity.pet.*;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.util.AuthenticationUtil;
import toyproject.syxxn.back_end.service.util.PostUtil;
import toyproject.syxxn.back_end.service.util.UserUtil;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final AccountRepository accountRepository;
    private final ApplicationRepository applicationRepository;
    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository petImageRepository;

    private final AuthenticationUtil authenticationFacade;
    private final PostUtil postUtil;
    private final UserUtil userUtil;

    @Value("${file.path}")
    private String path;

    @Override
    public void deletePost(Integer postId) {
        Post post = postUtil.getPost(postId, userUtil.getLocalConfirmAccount());
        postRepository.delete(post);
    }

    @Override
    public void saveFiles(Integer postId, List<MultipartFile> files) {
        Post post = postUtil.getPost(postId, userUtil.getLocalConfirmAccount());

        for (PetImage petImage : post.getPetImages()) {
            File file = new File(petImage.getPath());
            if (file.exists() && file.delete())
                petImageRepository.delete(petImage);
        }

        saveFile(files, post);
    }

    @Transactional
    @Override
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

    @Override
    public Integer updatePost(Integer postId, PostRequest request) {
        Account account = userUtil.getLocalConfirmAccount();
        Post post = postUtil.getPost(postId, account);

        String startDate = request.getProtectionStartDate();
        String endDate = request.getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);

        post.update(request);
        post.getPetInfo().update(request);
        postRepository.save(post);
        petInfoRepository.save(post.getPetInfo());

        return post.getId();
    }

    @Override
    public PostDetailsResponse getPostDetails(Integer postId) {
        userUtil.getLocalConfirmAccount();

        Post post = postUtil.getPost(postId);
        Account account = post.getAccount();
        PetInfo petInfo = post.getPetInfo();

        List<PetImage> petImages = petImageRepository.findAllByPost(post);
        List<String> filePaths = new ArrayList<>();

        for (PetImage petImage : petImages) {
            filePaths.add(petImage.getPath());
        }

        PostDetailsDto postDetailsDto = PostDetailsDto.builder()
                .title(post.getTitle())
                .description(post.getDescription())
                .protectionStartDate(post.getProtectionStartDate())
                .protectionEndDate(post.getProtectionEndDate())
                .applicationEndDate(post.getApplicationEndDate())
                .contactInfo(post.getContactInfo())
                .createdAt(post.getCreatedAt())
                .isApplicationEnd(post.getIsApplicationEnd())
                .isUpdated(post.getIsUpdated())
                .build();

        PetDetailsDto petDetailsDto = PetDetailsDto.builder()
                .petName(petInfo.getPetName())
                .petSpecies(petInfo.getPetSpecies())
                .petSex(petInfo.getPetSex().toString())
                .animalType(petInfo.getAnimalType().toString())
                .filePaths(filePaths)
                .build();

        return PostDetailsResponse.builder()
                .writerId(account.getId())
                .rating(account.getRating())
                .isMine(postUtil.postIsMine(account, post))
                .isApplied(isApplied(account, post))
                .nickname(post.getAccount().getNickname())
                .pet(petDetailsDto)
                .post(postDetailsDto)
                .build();
    }

    @Override
    public PostResponse getPosts() {
        Account account = accountRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);
        if (!account.getIsLocationConfirm()) {
            List<Post> latelyPost = postRepository.findAllByOrderByCreatedAtDesc();
            return new PostResponse(latelyPost.stream()
                    .map(this::getPost).collect(Collectors.toList())
            );
        }

        List<Post> posts = postRepository.findAllByIsApplicationEndFalseOrderByCreatedAtDesc();
        return new PostResponse(posts.stream()
                .filter(post -> distance(account, post.getAccount()) >= 2.0)
                .map(this::getPost).collect(Collectors.toList())
        );
    }

    private void startDateAfterEndDate(String startDate, String endDate) {
        if (LocalDate.parse(startDate).isAfter(LocalDate.parse(endDate))) {
            throw new InvalidScheduleSettingException();
        }
    }

    private void saveFile(List<MultipartFile> files, Post post) {
        try {
            if (files.size() > 5) {
                throw new FileNumberExceedException();
            }
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() == null) {
                    throw new FileNotFoundException();
                }
                String filePath = path + post.getId() + "-" + UUID.randomUUID();

                String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                if (!(extension.contains("JPG") || extension.contains("jpg") || extension.contains("jpeg") || extension.contains("JPEG") || extension.contains("png") || extension.contains("PNG"))) {
                    throw new InvalidFileTypeException();
                }

                file.transferTo(new File(filePath));
                petImageRepository.save(new PetImage(post, filePath));
            }
        } catch (Exception e) {
            postRepository.delete(post);
            petInfoRepository.delete(post.getPetInfo());
            e.printStackTrace();
            throw new FileSaveFailedException();
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
    
    private PostResponseDto getPost(Post post) {
        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .animalType(post.getPetInfo().getAnimalType().toString())
                .administrationDivision(post.getAccount().getAdministrationDivision())
                .firstImagePath(post.getPetImages().get(0).getPath())
                .protectionStartDate(post.getProtectionStartDate())
                .protectionEndDate(post.getProtectionEndDate())
                .build();
    }

    private boolean isApplied(Account account, Post post) {
        if (postUtil.postIsMine(account, post)) return false;
        return applicationRepository.findByPostAndAccount(post, account).orElse(null) != null;
    }

}
