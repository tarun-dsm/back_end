package toyproject.syxxn.back_end.service.post;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import toyproject.syxxn.back_end.dto.request.PostRequest;
import toyproject.syxxn.back_end.dto.response.*;
import toyproject.syxxn.back_end.entity.Sex;
import toyproject.syxxn.back_end.entity.account.Account;
import toyproject.syxxn.back_end.entity.account.AccountRepository;
import toyproject.syxxn.back_end.entity.pet.*;
import toyproject.syxxn.back_end.entity.post.Post;
import toyproject.syxxn.back_end.entity.post.PostRepository;
import toyproject.syxxn.back_end.exception.*;
import toyproject.syxxn.back_end.service.facade.AuthenticationFacade;
import toyproject.syxxn.back_end.service.facade.BaseService;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService {

    private final AccountRepository accountRepository;
    private final PostRepository postRepository;
    private final PetInfoRepository petInfoRepository;
    private final PetImageRepository petImageRepository;

    private final AuthenticationFacade authenticationFacade;

    private final BaseService baseService;

    @Value("${file.path}")
    private String path;

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        if (!baseService.getLocalConfirmAccount().equals(post.getAccount())) {
            throw new UserNotAccessibleException();
        }
        postRepository.delete(post);
    }

    @Override
    public void saveFiles(Integer postId, List<MultipartFile> files) {
        Post post = postRepository.findById(postId)
                .filter(p -> p.getAccount().equals(baseService.getLocalConfirmAccount()))
                .orElseThrow(PostNotFoundException::new);

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
        Account account = baseService.getLocalConfirmAccount();
        String startDate = request.getProtectionStartDate();
        String endDate = request.getProtectionEndDate();

        startDateAfterEndDate(startDate, endDate);
        Post post = postRepository.save(
                Post.builder()
                        .title(request.getTitle())
                        .description(request.getDescription())
                        .account(account)
                        .protectionStartDate(LocalDate.parse(startDate))
                        .protectionEndDate(LocalDate.parse(endDate))
                        .applicationEndDate(LocalDate.parse(request.getApplicationEndDate()))
                        .contactInfo(request.getContactInfo())
                        .isApplicationEnd(false)
                        .isUpdated(false)
                        .build()
        );

        petInfoRepository.save(
                PetInfo.builder()
                        .petName(request.getPetName())
                        .petSpecies(request.getPetSpecies())
                        .petSex(Sex.valueOf(request.getPetSex()))
                        .post(post)
                        .animalType(AnimalType.valueOf(request.getAnimalType()))
                        .build()
        );

        return post.getId();
    }

    @Override
    public Integer updatePost(Integer postId, PostRequest request) {
        Account account = baseService.getLocalConfirmAccount();
        Post post = postRepository.findById(postId)
                .filter(p -> p.getAccount().equals(account))
                .filter(p -> !p.getIsApplicationEnd())
                .orElseThrow(PostNotFoundException::new);


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
        baseService.getLocalConfirmAccount();

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
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
            int i = 1;
            for (MultipartFile file : files) {
                if (file.getOriginalFilename() == null) {
                    throw new FileNotFoundException();
                }
                String filePath = path + post.getId() + file.getOriginalFilename() + LocalDateTime.now();

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

}
