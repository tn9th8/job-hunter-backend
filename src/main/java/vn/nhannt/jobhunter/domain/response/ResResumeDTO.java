package vn.nhannt.jobhunter.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.nhannt.jobhunter.domain.entity.Resume;
import vn.nhannt.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResResumeDTO {
    // attribute
    private Long id;

    private String email;
    private String url;
    private StatusEnum status;

    private UserKey user;
    private JobKey job;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserKey {
        private Long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobKey {
        private Long id;
        private String name;
    }

    public static ResResumeDTO mapFrom(Resume dbResume) {
        final ResResumeDTO resResume = new ResResumeDTO(
                dbResume.getId(),
                dbResume.getEmail(),
                dbResume.getUrl(),
                dbResume.getStatus(),
                null,
                null);
        // check user
        if (dbResume.getUser() != null) {
            ResResumeDTO.UserKey userKey = new UserKey(
                    dbResume.getUser().getId(),
                    dbResume.getUser().getName());
            resResume.setUser(userKey);

        }
        // check job
        if (dbResume.getJob() != null) {
            ResResumeDTO.JobKey jobKey = new JobKey(
                    dbResume.getJob().getId(),
                    dbResume.getJob().getName());
            resResume.setJob(jobKey);
        }

        return resResume;
    }

}
